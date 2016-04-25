package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.utils.UserDetailizer;
import com.vorontsov.utils.UserExplorer;
import com.vorontsov.utils.XMLFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Максим on 21.01.2016.
 */
public class VKApiTest {
    public static void main(String[] args) throws IOException{
        //HashMap<Integer, User> usrs = XMLFactory.readUsersFromXML("P://vk-analizer//outputUsers1.xml");
        //System.out.println("Read users: " + usrs.size());

        /*UserExplorer usex = UserExplorer.getInstance();
        //usex.setCollectedUsers(usrs);
        usex.setDepth(1);
        //usex.setSkipToDepth(3);

        final long startTime = System.currentTimeMillis();
        HashMap<Integer, User> users = usex.getUsersFriendsWithDepth(133349897);
        System.out.println("\nProcessed users: " + usex.getCollectedUsers().size());
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime)/1000 + "s" );

        XMLFactory.writeUsersToXML(new LinkedList<>(users.values()), "P://vk-analizer//outputUsers2.xml");
        System.out.println("Totally collected: " + users.size());
        */

        UserDetailizer userDetailizer = new UserDetailizer();
        VKApi.getInstance().getDetailedUser(87582268);


        //XMLFactory.writeUsersToXML(new LinkedList<>(usrs.values()), "P://vk-analizer//outputUsers2.xml");

        /*BufferedWriter bw = new BufferedWriter(new FileWriter("P://testResults.txt"));
        bw.write(usex.getProcessedUsersAsString());
        bw.close();*/

        /*System.out.println(vkApi.getUsersFriends(friends.get(5).getId()).size());*/
    }

}
