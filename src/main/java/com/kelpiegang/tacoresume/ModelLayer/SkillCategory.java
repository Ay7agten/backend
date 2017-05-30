package com.kelpiegang.tacoresume.ModelLayer;

import java.util.ArrayList;

public class SkillCategory {

    private String title;
    private ArrayList<Skill> skills;

    public SkillCategory(String title, ArrayList<Skill> skills) {
        this.title = title;
        this.skills = skills;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

}
