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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApi {
    private static VKApi instance = null;
    private final static String token = "6b7599ca2a269cb30af180720f8b580e1f46ef7a8c1017ec0fd1a6e34e4cc02b8ec92b3b5c407ee354083";

    private VKApi(){}

    public static VKApi getInstance() {
        if(instance == null)
            instance = new VKApi();
        return instance;
    }

    public List<User> getUsersFriends(int id){
        String url = createURL("friends.get?user_id=" + id, "uid,first_name,last_name");
        System.out.println("Created getFriendURL: " + url);

        String JSONresponse = executeHttpRequest(url);

        JSONParser parser = new JSONParser();
        List<User> friends = new LinkedList<>();
        try{
            JSONObject jsonResp = (JSONObject) parser.parse(JSONresponse);
            JSONArray postsList = (JSONArray) jsonResp.get("response");
            for(Object tmp : postsList){
                JSONObject userInfo = (JSONObject) tmp;
                int userId = Integer.parseInt(userInfo.get("uid").toString());
                String firstName = userInfo.get("first_name").toString();
                String lastName = userInfo.get("last_name").toString();
                friends.add(new User(userId, firstName, lastName));
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

        String userIdJSON = executeHttpRequest(url);

        JSONParser parser = new JSONParser();
        try{
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
        String url = "https://api.vk.com/method/" +
                method +
                "&fields=" + parameters + "&out=0" +
                "&access_token=" + token;
        return url;
    }

    private String executeHttpRequest(String url){
        BufferedReader reader = null;
        String response = "";

        try{
            URL query = new URL(url);
            reader = new BufferedReader(new InputStreamReader(query.openStream(), "utf-8"));
            response = reader.readLine();
        }catch (MalformedURLException e){
            e.printStackTrace();
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

        return response;

    }
}
