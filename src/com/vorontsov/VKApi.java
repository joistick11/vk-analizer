package com.vorontsov;

import com.vorontsov.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApi {
    private static VKApi instance = null;
    //https://oauth.vk.com/authorize?client_id=4763444&scope=13&redirect_uri=http://api.vkontakte.ru/blank.html&display=page&v=5.21&response_type=token
    private final static String token = "2d04cc8aedeb4435c707f8e33e3a2ac64e6b4ee03bbdf048b6771e61eb35efcd268fb6373fbcda6573f5f";

    private static JSONParser jsonParser = new JSONParser();

    private VKApi(){}

    public static VKApi getInstance() {
        if(instance == null)
            instance = new VKApi();
        return instance;
    }

    public List<User> getUsersFriends(int id){
        //System.out.println("[vkAPI] get friends for " + id);
        String url = createURL("friends.get?user_id=" + id, "uid,first_name,last_name,city,domain");
        System.out.println("Created getFriendURL: " + url);

        List<User> friends = new LinkedList<>();
        try{
            String JSONresponse = executeHttpRequest(url);
            if(JSONresponse == null)
                return null;
            //System.out.println("[vkAPI] JSON resp: " + JSONresponse);

            JSONObject jsonResp = (JSONObject) jsonParser.parse(JSONresponse);
            JSONObject items = (JSONObject) jsonResp.get("response");
            if(items != null) {
                JSONArray itemsList = (JSONArray) items.get("items");
                for (Object tmp : itemsList) {
                    JSONObject userInfo = (JSONObject) tmp;
                    int userId = Integer.parseInt(userInfo.get("id").toString());
                    String firstName = userInfo.get("first_name").toString();
                    String lastName = userInfo.get("last_name").toString();
                    String city = "Non specified";
                    if (userInfo.get("city") != null){
                        JSONObject cityObject = (JSONObject) userInfo.get("city");
                        city = cityObject.get("title").toString();
                    }
                    friends.add(new User(userId, firstName, lastName, city));
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return friends;
    }

    public void getUserInfo(){
        int userId = 0;
        String name = "";
        String lastname = "";
        String photo = "";

        String url = "https://api.vk.com/method/" +
                "users.get" +
                "?fields=photo_50" + "&out=0" +
                "&access_token=" + token;
        try{
            String userIdJSON = executeHttpRequest(url);

            JSONParser parser = new JSONParser();
            JSONObject jsonResp = (JSONObject) parser.parse(userIdJSON.toString());
            JSONArray postsList = (JSONArray) jsonResp.get("response");
            JSONObject userInfo = null;
            for (int i=0; i < postsList.size(); i++){
                userInfo = (JSONObject) postsList.get(i);
                userId = Integer.parseInt(userInfo.get("uid").toString());
                name = userInfo.get("first_name").toString();
                lastname = userInfo.get("last_name").toString();
                photo = userInfo.get("photo_50").toString();
            }
        }catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("UserId=" + userId + "; name: " + name + "; photo: " + photo);

    }

    public String createURL(String method, String parameters){
        return "https://api.vk.com/method/" +
                method +
                "&v=5.21&fields=" + parameters + "&out=0" +
                "&access_token=" + token;
    }

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
