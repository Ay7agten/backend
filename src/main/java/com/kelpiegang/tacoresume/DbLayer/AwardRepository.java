package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.Award;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class AwardRepository {

    private final Datastore datastore;
    private static AwardRepository instance;

    private AwardRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static AwardRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new AwardRepository(datastore);
        }
        return instance;
    }

    public Award add(Award award) throws DbError {
        try {
            this.datastore.save(award);
            return award;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Award> updateAwardsByUser(ArrayList<Award> awards, User user) throws DbError {
        for (Award award : awards) {
            award.setUser(user);
        }
        try {
            if (awards.isEmpty()) {
                this.removeAllByUser(user);

            } else {
                this.removeAllByUser(user);
                this.datastore.save(awards);
            }
            return awards;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Award remove(Award award) throws DbError {
        try {
            this.datastore.delete(award);
            return award;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Award> removeAllByUser(User user) throws DbError {
        try {
            List<Award> removed = this.getAllByUser(user);
            this.datastore.delete(datastore.find(Award.class).field("user").equal(user));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Award getById(ObjectId id) throws DbError {
        try {
            Award award = datastore.get(Award.class, id);
            if (award == null) {
                throw new DbError("Work Experience not found!");
            }
            return award;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Award> getAllByUser(User user) throws DbError {
        try {
            return datastore.find(Award.class).field("user").equal(user).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Award> getAll() throws DbError {
        try {
            return datastore.find(Award.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
