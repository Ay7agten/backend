package com.kelpiegang.tacoresume.DbLayer;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ModelLayer.SkillCategory;
import com.kelpiegang.tacoresume.ModelLayer.Skill;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class SkillRepository {

    private final Datastore datastore;
    private static SkillRepository instance;

    private SkillRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public static SkillRepository getInstance(Datastore datastore) {
        if (instance == null) {
            instance = new SkillRepository(datastore);
        }
        return instance;
    }

    public Skill add(Skill skill) throws DbError {
        try {
            this.datastore.save(skill);
            return skill;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Skill> addAll(ArrayList<Skill> skills) throws DbError {
        try {
            this.datastore.save(skills);
            return skills;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Skill> update(ArrayList<Skill> skills) throws DbError {
        try {
            this.removeAllBySkillCategory(skills.get(0).getSkillCategory());
            this.datastore.save(skills);
            return skills;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public ArrayList<Skill> updateSkillsBySkillCategory(ArrayList<Skill> skills, SkillCategory skillCategory) throws DbError {
        for (Skill skill : skills) {
            skill.setSkillCategory(skillCategory);
        }
        try {
            if (skills.isEmpty()) {
                this.removeAllBySkillCategory(skillCategory);

            } else {
                this.removeAllBySkillCategory(skillCategory);
                this.datastore.save(skills);
            }
            return skills;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Skill> removeAllBySkillCategory(SkillCategory skillCategory) throws DbError {
        try {
            List<Skill> removed = this.getAllBySkillCategory(skillCategory);
            this.datastore.delete(datastore.find(Skill.class).field("skillCategory").equal(skillCategory));
            return removed;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Skill remove(Skill skill) throws DbError {
        try {
            this.datastore.delete(skill);
            return skill;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public Skill getById(ObjectId id) throws DbError {
        try {
            Skill skill = datastore.get(Skill.class, id);
            if (skill == null) {
                throw new DbError("Work Experience not found!");
            }
            return skill;
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Skill> getAllBySkillCategory(SkillCategory skillCategory) throws DbError {
        try {
            return datastore.find(Skill.class).field("skillCategory").equal(skillCategory).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

    public List<Skill> getAll() throws DbError {
        try {
            return datastore.find(Skill.class).asList();
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }
    }

}
