package com.vorontsov.utils;

import com.vorontsov.VKApi;
import com.vorontsov.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Максим on 24.01.2016.
 */
public class UserExplorer {
    private int depth;
    private String city = "Nizhny Novgorod";
    private HashMap<Integer, User> processedUsers;
    private VKApi vkApi;

    private static UserExplorer instance = new UserExplorer();

    public static UserExplorer getInstance() {
        return instance;
    }

    private UserExplorer() {
        vkApi = VKApi.getInstance();
        processedUsers = new HashMap<>();
    }

    public HashMap<Integer, User> getUsersFriendsWithDepth(User startUser){
        processedUsers.put(startUser.getId(), startUser);
        getUsersFriends(startUser, 1);
        return getProcessedUsers();
    }

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
                processedUsers.get(user.getId()).addFriend(friend.getId());
                if (!getProcessedUsers().containsKey(friend.getId())) {
                    processedUsers.put(friend.getId(), friend);
                    getUsersFriends(friend, currDepth + 1);
                }
            }
        }

    }

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

    public HashMap<Integer, User> getProcessedUsers() {
        return processedUsers;
    }
}
