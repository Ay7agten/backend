package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.DevelopmentToolsSection;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class DevelopmentToolsSectionRepository {

    private final Datastore datastore;
    private static DevelopmentToolsSectionRepository instance;

    private DevelopmentToolsSectionRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static DevelopmentToolsSectionRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new DevelopmentToolsSectionRepository(datastore);
        }
        return instance;
    }

    public DevelopmentToolsSection add(DevelopmentToolsSection developmentToolsSection) throws DbError {
        try {
            ObjectId _id = (ObjectId) this.datastore.save(developmentToolsSection).getId();
            developmentToolsSection.setId(_id);
            return developmentToolsSection;

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public DevelopmentToolsSection updateByUser(DevelopmentToolsSection developmentToolsSection, User user) throws DbError {

        developmentToolsSection.setUser(user);
        DevelopmentToolsSection dbDevelopmentToolsSection = this.getByUser(user);
        if (dbDevelopmentToolsSection == null) {
            return this.add(developmentToolsSection);
        } else {
            developmentToolsSection.setId(dbDevelopmentToolsSection.getObjectId());
            return developmentToolsSection;
        }
    }

    public DevelopmentToolsSection remove(DevelopmentToolsSection developmentToolsSection) throws DbError {
        try {
            this.datastore.delete(developmentToolsSection);
            return developmentToolsSection;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public DevelopmentToolsSection getById(ObjectId id) throws DbError {
        try {
            DevelopmentToolsSection developmentToolsSection = datastore.get(DevelopmentToolsSection.class, id);
            if (developmentToolsSection == null) {
                throw new DbError("Development tools section not found!");
            }
            return developmentToolsSection;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public DevelopmentToolsSection getByUser(User user) throws DbError {
        try {
            return datastore.find(DevelopmentToolsSection.class).field("user").equal(user).get();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<DevelopmentToolsSection> getAll() throws DbError {
        try {
            return datastore.find(DevelopmentToolsSection.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
