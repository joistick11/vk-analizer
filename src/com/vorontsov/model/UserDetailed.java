package com.vorontsov.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 20.04.2016.
 */
public class UserDetailed extends User {
    String homeTown;
    String photo;
    /**
     * Does user have mobile on the page
     */
    int hasMobile;
    /**
     * mobile_phone, home_phone
     */
    Map<String, String> contacts;
    String site;
    /**
     * university, university_name, faculty, faculty_name, graduation
     */
    Map<String, String> education;
    /**
     * University -> id, country, city, name, faculty, faculty_name, chair, chair_name, graduation
     */
    List<Map<String, String>> universities;
    /**
     * School -> id, country, city, name, year_from, year_to, year_graduated, class, speciality, type, type_str
     */
    List<Map<String, String>> schools;
    String status;
    /**
     * Number of albums, videos, audios, notes, friends, groups, online_friends, user_videos
     * todo add collection of this parameter
     */
    Map<String, Integer> counters;
    String nickname;
    /**
     * name of service -> login name (ex. twitter -> maxvorontsov)
     */
    Map<String, String> connections;
    /**
     * current user occupation (school, university or work)
     * type -> "type of occupation",
     * id -> "id of organization",
     * name -> "name of organization"
     */
    Map<String, String> occupation;

    public UserDetailed(User user){
        super(user);
    }

    public UserDetailed(User user, String homeTown, String photo, int hasMobile, Map contacts, String site, Map education, List universities, List schools, String status, Map counters, String nickname, Map connections, Map occupation){
        super(user);
        this.homeTown = homeTown;
        this.photo = photo;
        this.hasMobile = hasMobile;
        this.contacts = contacts;
        this.site = site;
        this.education = education;
        this.universities = universities;
        this.schools = schools;
        this.status = status;
        this.counters = counters;
        this.nickname = nickname;
        this.connections = connections;
        this.occupation = occupation;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int isHasMobile() {
        return hasMobile;
    }

    public void setHasMobile(int hasMobile) {
        this.hasMobile = hasMobile;
    }
    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Map<String, String> getEducation() {
        return education;
    }

    public void setEducation(Map<String, String> education) {
        this.education = education;
    }

    public List<Map<String, String>> getUniversities() {
        return universities;
    }

    public void setUniversities(List<Map<String, String>> universities) {
        this.universities = universities;
    }

    public List<Map<String, String>> getSchools() {
        return schools;
    }

    public void setSchools(List<Map<String, String>> schools) {
        this.schools = schools;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Integer> getCounters() {
        return counters;
    }

    public void setCounters(Map<String, Integer> counters) {
        this.counters = counters;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, String> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, String> connections) {
        this.connections = connections;
    }

    public Map<String, String> getOccupation() {
        return occupation;
    }

    public void setOccupation(Map<String, String> occupation) {
        this.occupation = occupation;
    }

    @Override
    public String toString(){
        return "ID=" + id +
                "; firstName='" + firstName + '\'' +
                "; lastName='" + lastName + "\'" +
                "; city='" + getCity() + "\'" +
                "; friends processed= " + friendsIDs.size() +
                "; sex=" + getSex() +
                "; bdate=" + getBdate() +
                "; followers=" + getFollowersCount() +
                "; relation=" + getRelation() +
                "; personal=" + getPersonal() +
                "; homeTown=" + homeTown +
                "; photo=" + photo +
                "; hasMobile=" + hasMobile +
                "; contacts=" + contacts +
                "; site=" + site +
                "; education=" + education +
                "; universities="+universities +
                "; schools=" + schools +
                "; status=" + status +
                "; counters=" + counters +
                "; nickname=" + nickname +
                "; connections=" + connections +
                "; occupation=" + occupation;
    }
}
