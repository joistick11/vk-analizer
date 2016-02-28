package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.utils.UserExplorer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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

        final long startTime = System.currentTimeMillis();
        HashMap<Integer, User> users = usex.getUsersFriendsWithDepth(87582268);
        System.out.println("\nProcessed users: " + usex.getProcessedUsers().size());
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) );

        BufferedWriter bw = new BufferedWriter(new FileWriter("P://testResults.txt"));
        bw.write(usex.getProcessedUsersAsString());
        bw.close();

        /*System.out.println(vkApi.getUsersFriends(friends.get(5).getId()).size());*/
    }

}
