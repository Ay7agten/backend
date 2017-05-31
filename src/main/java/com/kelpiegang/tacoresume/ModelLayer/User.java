package com.kelpiegang.tacoresume.ModelLayer;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("users")
public class User {

    @Id
    private ObjectId _id;
    private String facebookId;
    private String googleId;
    private String linkedInId;
    private String name;
    private String email;
    private String about;
    private String jobTitle;
    private String phoneNumber;
    private String website;

    public User() {
    }

    public User(String socialMediaId, String socialMedia) {
        if (socialMedia.equals("facebook")) {
            this.facebookId = socialMediaId;
        } else if (socialMedia.equals("google")) {
            this.googleId = socialMediaId;
        } else if (socialMedia.equals("linkedIn")) {
            this.linkedInId = socialMediaId;
        }
    }

    public ObjectId getObjectId() {
        return _id;
    }

    public String get_id() {
        return _id.toString();
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getLinkedInId() {
        return linkedInId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
