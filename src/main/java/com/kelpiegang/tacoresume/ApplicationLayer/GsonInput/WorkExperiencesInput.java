package com.kelpiegang.tacoresume.ApplicationLayer.GsonInput;

import com.kelpiegang.tacoresume.ModelLayer.WorkExperience;
import java.util.ArrayList;

public class WorkExperiencesInput {

    private ArrayList<WorkExperience> workExperiences;

    public WorkExperiencesInput(ArrayList<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public ArrayList<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(ArrayList<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }
}
