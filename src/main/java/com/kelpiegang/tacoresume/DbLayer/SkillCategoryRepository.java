package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.DevelopmentToolsSection;
import com.kelpiegang.tacoresume.ModelLayer.SkillCategory;
import com.kelpiegang.tacoresume.ModelLayer.ProfessionalSkillsSection;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class SkillCategoryRepository {

    private final Datastore datastore;
    private static SkillCategoryRepository instance;

    private SkillCategoryRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static SkillCategoryRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new SkillCategoryRepository(datastore);
        }
        return instance;
    }

    public SkillCategory add(SkillCategory skillCategory) throws DbError {
        try {
            ObjectId _id = (ObjectId) this.datastore.save(skillCategory).getId();
            skillCategory.setId(_id);
            return skillCategory;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }
    
    public List<SkillCategory> removeAllByProfessionalSkillsSection(ProfessionalSkillsSection professionalSkillsSection) throws DbError {
        try {
            List<SkillCategory> removed = this.getAllByProfessionalSkillsSection(professionalSkillsSection);
            this.datastore.delete(datastore.find(SkillCategory.class).field("professionalSkillsSection").equal(professionalSkillsSection));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<SkillCategory> removeAllByDevelopmentToolsSection(DevelopmentToolsSection developmentToolsSection) throws DbError {
        try {
            List<SkillCategory> removed = this.getAllByDevelopmentToolsSection(developmentToolsSection);
            this.datastore.delete(datastore.find(SkillCategory.class).field("developmentToolsSection").equal(developmentToolsSection));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public SkillCategory remove(SkillCategory skillCategory) throws DbError {
        try {
            this.datastore.delete(skillCategory);
            return skillCategory;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public SkillCategory getById(ObjectId id) throws DbError {
        try {
            SkillCategory skillCategory = datastore.get(SkillCategory.class, id);
            if (skillCategory == null) {
                throw new DbError("Work Experience not found!");
            }
            return skillCategory;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<SkillCategory> getAllByProfessionalSkillsSection(ProfessionalSkillsSection professionalSkillsSection) throws DbError {
        try {
            return datastore.find(SkillCategory.class).field("professionalSkillsSection").equal(professionalSkillsSection).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<SkillCategory> getAllByDevelopmentToolsSection(DevelopmentToolsSection developmentToolsSection) throws DbError {
        try {
            return datastore.find(SkillCategory.class).field("developmentToolsSection").equal(developmentToolsSection).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<SkillCategory> getAll() throws DbError {
        try {
            return datastore.find(SkillCategory.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
