package com.kelpiegang.tacoresume.ModelLayer;

public class Award {

    private String title;
    private String about;

    public Award(String title, String about) {
        this.title = title;
        this.about = about;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
