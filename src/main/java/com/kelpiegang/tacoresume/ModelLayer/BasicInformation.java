package com.kelpiegang.tacoresume.ModelLayer;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("basicInformations")
public class BasicInformation {

    @Id
    private ObjectId _id;
    @Reference
    private User user;
    private String name;
    private String email;
    private String about;
    private String jobTitle;

    public BasicInformation() {
    }

    public BasicInformation(User user, String name, String email, String about, String jobTitle) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.about = about;
        this.jobTitle = jobTitle;
    }

    public ObjectId getId() {
        return _id;
    }

    public String get_id() {
        return _id.toString();
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

}
