package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class UserRepository {

    private final Datastore datastore;
    private static UserRepository instance;

    private UserRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static UserRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new UserRepository(datastore);
        }
        return instance;
    }

    public User update(User user) throws DbError {
        try {
            User Dbuser = this.getById(user.getObjectId());
            if (user.getName() != null) {
                Dbuser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                Dbuser.setEmail(user.getEmail());
            }
            if (user.getGender() != null) {
                Dbuser.setGender(user.getGender());
            }
            this.datastore.save(Dbuser);
            return Dbuser;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User add(User user) throws DbError {
        try {
            this.datastore.save(user);
            return user;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User remove(User user) throws DbError {
        try {
            this.datastore.delete(user);
            return user;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User getById(ObjectId id) throws DbError {
        try {
            User user = datastore.get(User.class, id);
            if (user == null) {
                throw new DbError("User not found!");
            }
            return user;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User getByEmail(String email) throws DbError {
        try {
            User user = datastore.createQuery(User.class).field("email").equal(email).get();
            if (user != null) {
                return user;
            } else {
                throw new DbError("User not found");
            }
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<User> getAll() throws DbError {
        try {
            return datastore.find(User.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
