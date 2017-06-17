package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ApplicationLayer.GsonInput.UserInput;
import com.kelpiegang.tacoresume.DbLayer.AwardRepository;
import com.kelpiegang.tacoresume.DbLayer.BasicInformationRepository;
import com.kelpiegang.tacoresume.DbLayer.ContactRepository;
import com.kelpiegang.tacoresume.DbLayer.DevelopmentToolsSectionRepository;
import com.kelpiegang.tacoresume.DbLayer.EducationRepository;
import com.kelpiegang.tacoresume.DbLayer.ProfessionalSkillsSectionRepository;
import com.kelpiegang.tacoresume.DbLayer.ReferenceRepository;
import com.kelpiegang.tacoresume.DbLayer.SkillCategoryRepository;
import com.kelpiegang.tacoresume.DbLayer.SkillRepository;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.DbLayer.WorkExperienceRepository;
import com.kelpiegang.tacoresume.ModelLayer.DevelopmentToolsSection;
import com.kelpiegang.tacoresume.ModelLayer.ProfessionalSkillsSection;
import com.kelpiegang.tacoresume.ModelLayer.Skill;
import com.kelpiegang.tacoresume.ModelLayer.SkillCategory;
import com.kelpiegang.tacoresume.ModelLayer.User;
import graphql.GraphQLException;
import graphql.Scalars;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import static graphql.schema.GraphQLInputObjectField.*;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TacoResumeSchemaMutations {

    private GraphQLObjectType updateUserMutation;
    private TacoResumeSchema tacoResumeSchema;

    private UserRepository userRepo;
    private WorkExperienceRepository workExpRepo;
    private AwardRepository awardRepo;
    private ContactRepository contactRepo;
    private DevelopmentToolsSectionRepository developmentToolsSectionRepo;
    private EducationRepository educationRepo;
    private ProfessionalSkillsSectionRepository professionalSkillsSectionRepo;
    private ReferenceRepository referenceRepo;
    private SkillCategoryRepository skillCategoryRepo;
    private SkillRepository skillRepo;
    private BasicInformationRepository basicInfoRepo;

    private Gson gson;

    public TacoResumeSchemaMutations(TacoResumeSchema tacoResumeSchema, UserRepository userRepo, WorkExperienceRepository workExpRepo,
            AwardRepository awardRepo, ContactRepository contactRepo,
            DevelopmentToolsSectionRepository developmentToolsSectionRepo, EducationRepository educationRepo,
            ProfessionalSkillsSectionRepository professionalSkillsSectionRepo, ReferenceRepository referenceRepo,
            SkillCategoryRepository skillCategoryRepo, SkillRepository skillRepo, BasicInformationRepository basicInfoRepo, Gson gson) {

        this.userRepo = userRepo;
        this.workExpRepo = workExpRepo;
        this.awardRepo = awardRepo;
        this.contactRepo = contactRepo;
        this.developmentToolsSectionRepo = developmentToolsSectionRepo;
        this.educationRepo = educationRepo;
        this.professionalSkillsSectionRepo = professionalSkillsSectionRepo;
        this.referenceRepo = referenceRepo;
        this.skillCategoryRepo = skillCategoryRepo;
        this.skillRepo = skillRepo;
        this.basicInfoRepo = basicInfoRepo;

        this.tacoResumeSchema = tacoResumeSchema;
        this.gson = gson;
        createUpdateUserMutation();
    }

    private void createUpdateUserMutation() {

        updateUserMutation = newObject()
                .name("updateUserMutation")
                .field(newFieldDefinition()
                        .name("updateUser")
                        .type(tacoResumeSchema.getUserType())
                        .argument(newArgument()
                                .name("UserInput")
                                .type(createUserInputType())
                        )
                        .dataFetcher(updateUsermutationDataFetcher()))
                .build();
    }

    private GraphQLInputObjectType createUserInputType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField nameField = newInputObjectField()
                .name("name")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField emailField = newInputObjectField()
                .name("email")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField aboutField = newInputObjectField()
                .name("about")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField jobTitleField = newInputObjectField()
                .name("jobTitle")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField workExperiencesField = newInputObjectField()
                .name("workExperiences")
                .type(new GraphQLList(createWorkExperienceInputType())).build();

        GraphQLInputObjectField awardsField = newInputObjectField()
                .name("awards")
                .type(new GraphQLList(createAwardInputType())).build();

        GraphQLInputObjectField educationsField = newInputObjectField()
                .name("educations")
                .type(new GraphQLList(createEducationType())).build();

        GraphQLInputObjectField referencesField = newInputObjectField()
                .name("references")
                .type(new GraphQLList(createReferenceType())).build();

        GraphQLInputObjectField contactField = newInputObjectField()
                .name("contact")
                .type(createContactType()).build();

        GraphQLInputObjectField professionalSkillsSectionField = newInputObjectField()
                .name("professionalSkills")
                .type(createProfessionalSkillsSeciotnType()).build();

        GraphQLInputObjectField developmentToolsSectionField = newInputObjectField()
                .name("developmentTools")
                .type(createDevelopmentToolsSeciotnType()).build();

        GraphQLInputObjectField basicInformationField = newInputObjectField()
                .name("basicInformation")
                .type(createBasicInformationType()).build();

        GraphQLInputObjectType userInputType = GraphQLInputObjectType.newInputObject()
                .name("UserInput")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(jobTitleField)
                .field(workExperiencesField)
                .field(awardsField)
                .field(educationsField)
                .field(referencesField)
                .field(contactField)
                .field(professionalSkillsSectionField)
                .field(developmentToolsSectionField)
                .field(basicInformationField)
                .build();

        return userInputType;
    }

    private GraphQLInputObjectType createWorkExperienceInputType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField titleField = newInputObjectField()
                .name("title")
                .type(GraphQLString).build();

        GraphQLInputObjectField companyField = newInputObjectField()
                .name("company")
                .type(GraphQLString).build();

        GraphQLInputObjectField startDateField = newInputObjectField()
                .name("startDate")
                .type(GraphQLLong).build();

        GraphQLInputObjectField endDateField = newInputObjectField()
                .name("endDate")
                .type(GraphQLLong).build();

        GraphQLInputObjectField dutiesField = newInputObjectField()
                .name("duties")
                .type(new GraphQLList(GraphQLString)).build();

        GraphQLInputObjectField toolsField = newInputObjectField()
                .name("tools")
                .type(new GraphQLList(GraphQLString)).build();

        GraphQLInputObjectType workExperienceInputType = GraphQLInputObjectType.newInputObject()
                .name("WorkExperienceInput")
                .field(idField)
                .field(titleField)
                .field(companyField)
                .field(startDateField)
                .field(endDateField)
                .field(dutiesField)
                .field(toolsField)
                .build();

        return workExperienceInputType;

    }

    private GraphQLInputObjectType createAwardInputType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField titleField = newInputObjectField()
                .name("title")
                .type(GraphQLString).build();

        GraphQLInputObjectField aboutField = newInputObjectField()
                .name("about")
                .type(GraphQLString).build();

        return GraphQLInputObjectType.newInputObject()
                .name("AwardInput")
                .field(idField)
                .field(titleField)
                .field(aboutField)
                .build();
    }

    private GraphQLInputObjectType createSkillInputType(String name) {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField titleField = newInputObjectField()
                .name("title")
                .type(GraphQLString).build();

        GraphQLInputObjectField percentageField = newInputObjectField()
                .name("percentage")
                .type(GraphQLInt).build();

        return GraphQLInputObjectType.newInputObject()
                .name("SkillInput" + name)
                .field(idField)
                .field(titleField)
                .field(percentageField)
                .build();
    }

    private GraphQLInputObjectType createSkillCategoryInputType(String name) {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField titleField = newInputObjectField()
                .name("title")
                .type(GraphQLString).build();

        GraphQLInputObjectField skillsField = newInputObjectField()
                .name("skills")
                .type(new GraphQLList(createSkillInputType(name))).build();

        return GraphQLInputObjectType.newInputObject()
                .name("SkillCategoryInput" + name)
                .field(idField)
                .field(titleField)
                .field(skillsField)
                .build();
    }

    private GraphQLInputObjectType createDevelopmentToolsSeciotnType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField skillCategoriesField = newInputObjectField()
                .name("skillCategories")
                .type(new GraphQLList(createSkillCategoryInputType("DevSection")))
                .build();

        return GraphQLInputObjectType.newInputObject()
                .name("DevelopmentToolsSectionInput")
                .field(idField)
                .field(skillCategoriesField)
                .build();
    }

    private GraphQLInputObjectType createProfessionalSkillsSeciotnType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField skillCategoriesField = newInputObjectField()
                .name("skillCategories")
                .type(new GraphQLList(createSkillCategoryInputType("ProfSection")))
                .build();

        return GraphQLInputObjectType.newInputObject()
                .name("ProfessionalSkillsSectionInput")
                .field(idField)
                .field(skillCategoriesField)
                .build();
    }

    private GraphQLInputObjectType createContactType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField phoneNumberField = newInputObjectField()
                .name("phoneNumber")
                .type(GraphQLString).build();

        GraphQLInputObjectField emailField = newInputObjectField()
                .name("email")
                .type(GraphQLString).build();

        GraphQLInputObjectField websiteField = newInputObjectField()
                .name("website")
                .type(GraphQLString).build();

        return GraphQLInputObjectType.newInputObject()
                .name("ContactInput")
                .field(idField)
                .field(phoneNumberField)
                .field(emailField)
                .field(websiteField)
                .build();
    }

    private GraphQLInputObjectType createReferenceType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField nameField = newInputObjectField()
                .name("name")
                .type(GraphQLString).build();

        GraphQLInputObjectField emailField = newInputObjectField()
                .name("email")
                .type(GraphQLString).build();

        GraphQLInputObjectField jobTitleField = newInputObjectField()
                .name("jobTitle")
                .type(GraphQLString).build();

        return GraphQLInputObjectType.newInputObject()
                .name("ReferenceInput")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(jobTitleField)
                .build();
    }

    private GraphQLInputObjectType createEducationType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField degreeField = newInputObjectField()
                .name("degree")
                .type(GraphQLString).build();

        GraphQLInputObjectField universityField = newInputObjectField()
                .name("university")
                .type(GraphQLString).build();

        GraphQLInputObjectField startDateField = newInputObjectField()
                .name("startDate")
                .type(GraphQLLong).build();

        GraphQLInputObjectField endDateField = newInputObjectField()
                .name("endDate")
                .type(GraphQLLong).build();

        return GraphQLInputObjectType.newInputObject()
                .name("EducationInput")
                .field(idField)
                .field(degreeField)
                .field(universityField)
                .field(startDateField)
                .field(endDateField)
                .build();
    }

    private GraphQLInputObjectType createBasicInformationType() {

        GraphQLInputObjectField idField = newInputObjectField()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLInputObjectField nameField = newInputObjectField()
                .name("name")
                .type(GraphQLString).build();

        GraphQLInputObjectField emailField = newInputObjectField()
                .name("email")
                .type(GraphQLString).build();

        GraphQLInputObjectField aboutField = newInputObjectField()
                .name("about")
                .type(GraphQLString).build();

        GraphQLInputObjectField jobTitleField = newInputObjectField()
                .name("jobTitle")
                .type(GraphQLString).build();

        return GraphQLInputObjectType.newInputObject()
                .name("BasicInformationInput")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(jobTitleField)
                .build();
    }

    private DataFetcher updateUsermutationDataFetcher() {
        return new DataFetcher() {
            @Override
            public User get(DataFetchingEnvironment environment) {
                //TODO id string to object id by gson and right implementation for db queries
                LinkedHashMap userInputMap = environment.getArgument("UserInput");
                String userInputJsonString = gson.toJson(userInputMap);
                UserInput userInput = gson.fromJson(userInputJsonString, UserInput.class);
                User user = gson.fromJson(userInputJsonString, User.class);

                if (!environment.getContext().toString().equals(user.get_id())) {
                    throw new GraphQLException("Not Authorized");
                } else {
                    try {
                        User updatedUser = userRepo.update(user);
                        if (userInput.getWorkExperiences() != null) {
                            workExpRepo.updateWorkExperiencesByUser(userInput.getWorkExperiences(), user);
                        }
                        if (userInput.getEducations() != null) {
                            educationRepo.updateAwardsByUser(userInput.getEducations(), user);
                        }
                        if (userInput.getAwards() != null) {
                            awardRepo.updateAwardsByUser(userInput.getAwards(), user);
                        }
                        if (userInput.getReferences() != null) {
                            referenceRepo.updateReferenceByUser(userInput.getReferences(), user);
                        }
                        if (userInput.getContact() != null) {
                            contactRepo.updateContactByUser(userInput.getContact(), user);
                        }

                        if (userInput.getBasicInformation() != null) {
                            basicInfoRepo.updateBasicInformationByUser(userInput.getBasicInformation(), user);
                        }

                        if (userInput.getProfessionalSkills() != null) {
                            ArrayList<SkillCategory> skillCategories = userInput.getProfessionalSkills().getSkillCategories();
                            ProfessionalSkillsSection professionalSkillsSection = professionalSkillsSectionRepo.updateByUser(userInput.getProfessionalSkills(), user);

                            List<SkillCategory> removedSkillCategorys = skillCategoryRepo.getAllByProfessionalSkillsSection(professionalSkillsSection);
                            for (SkillCategory removedSkillCategory : removedSkillCategorys) {
                                skillRepo.removeAllBySkillCategory(removedSkillCategory);
                            }

                            skillCategoryRepo.removeAllByProfessionalSkillsSection(professionalSkillsSection);

                            for (SkillCategory skillCategory : skillCategories) {

                                skillCategory.setProfessionalSkillsSection(professionalSkillsSection);
                                SkillCategory dbSkillCategory = skillCategoryRepo.add(skillCategory);
                                for (Skill skill : dbSkillCategory.getSkills()) {
                                    skill.setSkillCategory(skillCategory);
                                }

                                skillRepo.addAll(dbSkillCategory.getSkills());
                            }

                        }
                        if (userInput.getDevelopmentTools() != null) {
                            ArrayList<SkillCategory> skillCategories = userInput.getDevelopmentTools().getSkillCategories();
                            DevelopmentToolsSection developmentToolsSection = developmentToolsSectionRepo.updateByUser(userInput.getDevelopmentTools(), user);

                            List<SkillCategory> removedSkillCategorys = skillCategoryRepo.getAllByDevelopmentToolsSection(developmentToolsSection);
                            for (SkillCategory removedSkillCategory : removedSkillCategorys) {
                                skillRepo.removeAllBySkillCategory(removedSkillCategory);
                            }
                            skillCategoryRepo.removeAllByDevelopmentToolsSection(developmentToolsSection);

                            for (SkillCategory skillCategory : skillCategories) {
                                skillCategory.setDevelopmentToolsSection(developmentToolsSection);
                                SkillCategory dbSkillCategory = skillCategoryRepo.add(skillCategory);
                                for (Skill skill : dbSkillCategory.getSkills()) {
                                    skill.setSkillCategory(skillCategory);
                                }
                                skillRepo.addAll(dbSkillCategory.getSkills());
                            }
                        }

                        return updatedUser;
                    } catch (DbError ex) {
                        throw new GraphQLException(ex.getMessage());
                    }
                }
            }
        };
    }

    public GraphQLObjectType getUpdateUserMutation() {
        return this.updateUserMutation;
    }
}
