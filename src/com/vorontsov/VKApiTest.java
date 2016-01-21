package com.vorontsov;

import com.vorontsov.model.User;

import java.util.List;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApiTest {

    public static void main(String[] args){
        VKApi vkApi = VKApi.getInstance();
        vkApi.getUserInfo();

        List<User> friends = vkApi.getUsersFriends(133349897);
        System.out.println(friends.get(5));
        System.out.println(vkApi.getUsersFriends(friends.get(5).getId()).size());
    }

}
