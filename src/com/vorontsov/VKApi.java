package com.vorontsov;

import com.vorontsov.model.User;
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
    private final static String token = "94558733e3838b5cd23cf30881d5f098f81e3913e41ec7697588fa614b96141906955b671a4746c41e6ad";

    private static JSONParser jsonParser = new JSONParser();

    private VKApi(){}

    public static VKApi getInstance() {
        if(instance == null)
            instance = new VKApi();
        return instance;
    }

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
     * @param userInfo user info in JSON format
     * @return User object
     */
    public User getUserByUserInfo(final JSONObject userInfo){
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
     * @param method method to use
     * @param parameters request parameters
     * @return created url
     */
    public String createURL(String method, String parameters){
        return "https://api.vk.com/method/" +
                method +
                "&v=5.21&fields=" + parameters + "&out=0" +
                "&access_token=" + token;
    }

    /**
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
                response = executeHttpRequest(url);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
}
