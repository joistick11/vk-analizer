package com.vorontsov.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Максим on 21.01.2016.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String city;
    private List<Integer> friendsIDs = new ArrayList<>();

    public User(int id, String firstName, String lastName, String city){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
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
        return "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + "\'" +
                ", city='" + getCity() + "\'\n" +
                ", number of friends: " + friendsIDs.size();
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
}
