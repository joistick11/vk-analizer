package com.vorontsov.utils;

import com.vorontsov.VKApi;
import com.vorontsov.model.User;
import com.vorontsov.model.UserDetailed;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserDetailizer {
    private VKApi vkApi;

    public UserDetailizer(){
        vkApi = VKApi.getInstance();
    }

    public HashMap<Integer, User> getDetailedUsers(HashMap<Integer, User> users){
        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            User us = (User) pair.getValue();
            // Replace user with detailed version
            users.put((Integer) pair.getKey(), getDetailedUser(us));
        }

        return users;
    }

    public UserDetailed getDetailedUser(int id){
        return getDetailedUser(vkApi.getUserById(id));
    }

    public UserDetailed getDetailedUser(User user){
        return null;
    }

}
