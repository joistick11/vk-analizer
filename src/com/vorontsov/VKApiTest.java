package com.vorontsov;

import com.vorontsov.model.User;
import com.vorontsov.model.UserDetailed;
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
        final long startTime = System.currentTimeMillis();

        UserExplorer usex = UserExplorer.getInstance();
        usex.setDepth(0);
        HashMap users = usex.getUsersFriendsWithDepth(87582268);
        System.out.println("\nProcessed users: " + usex.getCollectedUsers().size());

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime)/1000 + "s" );

        UserDetailizer userDetailizer = new UserDetailizer();
        HashMap userDetailed = userDetailizer.getDetailedUsers(users);
        XMLFactory.writeUsersToXML(new LinkedList<>(userDetailed.values()), "P://vk-analizer//lol.xml", true);

        HashMap usersRead = XMLFactory.readDetailedUsersFromXML("P://vk-analizer//lol.xml");
        System.out.println(userDetailed);
        System.out.println(usersRead);

        //XMLFactory.writeUsersToXML(new LinkedList<>(users.values()), "P://vk-analizer//pizdec.xml");


    }
}
