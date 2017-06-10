package com.kelpiegang.tacoresume.ModelLayer;

import java.util.ArrayList;
import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("workExperiences")
public class WorkExperience {

    @Id
    private ObjectId _id;
    @Reference
    private User user;
    private String title;
    private String company;
    private Date startDate;
    private Date endDate;
    private ArrayList<String> duties;
    private ArrayList<String> tools;

    public WorkExperience() {
    }

    public WorkExperience(User user, String title, String company, Date startDate, Date endDate, ArrayList<String> duties, ArrayList<String> tools) {
        this.user = user;
        this.title = title;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duties = duties;
        this.tools = tools;
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

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Long getStartDate() {
        if (startDate != null) {
            return startDate.getTime();
        } else {
            return null;
        }
    }

    public Long getEndDate() {
        if (endDate != null) {
            return endDate.getTime();
        } else {
            return null;
        }
    }

    public ArrayList<String> getDuties() {
        return duties;
    }

    public ArrayList<String> getTools() {
        return tools;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDuties(ArrayList<String> duties) {
        this.duties = duties;
    }

    public void setTools(ArrayList<String> tools) {
        this.tools = tools;
    }
}
