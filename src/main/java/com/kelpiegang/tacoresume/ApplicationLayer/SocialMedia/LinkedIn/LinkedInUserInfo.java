package com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.LinkedIn;

public class LinkedInUserInfo {

    private String firstName;
    private String lastName;
    private String id;
    private String headline;

    public LinkedInUserInfo(String firstName, String lastName, String id, String headline) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.headline = headline;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getHeadline() {
        return headline;
    }

}
