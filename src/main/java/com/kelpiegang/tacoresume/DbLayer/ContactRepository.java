package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.Contact;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class ContactRepository {

    private final Datastore datastore;
    private static ContactRepository instance;

    private ContactRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static ContactRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new ContactRepository(datastore);
        }
        return instance;
    }

    public Contact add(Contact contact) throws DbError {
        try {
            this.datastore.save(contact);
            return contact;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Contact updateContactByUser(Contact contact, User user) throws DbError {

        contact.setUser(user);

        try {
            this.datastore.save(contact);
            return contact;

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Contact remove(Contact contact) throws DbError {
        try {
            this.datastore.delete(contact);
            return contact;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Contact getById(ObjectId id) throws DbError {
        try {
            Contact contact = datastore.get(Contact.class, id);
            if (contact == null) {
                throw new DbError("Work Experience not found!");
            }
            return contact;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Contact getByUser(User user) throws DbError {
        try {
            return datastore.find(Contact.class).field("user").equal(user).get();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Contact> getAll() throws DbError {
        try {
            return datastore.find(Contact.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
