package com.vorontsov.utils;

import com.vorontsov.model.User;
import com.vorontsov.model.UserDetailed;
import org.dom4j.*;
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
        writeUsersToXML(users, filename, false);
    }

    public static void writeUsersToXML(List<User> users, String filename, boolean isDetailed){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("users");
        if(!isDetailed) {
            for (User us : users) {
                root.add(createXMLForUser(us));
            }
        }else {
            List<UserDetailed> usersDetailed = (List<UserDetailed>) (List<?>) users;
            for (UserDetailed us : usersDetailed){
                Element userElement = createXMLForUser(us);
                detailUserXML(userElement, us);

                root.add(userElement);
            }
        }

        writeToFile(document, filename);
    }

    private static Element createXMLForUser(User us){
        Element user = DocumentHelper.createElement("user")
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

        return user;
    }

    private static void detailUserXML(Element userXML, UserDetailed user){
        userXML.addElement("home_town").addText(user.getHomeTown());
        userXML.addElement("photo").addText(user.getPhoto());
        userXML.addElement("has_mobile").addText(user.isHasMobile() + "");

        /*Contacts*/
        Element contacts = userXML.addElement("contacts");
        for(Iterator it = user.getContacts().entrySet().iterator(); it.hasNext();){
            Map.Entry contactsPair = (Map.Entry) it.next();
            contacts.addElement(contactsPair.getKey().toString())
                    .addText(contactsPair.getValue().toString());
        }

        userXML.addElement("site").addText(user.getSite());

        /*Education*/
        Element education = userXML.addElement("education");
        for(Iterator it = user.getEducation().entrySet().iterator(); it.hasNext();){
            Map.Entry educationPair = (Map.Entry) it.next();
            education.addElement(educationPair.getKey().toString())
                    .addText(educationPair.getValue().toString());
        }

        /*Universities*/
        Element universities = userXML.addElement("universities");
        for(Map<String, String> univ : user.getUniversities()) {
            Element univElement = universities.addElement("university");
            for (Iterator it = univ.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry univPair = (Map.Entry) it.next();
                univElement.addElement(univPair.getKey().toString())
                        .addText(univPair.getValue().toString());
            }
        }

        /*Schools*/
        Element schools = userXML.addElement("schools");
        for(Map<String, String> sch : user.getSchools()) {
            Element schElement = schools.addElement("school");
            for (Iterator it = sch.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry schPair = (Map.Entry) it.next();
                schElement.addElement(schPair.getKey().toString())
                        .addText(schPair.getValue().toString());
            }
        }

        userXML.addElement("status").addText(user.getStatus());

        /*Counters*/
        Element counters = userXML.addElement("counters");
        if(user.getCounters() != null) {
            for (Iterator it = user.getCounters().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry counterPair = (Map.Entry) it.next();
                counters.addElement(counterPair.getKey().toString())
                        .addText(counterPair.getValue().toString());
            }
        }

        userXML.addElement("nickname").addText(user.getNickname());

        /*Connections*/
        Element connections = userXML.addElement("connections");
        for(Iterator it = user.getConnections().entrySet().iterator(); it.hasNext();){
            Map.Entry connectionPair = (Map.Entry) it.next();
            connections.addElement(connectionPair.getKey().toString())
                    .addText(connectionPair.getValue().toString());
        }

        /*Connections*/
        Element occupation = userXML.addElement("occupation");
        for(Iterator it = user.getOccupation().entrySet().iterator(); it.hasNext();){
            Map.Entry occupationPair = (Map.Entry) it.next();
            occupation.addElement(occupationPair.getKey().toString())
                    .addText(occupationPair.getValue().toString());
        }
    }

    public static HashMap<Integer, User> readUsersFromXML(String filename) throws FileNotFoundException{
        HashMap<Integer, User> usersToReturn = new HashMap<>();
        Element usersElement = readElementFromFile(filename).getRootElement();
        for(Iterator i = usersElement.elementIterator(); i.hasNext();) {
            Element userElement = (Element) i.next();
            User user = parseUserFromXML(userElement);
            usersToReturn.put(user.getId(), user);
        }

        return usersToReturn;
    }

    public static HashMap<Integer, UserDetailed> readDetailedUsersFromXML(String filename) throws FileNotFoundException{
        HashMap<Integer, UserDetailed> usersToReturn = new HashMap<>();
        Element usersElement = readElementFromFile(filename).getRootElement();
        for(Iterator i = usersElement.elementIterator(); i.hasNext();) {
            Element userElement = (Element) i.next();
            UserDetailed user = parseUserDetailedFromXML(userElement);
            usersToReturn.put(user.getId(), user);
        }

        return usersToReturn;
    }

    private static User parseUserFromXML(Element userXML){
        /*General info*/
        int id = Integer.parseInt(userXML.attribute("id").getValue());
        String firstName = userXML.element("firstName").getStringValue();
        String lastName = userXML.element("lastName").getStringValue();
        String city = userXML.element("city").getStringValue();
        int sex = Integer.parseInt(userXML.element("sex").getStringValue());
        String bdate = userXML.element("bdate").getStringValue();
        int followersCount = Integer.parseInt(userXML.element("followers").getStringValue());
        int relation = Integer.parseInt(userXML.element("relation").getStringValue());

            /*Personal info*/
        Map<String, String> personal = new HashMap<>();
        Element personalElement = userXML.element("personal");
        for(Iterator j = personalElement.elementIterator(); j.hasNext();){
            Element currPersonal = (Element) j.next();
            personal.put(currPersonal.getName(), currPersonal.getStringValue());
        }

            /*User's friends*/
        ArrayList<Integer> friendsIDs = new ArrayList<>();
        String[] friendsArr = null;
        String friendsString = userXML.element("friends").getStringValue();
        if(friendsString != null && !friendsString.isEmpty()) {
            friendsArr = friendsString.split(",");
            for(String frID : friendsArr) {
                friendsIDs.add(Integer.parseInt(frID));
            }
        }

        return new User(id, firstName, lastName, city, sex, bdate, followersCount, relation, personal, friendsIDs);
    }

    private static UserDetailed parseUserDetailedFromXML(Element userXML){
        User user = parseUserFromXML(userXML);

        String homeTown = userXML.element("home_town").getStringValue();
        String photo = userXML.element("photo").getStringValue();
        int hasMobile = Integer.parseInt(userXML.element("has_mobile").getStringValue());
        Map<String, String> contacts = parseMapFromXML("contacts", userXML);
        String site = userXML.element("site").getStringValue();
        Map<String, String> education = parseMapFromXML("education", userXML);
        List<Map<String, String>> universities = parseListOfMapsFromXML("universities", userXML);
        List<Map<String, String>> schools = parseListOfMapsFromXML("schools", userXML);
        String status = userXML.element("status").getStringValue();
        Map<String, Integer> counters = null;
        String nickname = userXML.element("nickname").getStringValue();
        Map<String, String> connections = parseMapFromXML("connections", userXML);
        Map<String, String> occupation = parseMapFromXML("occupation", userXML);

        return new UserDetailed(user, homeTown, photo, hasMobile, contacts, site, education, universities
        , schools, status, counters, nickname, connections, occupation);
    }

    private static Map<String, String> parseMapFromXML(String nodeName, Element userXML){
        Map<String, String> mapToReturn = new HashMap<>();
        Element nodeElements = userXML.element(nodeName);
        for(Iterator j = nodeElements.elementIterator(); j.hasNext();){
            Element currNodeElement = (Element) j.next();
            mapToReturn.put(currNodeElement.getName(), currNodeElement.getStringValue());
        }

        return mapToReturn;
    }

    private static List<Map<String, String>> parseListOfMapsFromXML(String nodeName, Element userXML){
        List<Map<String, String>> listToReturn = new LinkedList<>();
        Element topElements = userXML.element(nodeName);
        for(Iterator j = topElements.elementIterator(); j.hasNext();){
            Map<String, String> currElMap = new HashMap<>();
            Element currChildElement = (Element) j.next();
            for(Iterator k = currChildElement.elementIterator(); k.hasNext();){
                Element currNodeElement = (Element) k.next();
                currElMap.put(currNodeElement.getName(), currNodeElement.getStringValue());
            }
            listToReturn.add(currElMap);
        }

        return listToReturn;
    }

    private static void writeToFile(Document document, String filename){
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

    private static Document readElementFromFile(String filename) throws FileNotFoundException{
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
        return document;
    }
}
