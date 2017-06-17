package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.Reference;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class ReferenceRepository {

    private final Datastore datastore;
    private static ReferenceRepository instance;

    private ReferenceRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static ReferenceRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new ReferenceRepository(datastore);
        }
        return instance;
    }

    public Reference add(Reference reference) throws DbError {
        try {
            this.datastore.save(reference);
            return reference;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Reference> updateReferenceByUser(ArrayList<Reference> references, User user) throws DbError {
        for (Reference reference : references) {
            reference.setUser(user);
        }
        try {
            if (references.isEmpty()) {
                this.removeAllByUser(user);
            } else {
                this.removeAllByUser(user);
                this.datastore.save(references);
            }
            return references;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Reference remove(Reference reference) throws DbError {
        try {
            this.datastore.delete(reference);
            return reference;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Reference> removeAllByUser(User user) throws DbError {
        try {
            List<Reference> removed = this.getAllByUser(user);
            this.datastore.delete(datastore.find(Reference.class).field("user").equal(user));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Reference getById(ObjectId id) throws DbError {
        try {
            Reference reference = datastore.get(Reference.class, id);
            if (reference == null) {
                throw new DbError("Reference not found!");
            }
            return reference;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Reference> getAllByUser(User user) throws DbError {
        try {
            return datastore.find(Reference.class).field("user").equal(user).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Reference> getAll() throws DbError {
        try {
            return datastore.find(Reference.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
