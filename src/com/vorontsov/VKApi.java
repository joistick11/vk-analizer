package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.model.UserDetailed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApi {
    private static VKApi instance = null;
    //https://oauth.vk.com/authorize?client_id=4763444&scope=13&redirect_uri=http://api.vkontakte.ru/blank.html&display=page&v=5.21&response_type=token
    private final static String token = "67ac0f63a21163006034f271562548251995413496ac323ee277d870c68dd73a25fc487dddf461ae72696";
    private boolean isDebugEnabled = true;

    private static JSONParser jsonParser = new JSONParser();

    private VKApi(){}

    public static VKApi getInstance() {
        if(instance == null)
            instance = new VKApi();
        return instance;
    }

    /**
     * Get <code>User</code> object by his id
     * @param id id of user
     * @return User
     */
    public User getUserById(int id){
        String url = createURL("users.get?user_id=" + id, "uid,first_name,last_name,city,domain,sex," +
                "bdate,followers_count,relation,personal,wall_comments,music,movies,tv,books,games,can_post,can_see_audio,can_write_private_message,interests,activities");
        System.out.println("Created url for start user: " + url);

        User user = null;
        try{
            String JSONresponse = executeHttpRequest(url);
            if(JSONresponse == null)
                return null;

            JSONObject jsonResp = (JSONObject) jsonParser.parse(JSONresponse);
            JSONArray userInfo = (JSONArray) jsonResp.get("response");

            user = getUserByUserInfo((JSONObject) userInfo.get(0));
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Get user friend's by his id
     * @param id id of user to get friends for
     * @return List of user friends
     */
    public List<User> getUsersFriends(int id){
        //System.out.println("[vkAPI] get friends for " + id);
        String url = createURL("friends.get?user_id=" + id, "uid,first_name,last_name,city,domain,sex," +
                "bdate,followers_count,relation,personal,wall_comments,music,movies,tv,books,games,can_post,can_see_audio,can_write_private_message,interests,activities");
        System.out.println("Created getFriendURL: " + url);

        List<User> friends = new LinkedList<>();
        try{
            String JSONresponse = executeHttpRequest(url);
            if(JSONresponse == null)
                return null;

            JSONObject jsonResp = (JSONObject) jsonParser.parse(JSONresponse);
            JSONObject items = (JSONObject) jsonResp.get("response");
            if(items != null) {
                JSONArray itemsList = (JSONArray) items.get("items");
                for (Object userInfo : itemsList) {
                    friends.add(getUserByUserInfo((JSONObject) userInfo));
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return friends;
    }

    /**
     * Get <code>UserDetailed</code> by user id
     * @param id id of the user
     * @return UserDetailed
     * todo add implementation (do not forget that all user fields are required)
     */
    public UserDetailed getDetailedUser(int id){
        String url = createURL("users.get?user_id=" + id, "home_town,photo_max_orig,has_mobile,contacts,site,education,universities,schools," +
                "status,counters,occupation,nickname,connections");
        System.out.println(url);

        return null;
    }

    /**
     * Get detailed users from list of common users
     * @param simpleUsers HashMap with users
     * @return HashMap of <code>UserDetailed</code>
     */
    public HashMap<Integer, UserDetailed> getDetailedUsers(HashMap<Integer, User> simpleUsers){
        // 180 - max allowed number by VK
        return detailUsersByIds(getIdsFromUserList(new ArrayList<>(simpleUsers.values()), 180), simpleUsers);
    }

    /**
     * Detail users by their ids
     * @param idsPartitions ArrayList with Strings - partitions of user ids (180 in one)
     * @param simpleUsers HashMap with common users
     * @return HashMap of <code>UserDetailed</code>
     */
    private HashMap<Integer, UserDetailed> detailUsersByIds(ArrayList<String> idsPartitions, HashMap<Integer, User> simpleUsers){
        HashMap<Integer, UserDetailed> users = new HashMap<>();
        for (String idsPartition: idsPartitions) {
            String url = createURL("users.get?user_ids=" + idsPartition, "home_town,photo_max_orig,has_mobile,contacts,site,education,universities,schools," +
                    "status,counters,occupation,nickname,connections");
            if(isDebugEnabled)
                System.out.println(url);
            try{
                String JSONresponse = executeHttpRequest(url);
                if(JSONresponse == null)
                    return null;

                JSONObject jsonResp = (JSONObject) jsonParser.parse(JSONresponse);
                JSONArray items = (JSONArray) jsonResp.get("response");
                if(items != null) {
                    if(isDebugEnabled)
                        System.out.println(items);

                    for (Object userInfo : items){
                        int userId = Integer.parseInt(((JSONObject) userInfo).get("id").toString());
                        UserDetailed userDetailed = getDetailedUserByUserInfo((JSONObject) userInfo, simpleUsers.get(userId));
                        users.put(userDetailed.getId(), userDetailed);
                    }
                }
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    /**
     * Returns an ArrayList with strings containing <code>k</code> user ids
     * @param users list of users to create strings with ids for
     * @param k number of ids in one partition
     * @return all partitions with length < k
     */
    private ArrayList<String> getIdsFromUserList(List<User> users, int k){
        ArrayList<String> allPartitions = new ArrayList<>();
        String currPartition = users.get(0).getId() + ",";
        for (int i = 1; i < users.size(); i++) {
            if(i % k == 0){
                System.out.println("Current i=" + i);
                allPartitions.add(currPartition);
                currPartition = "";
            }
            currPartition += users.get(i).getId() + ",";
        }
        if (!currPartition.isEmpty())
            allPartitions.add(currPartition);
        return allPartitions;
    }

    /**
     * Get <code>User</code> from JSON
     * @param userInfo user info in JSON format
     * @return User object
     */
    private User getUserByUserInfo(final JSONObject userInfo){
        int userId = Integer.parseInt(userInfo.get("id").toString());
        String firstName = userInfo.get("first_name").toString();
        String lastName = userInfo.get("last_name").toString();
        String city = "Non specified";
        if (userInfo.get("city") != null){
            JSONObject cityObject = (JSONObject) userInfo.get("city");
            city = cityObject.get("title").toString();
        }
        int sex = Integer.parseInt(userInfo.get("sex").toString());
        String bdate = userInfo.get("bdate") != null ? userInfo.get("bdate").toString() : "Non specified";
        int followersCount = userInfo.get("followers_count") != null ? Integer.parseInt(userInfo.get("followers_count").toString()) : 0;
        int relation = userInfo.get("relation") != null ? Integer.parseInt(userInfo.get("relation").toString()) : 0;
        Map<String, String> personal = getPersonalInfoByData(userInfo);

        return new User(userId, firstName, lastName, city, sex, bdate, followersCount, relation, personal);
    }

    /**
     * Get <code>UserDetailed</code> from JSON
     * @param userInfo user info in JSON format
     * @param us common <code>User</code>
     * @return UserDetailed
     * todo refactor this method
     */
    private UserDetailed getDetailedUserByUserInfo(final JSONObject userInfo, User us){
        String homeTown = userInfo.get("home_town") != null ? userInfo.get("home_town").toString() : "Non specified";
        String photoURL = userInfo.get("photo_max_orig").toString();
        int hasMobile = userInfo.get("has_mobile") != null ? Integer.parseInt(userInfo.get("has_mobile").toString()) : 0;
        // Contacts
        Map<String, String> contacts = null;
        if(hasMobile == 1){
            contacts = new HashMap<>();
            if(userInfo.get("mobile_phone") != null)
                contacts.put("mobile_phone", userInfo.get("mobile_phone").toString());
            if(userInfo.get("home_phone") != null)
                contacts.put("home_phone", userInfo.get("home_phone").toString());
        }
        String site = (userInfo.get("site") != null) ? userInfo.get("site").toString() : "";
        // Education
        Map<String, String> education = null;
        if(userInfo.get("university") != null){
            education = new HashMap<>();
            education.put("university", userInfo.get("university").toString());
            education.put("university_name", userInfo.get("university_name").toString());
            if(userInfo.get("faculty") != null) {
                education.put("faculty", userInfo.get("faculty").toString());
                education.put("faculty_name", userInfo.get("faculty_name").toString());
            }
            if(userInfo.get("graduation") != null)
                education.put("graduation", userInfo.get("graduation").toString());
        }
        // Universities
        List<Map<String, String>> universities = null;
        if(userInfo.get("universities") != null){
            universities = new LinkedList<>();
            JSONArray universitiesArr = (JSONArray) userInfo.get("universities");
            for(Object universityObj : universitiesArr){
                JSONObject university = (JSONObject) universityObj;
                Map<String, String> un = new HashMap<>();
                un.put("id", university.get("id").toString());
                un.put("country", university.get("country").toString());
                un.put("city", university.get("city").toString());
                un.put("name", university.get("name").toString());
                if(university.get("faculty") != null) {
                    un.put("faculty", university.get("faculty").toString());
                    un.put("faculty_name", university.get("faculty_name").toString());
                }
                if(university.get("chair") != null) {
                    un.put("chair", university.get("chair").toString());
                    un.put("chair_name", university.get("chair_name").toString());
                }
                if(university.get("graduation") != null)
                    un.put("graduation", university.get("graduation").toString());
                universities.add(un);
            }
        }
        // Schools
        List<Map<String, String>> schools = null;
        if(userInfo.get("schools") != null){
            schools = new LinkedList<>();
            JSONArray schoolsArr = (JSONArray) userInfo.get("schools");
            for(Object schoolObj : schoolsArr){
                JSONObject school = (JSONObject) schoolObj;
                Map<String, String> sch = new HashMap<>();
                sch.put("id", school.get("id").toString());
                sch.put("country", school.get("country").toString());
                sch.put("city", school.get("city").toString());
                sch.put("name", school.get("name").toString());
                if(school.get("year_from") != null)
                    sch.put("year_from", school.get("year_from").toString());
                if(school.get("year_to") != null)
                    sch.put("year_to", school.get("year_to").toString());
                if(school.get("year_graduated") != null)
                    sch.put("year_graduated", school.get("year_graduated").toString());
                sch.put("class", school.get("class").toString());
                if(school.get("speciality") != null)
                    sch.put("speciality", school.get("speciality").toString());
                if(school.get("type") != null) {
                    sch.put("type", school.get("type").toString());
                    sch.put("type_str", school.get("type_str").toString());
                }
                schools.add(sch);
            }
        }
        String status = (userInfo.get("status") != null) ? userInfo.get("status").toString(): "";
        String nickname = (userInfo.get("nickname") != null) ? userInfo.get("nickname").toString() : "";
        // Connections
        Map<String, String> connections = null;
        if(userInfo.get("skype") != null || userInfo.get("facebook") != null || userInfo.get("twitter") != null || userInfo.get("livejournal") != null || userInfo.get("instagram") != null) {
            connections = new HashMap<>();
            if(userInfo.get("skype") != null)
                connections.put("skype", userInfo.get("skype").toString());
            if(userInfo.get("facebook") != null)
                connections.put("facebook", userInfo.get("facebook").toString());
            if(userInfo.get("twitter") != null)
                connections.put("twitter", userInfo.get("twitter").toString());
            if(userInfo.get("livejournal") != null)
                connections.put("livejournal", userInfo.get("livejournal").toString());
            if(userInfo.get("instagram") != null)
                connections.put("instagram", userInfo.get("instagram").toString());
        }
        Map<String, String> occupation = null;
        if(userInfo.get("occupation") != null){
            occupation = new HashMap<>();
            JSONObject occ = (JSONObject) userInfo.get("occupation");
            occupation.put("type", occ.get("type").toString());
            if(occ.get("id") != null)
                occupation.put("id", occ.get("id").toString());
            occupation.put("name", occ.get("name").toString());
        }

        return new UserDetailed(us, homeTown, photoURL, hasMobile, contacts, site, education, universities, schools, status, null, nickname, connections, occupation);
    }

    /**
     * Get personal info for user from JSON
     * @param userInfo User info in JSON format
     * @return Map with user personal info: key String and value String
     */
    private Map<String, String> getPersonalInfoByData(final JSONObject userInfo){
        Map<String, String> personal = new HashMap<>();
        /*Data is not empty only when we got object, not array*/
        if(userInfo.get("personal") != null && userInfo.get("personal") instanceof JSONObject) {
            JSONObject personalData = (JSONObject) userInfo.get("personal");
            if (personalData.get("political") != null && !personalData.get("political").toString().isEmpty())
                personal.put("political", personalData.get("political").toString());
            if (personalData.get("langs") != null) {
                String langsList = "";
                JSONArray langs = (JSONArray) personalData.get("langs");
                for (Object lang : langs.toArray())
                    langsList += lang + " ";
                personal.put("langs", langsList);
            }
            if (personalData.get("religion") != null)
                personal.put("religion", personalData.get("religion").toString());

            if (personalData.get("inspired_by") != null)
                personal.put("inspired_by", personalData.get("inspired_by").toString());

            if (personalData.get("people_main") != null)
                personal.put("people_main", personalData.get("people_main").toString());

            if (personalData.get("life_main") != null)
                personal.put("life_main", personalData.get("life_main").toString());

            if (personalData.get("smoking") != null)
                personal.put("smoking", personalData.get("smoking").toString());

            if (personalData.get("alcohol") != null)
                personal.put("alcohol", personalData.get("alcohol").toString());
        }

        if(userInfo.get("wall_comments") != null)
            personal.put("wall_comments", userInfo.get("wall_comments").toString());

        if(userInfo.get("can_post") != null)
            personal.put("can_post", userInfo.get("can_post").toString());

        if(userInfo.get("can_see_audio") != null)
            personal.put("can_see_audio", userInfo.get("can_see_audio").toString());

        if(userInfo.get("can_write_private_message") != null)
            personal.put("can_write_private_message", userInfo.get("can_write_private_message").toString());

        if(userInfo.get("music") != null && !userInfo.get("music").toString().isEmpty())
            personal.put("music", userInfo.get("music").toString());

        if(userInfo.get("movies") != null && !userInfo.get("movies").toString().isEmpty())
            personal.put("movies", userInfo.get("movies").toString());

        if(userInfo.get("tv") != null && !userInfo.get("tv").toString().isEmpty())
            personal.put("tv", userInfo.get("tv").toString());

        if(userInfo.get("books") != null && !userInfo.get("books").toString().isEmpty())
            personal.put("books", userInfo.get("books").toString());

        if(userInfo.get("games") != null && !userInfo.get("games").toString().isEmpty())
            personal.put("games", userInfo.get("games").toString());

        return personal;
    }

    /**
     * Create a URL for request to VK Api
     * @param method method to use
     * @param parameters request parameters
     * @return created url
     */
    private String createURL(String method, String parameters){
        return "https://api.vk.com/method/" +
                method +
                "&v=5.21&fields=" + parameters + "&out=0" +
                "&access_token=" + token;
    }

    /**
     * Execute http request with specified <code>url</code>
     * @param url url to send request to
     * @return response string
     * @throws ParseException
     */
    private String executeHttpRequest(String url) throws ParseException{
        BufferedReader reader = null;
        String response = "";

        try{
            URL query = new URL(url);
            reader = new BufferedReader(new InputStreamReader(query.openStream(), "utf-8"));
            response = reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(reader != null) {
                    reader.close();
                }
            }catch(IOException ex){
                Logger.getLogger(VKApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        JSONObject responseObj = (JSONObject) jsonParser.parse(response);
        JSONObject error = null;
        if( (error = (JSONObject) responseObj.get("error")) != null){
            System.out.println();
            // todo
            // Error code may be different, was a case then thread sleeps for infinity
            if( !"6".equals(error.get("error_code").toString())){
                return null;
            }
            try {
                Thread.sleep(200);
                // Executing request until it's successful
                response = executeHttpRequest(url);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
}
