package com.kelpiegang.tacoresume.ModelLayer;

import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Reference;

@Entity("developmentToolsSections")
public class DevelopmentToolsSection {

    @Id
    private ObjectId _id;
    @Reference
    private User user;
    @NotSaved
    private ArrayList<SkillCategory> skillCategories;

    public DevelopmentToolsSection() {
    }

    public DevelopmentToolsSection(User user) {
        this.user = user;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
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

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<SkillCategory> getSkillCategories() {
        return skillCategories;
    }

    public void setSkillCategories(ArrayList<SkillCategory> skillCategories) {
        this.skillCategories = skillCategories;
    }

}
