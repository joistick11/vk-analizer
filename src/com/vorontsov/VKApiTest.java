package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.utils.UserExplorer;
import com.vorontsov.utils.XMLFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApiTest {

    //

    public static void main(String[] args) throws IOException{
        VKApi vkApi = VKApi.getInstance();
        /*vkApi.getUserInfo();*/

        /*UserExplorer usex = UserExplorer.getInstance();
        usex.setDepth(2);

        final long startTime = System.currentTimeMillis();
        HashMap<Integer, User> users = usex.getUsersFriendsWithDepth(133349897);
        System.out.println("\nProcessed users: " + usex.getProcessedUsers().size());
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) );*/

        //XMLFactory.writeUsersToXML(new LinkedList<>(users.values()), "P://vk-analizer//outputUsers.xml");
        HashMap<Integer, User> usrs = XMLFactory.readUsersFromXML("P://vk-analizer//outputUsers.xml");
        System.out.println("Read users: " + usrs.size());

        XMLFactory.writeUsersToXML(new LinkedList<>(usrs.values()), "P://vk-analizer//outputUsers2.xml");

        /*BufferedWriter bw = new BufferedWriter(new FileWriter("P://testResults.txt"));
        bw.write(usex.getProcessedUsersAsString());
        bw.close();*/

        /*System.out.println(vkApi.getUsersFriends(friends.get(5).getId()).size());*/
    }

}
