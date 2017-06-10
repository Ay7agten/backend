package com.kelpiegang.tacoresume.ModelLayer;

import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Reference;

@Entity("skillCategories")
public class SkillCategory {

    @Id
    private ObjectId _id;
    private String title;
    @Reference
    private ProfessionalSkillsSection professionalSkillsSection;
    @Reference
    private DevelopmentToolsSection developmentToolsSection;
    @NotSaved
    private ArrayList<Skill> skills;

    public SkillCategory() {

    }

    public SkillCategory(String title, ProfessionalSkillsSection professionalSkillsSection) {
        this.title = title;
        this.professionalSkillsSection = professionalSkillsSection;
    }

    public SkillCategory(String title, DevelopmentToolsSection developmentToolsSection) {
        this.title = title;
        this.developmentToolsSection = developmentToolsSection;
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

    public ProfessionalSkillsSection getProfessionalSkillsSection() {
        return professionalSkillsSection;
    }

    public void setProfessionalSkillsSection(ProfessionalSkillsSection professionalSkillsSection) {
        this.professionalSkillsSection = professionalSkillsSection;
    }

    public DevelopmentToolsSection getdevelopmentToolsSection() {
        return developmentToolsSection;
    }

    public void setdevelopmentToolsSection(DevelopmentToolsSection developmentToolsSection) {
        this.developmentToolsSection = developmentToolsSection;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public DevelopmentToolsSection getDevelopmentToolsSection() {
        return developmentToolsSection;
    }

    public void setDevelopmentToolsSection(DevelopmentToolsSection developmentToolsSection) {
        this.developmentToolsSection = developmentToolsSection;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }
}
