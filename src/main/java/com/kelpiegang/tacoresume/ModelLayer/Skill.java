package com.kelpiegang.tacoresume.ModelLayer;

public class Skill {

    private String title;
    private int percentage;

    public Skill(String title, int percentage) {
        this.title = title;
        this.percentage = percentage;
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

}
