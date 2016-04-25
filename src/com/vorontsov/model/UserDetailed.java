package com.vorontsov.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 20.04.2016.
 */
public class UserDetailed extends User {
    String homeTown;
    String photo;
    boolean hasMobile;
    Map<String, String> contacts;
    String site;
    Map<String, String> education;
    List<Map<String, String>> universities;
    List<Map<String, String>> schools;
    String status;
    Map<String, Integer> counters;
    String nickname;
    Map<String, String> connections;
    Map<String, String> occupation;

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

    /**
     * Does user have mobile on the page
     */
    public boolean isHasMobile() {
        return hasMobile;
    }

    public void setHasMobile(boolean hasMobile) {
        this.hasMobile = hasMobile;
    }

    /**
     * mobile_phone, home_phone
     */
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

    /**
     * university, university_name, faculty, faculty_name, graduation
     */
    public Map<String, String> getEducation() {
        return education;
    }

    public void setEducation(Map<String, String> education) {
        this.education = education;
    }

    /**
     * University -> id, country, city, name, faculty, faculty_name, chair, chair_name, graduation
     */
    public List<Map<String, String>> getUniversities() {
        return universities;
    }

    public void setUniversities(List<Map<String, String>> universities) {
        this.universities = universities;
    }

    /**
     * School -> id, country, city, name, year_from, year_to, year_graduated, class, speciality, type, type_str
     */
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

    /**
     * Number of albums, videos, audios, notes, friends, groups, online_friends, user_videos
     */
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

    /**
     * name of service -> login name (ex. twitter -> maxvorontsov)
     */
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
}
