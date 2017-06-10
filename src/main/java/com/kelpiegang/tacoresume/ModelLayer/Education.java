package com.kelpiegang.tacoresume.ModelLayer;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("educations")
public class Education {

    @Id
    private ObjectId _id;
    @Reference
    private User user;
    private String degree;
    private String university;
    private Date startDate;
    private Date endDate;

    public Education() {
    }

    public Education(User user, String degree, String university, Date startDate, Date endDate) {
        this.user = user;
        this.degree = degree;
        this.university = university;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getDegree() {
        return degree;
    }

    public String getUniversity() {
        return university;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
