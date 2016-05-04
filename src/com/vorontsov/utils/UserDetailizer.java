package com.vorontsov.utils;

import com.vorontsov.VKApi;
import com.vorontsov.model.User;
import com.vorontsov.model.UserDetailed;

import java.util.HashMap;

public class UserDetailizer {
    private VKApi vkApi;

    public UserDetailizer(){
        vkApi = VKApi.getInstance();
    }

    /**
     * Detail users, returns map with <code>UserDetailed</code> objects - extended version of User
     * @param users common users collected with <code>UserExplorer#getUsersFriendsWithDepth()</code>
     * @return HashMap with detailed users
     */
    public HashMap<Integer, UserDetailed> getDetailedUsers(HashMap<Integer, User> users){
        return vkApi.getDetailedUsers(users);
    }

    /**
     * Detail one user, returns UserDetailed object
     * @param id id of the user to detail
     * @return UserDetailed
     */
    public UserDetailed getDetailedUser(int id){
        return getDetailedUser(vkApi.getUserById(id));
    }

    /**
     * Detail one user, returns UserDetailed object
     * @param user User object to detail
     * @return UserDetailed
     * todo add implementation
     */
    public UserDetailed getDetailedUser(User user){
        return null;
    }

}
