package com.kelpiegang.tacoresume.ModelLayer;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("skills")
public class Skill {

    @Id
    private ObjectId _id;
    @Reference
    private SkillCategory skillCategory;
    private String title;
    private int percentage;

    public Skill() {
    }

    public Skill(SkillCategory skillCategory, String title, int percentage) {
        this.title = title;
        this.percentage = percentage;
        this.skillCategory = skillCategory;
    }

    public ObjectId getObjectId() {
        return _id;
    }

    public String get_id() {
        return _id.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public SkillCategory getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(SkillCategory skillCategory) {
        this.skillCategory = skillCategory;
    }

}
