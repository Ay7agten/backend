package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.Education;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class EducationRepository {

    private final Datastore datastore;
    private static EducationRepository instance;

    private EducationRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static EducationRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new EducationRepository(datastore);
        }
        return instance;
    }

    public Education add(Education education) throws DbError {
        try {
            this.datastore.save(education);
            return education;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Education> updateAwardsByUser(ArrayList<Education> educations, User user) throws DbError {
        for (Education education : educations) {
            education.setUser(user);
        }
        try {
            if (educations.isEmpty()) {
                this.removeAllByUser(user);
            } else {
                this.removeAllByUser(user);
                this.datastore.save(educations);
            }
            return educations;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Education remove(Education education) throws DbError {
        try {
            this.datastore.delete(education);
            return education;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Education> removeAllByUser(User user) throws DbError {
        try {
            List<Education> removed = this.getAllByUser(user);
            this.datastore.delete(datastore.find(Education.class).field("user").equal(user));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Education getById(ObjectId id) throws DbError {
        try {
            Education education = datastore.get(Education.class, id);
            if (education == null) {
                throw new DbError("Work Experience not found!");
            }
            return education;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Education> getAllByUser(User user) throws DbError {
        try {
            return datastore.find(Education.class).field("user").equal(user).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Education> getAll() throws DbError {
        try {
            return datastore.find(Education.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
