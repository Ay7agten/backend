package com.kelpiegang.tacoresume.ModelLayer;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("references")
public class Reference {

    @Id
    private ObjectId _id;
    @org.mongodb.morphia.annotations.Reference
    private User user;
    private String name;
    private String jobTitle;
    private String email;

    public Reference() {
    }

    public Reference(User user, String name, String jobTitle, String email) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.email = email;
        this.user = user;
    }

    public ObjectId getObjectId() {
        return _id;
    }

    public String get_id() {
        return _id.toString();
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
