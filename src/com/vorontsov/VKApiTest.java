package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.utils.UserExplorer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApiTest {

    //

    public static void main(String[] args) throws IOException{
        VKApi vkApi = VKApi.getInstance();
        /*vkApi.getUserInfo();*/

        UserExplorer usex = UserExplorer.getInstance();
        usex.setDepth(2);
        User us = new User(133349897, "Andrey", "Obuhov", "Nizhny Novgorod");

        final long startTime = System.currentTimeMillis();
        HashMap<Integer, User> users = usex.getUsersFriendsWithDepth(us);
        System.out.println("Counted users: " + users.size());
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) );

        System.out.println("Processed users: " + usex.getProcessedUsers().size());

        BufferedWriter bw = new BufferedWriter(new FileWriter("P://testResults.txt"));
        bw.write(usex.getProcessedUsers().toString());

        /*System.out.println(vkApi.getUsersFriends(friends.get(5).getId()).size());*/
    }

}
