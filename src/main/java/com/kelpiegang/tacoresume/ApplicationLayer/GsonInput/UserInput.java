package com.kelpiegang.tacoresume.ApplicationLayer.GsonInput;

import com.kelpiegang.tacoresume.ModelLayer.*;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class UserInput {

    private ObjectId _id;
    private String facebookId;
    private String googleId;
    private String linkedInId;
    private String name;
    private String email;
    private String about;
    private String jobTitle;
    private ArrayList<WorkExperience> workExperiences;
    private ArrayList<Education> educations;
    private ArrayList<Award> awards;
    private ArrayList<Reference> references;
    private Contact contact;
    private ProfessionalSkillsSection professionalSkills;
    private DevelopmentToolsSection developmentTools;


    public ArrayList<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(ArrayList<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getLinkedInId() {
        return linkedInId;
    }

    public void setLinkedInId(String linkedInId) {
        this.linkedInId = linkedInId;
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

    public ArrayList<Education> getEducations() {
        return educations;
    }

    public void setEducations(ArrayList<Education> educations) {
        this.educations = educations;
    }

    public ArrayList<Award> getAwards() {
        return awards;
    }

    public void setAwards(ArrayList<Award> awards) {
        this.awards = awards;
    }

    public ArrayList<Reference> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<Reference> references) {
        this.references = references;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ProfessionalSkillsSection getProfessionalSkills() {
        return professionalSkills;
    }

    public void setProfessionalSkills(ProfessionalSkillsSection professionalSkills) {
        this.professionalSkills = professionalSkills;
    }

    public DevelopmentToolsSection getDevelopmentTools() {
        return developmentTools;
    }

    public void setDevelopmentTools(DevelopmentToolsSection developmentTools) {
        this.developmentTools = developmentTools;
    }

}
