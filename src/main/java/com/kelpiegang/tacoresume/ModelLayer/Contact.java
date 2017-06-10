package com.kelpiegang.tacoresume.ModelLayer;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("contacts")
public class Contact {

    @Id
    private ObjectId _id;
    @Reference
    private User user;
    private String phoneNumber;
    private String email;
    private String website;

    public Contact() {
    }

    public Contact(User user, String email, String phoneNumber, String website) {
        this.user = user;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
