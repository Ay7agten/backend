package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.User;
import com.kelpiegang.tacoresume.ModelLayer.WorkExperience;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class WorkExperienceRepository {

    private final Datastore datastore;
    private static WorkExperienceRepository instance;

    private WorkExperienceRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static WorkExperienceRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new WorkExperienceRepository(datastore);
        }
        return instance;
    }

    public WorkExperience add(WorkExperience workExperience) throws DbError {
        try {
            this.datastore.save(workExperience);
            return workExperience;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<WorkExperience> update(ArrayList<WorkExperience> workExperiences) throws DbError {
        try {
            this.removeAllByUser(workExperiences.get(0).getUser());
            this.datastore.save(workExperiences);
            return workExperiences;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<WorkExperience> removeAllByUser(User user) throws DbError {
        try {
            List<WorkExperience> removed = this.getAllByUser(user);
            this.datastore.delete(datastore.find(WorkExperience.class).field("user").equal(user));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public WorkExperience remove(WorkExperience workExperience) throws DbError {
        try {
            this.datastore.delete(workExperience);
            return workExperience;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public WorkExperience getById(ObjectId id) throws DbError {
        try {
            WorkExperience workExperience = datastore.get(WorkExperience.class, id);
            if (workExperience == null) {
                throw new DbError("Work Experience not found!");
            }
            return workExperience;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<WorkExperience> getAllByUser(User user) throws DbError {
        try {
            return datastore.find(WorkExperience.class).field("user").equal(user).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<WorkExperience> getAll() throws DbError {
        try {
            return datastore.find(WorkExperience.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
