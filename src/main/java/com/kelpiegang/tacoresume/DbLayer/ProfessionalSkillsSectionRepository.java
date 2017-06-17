package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.ProfessionalSkillsSection;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class ProfessionalSkillsSectionRepository {

    private final Datastore datastore;
    private static ProfessionalSkillsSectionRepository instance;

    private ProfessionalSkillsSectionRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static ProfessionalSkillsSectionRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new ProfessionalSkillsSectionRepository(datastore);
        }
        return instance;
    }

    public ProfessionalSkillsSection add(ProfessionalSkillsSection professionalSkillsSection) throws DbError {
        try {
            ObjectId _id = (ObjectId) this.datastore.save(professionalSkillsSection).getId();
            professionalSkillsSection.setId(_id);
            return professionalSkillsSection;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ProfessionalSkillsSection updateByUser(ProfessionalSkillsSection professionalSkillsSection, User user) throws DbError {

        professionalSkillsSection.setUser(user);
        ProfessionalSkillsSection dbProfessionalSkillsSection = this.getByUser(user);
        if (dbProfessionalSkillsSection == null) {
            return this.add(professionalSkillsSection);
        }else {
            professionalSkillsSection.setId(dbProfessionalSkillsSection.getObjectId());
            return professionalSkillsSection;
        }

    }

    public ProfessionalSkillsSection remove(ProfessionalSkillsSection professionalSkills) throws DbError {
        try {
            this.datastore.delete(professionalSkills);
            return professionalSkills;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ProfessionalSkillsSection getById(ObjectId id) throws DbError {
        try {
            ProfessionalSkillsSection professionalSkills = datastore.get(ProfessionalSkillsSection.class, id);
            if (professionalSkills == null) {
                throw new DbError("Professional skills section not found!");
            }
            return professionalSkills;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ProfessionalSkillsSection getByUser(User user) throws DbError {
        try {
            return datastore.find(ProfessionalSkillsSection.class).field("user").equal(user).get();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<ProfessionalSkillsSection> getAll() throws DbError {
        try {
            return datastore.find(ProfessionalSkillsSection.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }
}
