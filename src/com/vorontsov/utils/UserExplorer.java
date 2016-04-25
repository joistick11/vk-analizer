package com.vorontsov.utils;

import com.vorontsov.VKApi;
import com.vorontsov.model.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 24.01.2016.
 */
public class UserExplorer {
    private int depth;
    private String city = "Nizhny Novgorod";
    private HashMap<Integer, User> collectedUsers;
    private VKApi vkApi;
    private int skipToDepth;

    // todo remove singleton
    private static UserExplorer instance = new UserExplorer();

    public static UserExplorer getInstance() {
        return instance;
    }

    private UserExplorer() {
        vkApi = VKApi.getInstance();
        collectedUsers = new HashMap<>();
        skipToDepth = 1;
    }

    /**
     * @param startUserId userId to start with
     * @return Map of users processed with specified depth
     */
    public HashMap<Integer, User> getUsersFriendsWithDepth(int startUserId){
        return getUsersFriendsWithDepth(vkApi.getUserById(startUserId));
    }

    /**
     * @param startUser user to start with
     * @return Map of users processed with specified depth
     */
    public HashMap<Integer, User> getUsersFriendsWithDepth(User startUser){
        collectedUsers.put(startUser.getId(), startUser);
        getUsersFriends(startUser, 1);
        return getCollectedUsers();
    }

    /**
     * @param user user to get friends for
     * @param currDepth current operation depth
     */
    private void getUsersFriends(final User user, int currDepth){
        if(currDepth > depth)
            return;

        System.out.println("Let's get friends for " + user.getId() + " with depth=" + currDepth);
        List<User> friends = vkApi.getUsersFriends(user.getId());
        if(friends != null) {
            for (User friend : friends) {
                // todo
                /*If more than 40% user friends from necessary city, add him to the list*/
                if(friend.getCity() == null || !city.equals(friend.getCity())) {
                    /*List<User> doubtfulFriends = vkApi.getUsersFriends(friend.getId());
                    if(doubtfulFriends == null || doubtfulFriends.size() == 0 || getFromCityPercentage(doubtfulFriends, city) < 0.40 )*/
                    continue;
                }
                if (!collectedUsers.containsKey(friend.getId()) || depth <= skipToDepth) {
                    collectedUsers.get(user.getId()).addFriend(friend.getId());
                    collectedUsers.put(friend.getId(), friend);
                    getUsersFriends(friend, currDepth + 1);
                }
            }
        }

    }

    /**
     * @param users users to analyze
     * @param city a city to count users in
     * @return percentage of people with specified city
     */
    public float getFromCityPercentage(List<User> users, String city){
        if(users == null || users.size() == 0)
            return 0;

        int numberFromCity = 0;
        for (User user: users) {
            if(city.equals(user.getCity()))
                numberFromCity++;
        }

        return numberFromCity / users.size();
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public int getDepth(){
        return depth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public HashMap<Integer, User> getCollectedUsers() {
        return collectedUsers;
    }

    public String getCollectedUsersAsString(){
        String puString = "";
        Iterator it = getCollectedUsers().entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            User us = (User) pair.getValue();
            puString += us.toString() + "\n";
        }

        return puString;
    }

    public void setSkipToDepth(int skipToDepth){
        this.skipToDepth = skipToDepth;
    }

    public void setCollectedUsers(HashMap<Integer, User> collectedUsers) {
        this.collectedUsers = collectedUsers;
    }
}
