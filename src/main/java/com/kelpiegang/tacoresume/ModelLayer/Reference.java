package com.kelpiegang.tacoresume.ModelLayer;

public class Reference {

    private String name;
    private String jobTitle;
    private String email;

    public Reference(String name, String jobTitle, String email) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
