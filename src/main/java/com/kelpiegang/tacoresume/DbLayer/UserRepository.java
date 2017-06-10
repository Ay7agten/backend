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
            User dbUser = this.getById(user.getObjectId());
            if (user.getName() != null) {
                dbUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                dbUser.setEmail(user.getEmail());
            }
            if (user.getAbout() != null) {
                dbUser.setAbout(user.getAbout());
            }
            if (user.getJobTitle() != null) {
                dbUser.setJobTitle(user.getJobTitle());
            }
            this.datastore.save(dbUser);
            return dbUser;
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

    public User getByFacebookId(String facebookId) throws DbError {
        try {
            User user = datastore.createQuery(User.class).field("facebookId").equal(facebookId).get();
            return user;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User getByGoogleId(String googleId) throws DbError {
        try {
            User user = datastore.createQuery(User.class).field("googleId").equal(googleId).get();
            return user;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public User getByLinkedInId(String linkedInId) throws DbError {
        try {
            User user = datastore.createQuery(User.class).field("linkedInId").equal(linkedInId).get();
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
