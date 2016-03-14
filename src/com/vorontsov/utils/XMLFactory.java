package com.vorontsov.utils;

import com.vorontsov.model.User;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Максим on 13.03.2016.
 */
public class XMLFactory {
    private static XMLFactory ourInstance = new XMLFactory();

    public static XMLFactory getInstance() {
        return ourInstance;
    }

    private XMLFactory() {}

    public static void writeUsersToXML(List<User> users, String filename){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("users");
        for (User us: users){
            Element user = root.addElement("user")
                    .addAttribute("id", us.getId() + "");
            /*General info*/
            user.addElement("firstName").addText(us.getFirstName());
            user.addElement("lastName").addText(us.getLastName());
            user.addElement("city").addText(us.getCity());
            user.addElement("sex").addText(us.getSex() + "");
            user.addElement("bdate").addText(us.getBdate());
            user.addElement("followers").addText(us.getFollowersCount() + "");
            user.addElement("relation").addText(us.getRelation() + "");

            /*Personal Info*/
            Element personal = user.addElement("personal");
            for(Iterator it = us.getPersonal().entrySet().iterator(); it.hasNext();){
                Map.Entry personalPair = (Map.Entry) it.next();
                personal.addElement(personalPair.getKey().toString())
                        .addText(personalPair.getValue().toString());
            }

            /*User's friends*/
            Element friends = user.addElement("friends");
            String friendsIDs = "";
            for (int id: us.getFriendsIDs()) {
                friendsIDs += id + ",";
            }
            friends.setText(friendsIDs);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            File fileToWrite = new File(filename);
            if(!fileToWrite.exists())
                fileToWrite.createNewFile();
            XMLWriter writer = new XMLWriter(new FileOutputStream(fileToWrite), format);
            writer.write(document);
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static HashMap<Integer, User> readUsersFromXML(String filename) throws FileNotFoundException{
        HashMap<Integer, User> usersToReturn = new HashMap<>();
        File fileToRead = new File(filename);
        if(!fileToRead.exists())
            throw new FileNotFoundException("Specified file cannot be found");

        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(fileToRead);
        }catch (DocumentException dex){
            dex.printStackTrace();
        }

        Element usersElement = document.getRootElement();
        for(Iterator i = usersElement.elementIterator(); i.hasNext();) {
            Element userElement = (Element) i.next();

            /*General info*/
            int id = Integer.parseInt(userElement.attribute("id").getValue());
            String firstName = userElement.element("firstName").getStringValue();
            String lastName = userElement.element("lastName").getStringValue();
            String city = userElement.element("city").getStringValue();
            int sex = Integer.parseInt(userElement.element("sex").getStringValue());
            String bdate = userElement.element("bdate").getStringValue();
            int followersCount = Integer.parseInt(userElement.element("followers").getStringValue());
            int relation = Integer.parseInt(userElement.element("relation").getStringValue());

            /*Personal info*/
            Map<String, String> personal = new HashMap<>();
            Element personalElement = userElement.element("personal");
            for(Iterator j = personalElement.elementIterator(); j.hasNext();){
                Element currPersonal = (Element) j.next();
                personal.put(currPersonal.getName(), currPersonal.getStringValue());
            }

            /*User's friends*/
            ArrayList<Integer> friendsIDs = new ArrayList<>();
            String[] friendsArr = null;
            String friendsString = userElement.element("friends").getStringValue();
            if(friendsString != null && !friendsString.isEmpty()) {
                friendsArr = friendsString.split(",");
                for(String frID : friendsArr) {
                   friendsIDs.add(Integer.parseInt(frID));
                }
            }

            usersToReturn.put(id, new User(id, firstName, lastName, city, sex, bdate, followersCount, relation, personal, friendsIDs));
        }

        return usersToReturn;
    }
}
