package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.BasicInformation;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class BasicInformationRepository {

    private final Datastore datastore;
    private static BasicInformationRepository instance;

    private BasicInformationRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static BasicInformationRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new BasicInformationRepository(datastore);
        }
        return instance;
    }

    public BasicInformation add(BasicInformation basicInformation) throws DbError {
        try {
            this.datastore.save(basicInformation);
            return basicInformation;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public BasicInformation updateBasicInformationByUser(BasicInformation basicInformation, User user) throws DbError {

        basicInformation.setUser(user);

        try {
            this.datastore.save(basicInformation);
            return basicInformation;

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public BasicInformation remove(BasicInformation basicInformation) throws DbError {
        try {
            this.datastore.delete(basicInformation);
            return basicInformation;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public BasicInformation getById(ObjectId id) throws DbError {
        try {
            BasicInformation basicInformation = datastore.get(BasicInformation.class, id);
            if (basicInformation == null) {
                throw new DbError("Basic Information not found!");
            }
            return basicInformation;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public BasicInformation getByUser(User user) throws DbError {
        try {
            return datastore.find(BasicInformation.class).field("user").equal(user).get();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<BasicInformation> getAll() throws DbError {
        try {
            return datastore.find(BasicInformation.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
