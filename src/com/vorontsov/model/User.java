package com.vorontsov.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 21.01.2016.
 */
public class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String city;
    /*
        1 - female
        2 - male
        0 - not specified
    */
    protected int sex;
    protected String bdate;
    protected int followersCount;
    /*
        1 – single
        2 – in a relationship
        3 – engaged
        4 – married
        5 – it's complicated
        6 – actively searching
        7 – in love
    */
    protected int relation;
    /*
        political:
            0 - not specified
            1 – Communist
            2 – Socialist
            3 – Moderate
            4 – Liberal
            5 – Conservative
            6 – Monarchist
            7 – Ultraconservative
            8 – Apathetic
            9 – Libertian
        langs:      list of languages separated with space
        religion:   world view
        inspired_by
        people_main:
            0 - not specified
            1 – intellect and creativity
            2 – kindness and honesty
            3 – health and beauty
            4 – wealth and power
            5 – courage and persistance
            6 – humor and love for life
        life_main:
            0 - not specified
            1 – family and children
            2 – career and money
            3 – entertainment and leisure
            4 – science and research
            5 – improving the world
            6 – personal development
            7 – beauty and art
            8 – fame and influence
        smoking, alcohol: views on smoking and alcohol
            0 - Not specified
            1 – very negative
            2 – negative
            3 – neutral
            4 – compromisable
            5 – positive
        wall_comments:
            0 - not allowed
            1 - allowed
        can_post: post on the wall
            0 - not allowed
            1 - allowed
        can_see_audio:
            0 - not allowed
            1 - allowed
        can_write_private_message:
            0 - not allowed
            1 - allowed
        music, movies, tv, books, games - favourites
        interests
        activities
    */
    protected Map<String, String> personal;
    protected Map<String, Integer> numberOfContent;

    protected List<Integer> friendsIDs = new ArrayList<>();

    protected User(){}

    public User(int id, String firstName, String lastName, String city, int sex, String bdate, int followersCount, int relation, Map personal){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.sex = sex;
        this.bdate = bdate;
        this.followersCount = followersCount;
        this.relation = relation;
        this.personal = personal;
    }

    //todo refactor
    public User(int id, String firstName, String lastName, String city, int sex, String bdate, int followersCount, int relation, Map personal, ArrayList<Integer> friendsIDs){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.sex = sex;
        this.bdate = bdate;
        this.followersCount = followersCount;
        this.relation = relation;
        this.personal = personal;
        this.friendsIDs = friendsIDs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                "; firstName='" + firstName + '\'' +
                "; lastName='" + lastName + "\'" +
                "; city='" + getCity() + "\'" +
                "; friends processed= " + friendsIDs.size() +
                "; sex=" + getSex() +
                "; bdate=" + getBdate() +
                "; followers=" + getFollowersCount() +
                "; relation=" + getRelation() +
                "; personal=" + getPersonal();
    }

    public List<Integer> getFriendsIDs() {
        return friendsIDs;
    }

    public void addFriend(int id) {
        this.friendsIDs.add(id);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public Map<String, String> getPersonal() {
        return personal;
    }

    public void setPersonal(Map<String, String> personal) {
        this.personal = personal;
    }
}
