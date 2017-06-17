package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.DbLayer.*;
import com.kelpiegang.tacoresume.ModelLayer.*;
import graphql.GraphQLException;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import graphql.schema.*;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import graphql.schema.GraphQLSchema;
import java.util.List;
import org.bson.types.ObjectId;

public class TacoResumeSchema {

    private GraphQLSchema schema;
    private GraphQLObjectType userType;
    private GraphQLObjectType workExperienceType;
    private GraphQLObjectType awardType;
    private GraphQLObjectType contactType;
    private GraphQLObjectType developmentToolsSectionType;
    private GraphQLObjectType educationType;
    private GraphQLObjectType professionalSkillsSectionType;
    private GraphQLObjectType referenceType;
    private GraphQLObjectType skillType;
    private GraphQLObjectType skillCategoryType;
    private GraphQLObjectType BasicInformationType;

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

    public TacoResumeSchema(UserRepository userRepo, WorkExperienceRepository workExpRepo, AwardRepository awardRepo, ContactRepository contactRepo,
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

        this.gson = gson;
        createSchema();

    }

    private void createSchema() {

        createAwardType();
        createContactType();
        createSkillType();
        createSkillCategoryType();
        createDevelopmentToolsSeciotnType();
        createProfessionalSkillsSeciotnType();
        createEducationType();
        createReferenceType();
        createWorkExperienceType();
        createBasicInformation();
        createUserType();

        GraphQLFieldDefinition user
                = GraphQLFieldDefinition
                        .newFieldDefinition()
                        .name("user")
                        .type(userType)
                        .dataFetcher(userDataFetcher())
                        .argument(newArgument().name("_id")
                                .type(GraphQLString).build())
                        .build();

        GraphQLObjectType rootQuery
                = GraphQLObjectType
                        .newObject()
                        .name("rootQuery")
                        .field(user)
                        .build();

        TacoResumeSchemaMutations tacoResumeSchemaMutations = new TacoResumeSchemaMutations(this, userRepo, workExpRepo, awardRepo, contactRepo,
                developmentToolsSectionRepo, educationRepo, professionalSkillsSectionRepo, referenceRepo,
                skillCategoryRepo, skillRepo, basicInfoRepo, gson);

        GraphQLObjectType updateUserMutation = tacoResumeSchemaMutations.getUpdateUserMutation();

        schema = GraphQLSchema.newSchema().query(rootQuery).mutation(updateUserMutation).build();
    }

    private void createUserType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition facebookIdField = newFieldDefinition()
                .name("facebookId")
                .type(GraphQLString).build();

        GraphQLFieldDefinition googleIdField = newFieldDefinition()
                .name("googleId")
                .type(GraphQLString).build();

        GraphQLFieldDefinition linkedinIdField = newFieldDefinition()
                .name("linkedInId")
                .type(GraphQLString).build();

        GraphQLFieldDefinition nameField = newFieldDefinition()
                .name("name")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition aboutField = newFieldDefinition()
                .name("about")
                .type(GraphQLString).build();

        GraphQLFieldDefinition jobTitleField = newFieldDefinition()
                .name("jobTitle")
                .type(GraphQLString).build();

        GraphQLFieldDefinition workExperiencesField = newFieldDefinition()
                .name("workExperiences")
                .type(new GraphQLList(this.workExperienceType))
                .dataFetcher(workExperiencesDataFetcher())
                .build();

        GraphQLFieldDefinition educationsField = newFieldDefinition()
                .name("educations")
                .type(new GraphQLList(this.educationType))
                .dataFetcher(educationsDataFetcher())
                .build();

        GraphQLFieldDefinition referencesField = newFieldDefinition()
                .name("references")
                .type(new GraphQLList(this.referenceType))
                .dataFetcher(referencesDataFetcher())
                .build();

        GraphQLFieldDefinition awardsField = newFieldDefinition()
                .name("awards")
                .type(new GraphQLList(this.awardType))
                .dataFetcher(awardsDataFetcher())
                .build();

        GraphQLFieldDefinition contactField = newFieldDefinition()
                .name("contact")
                .type(this.contactType)
                .dataFetcher(contactDataFetcher())
                .build();

        GraphQLFieldDefinition professionalSkillsSectionField = newFieldDefinition()
                .name("professionalSkills")
                .type(this.professionalSkillsSectionType)
                .dataFetcher(professionalSkillsSectionFetcher())
                .build();

        GraphQLFieldDefinition developmentToolsSectionField = newFieldDefinition()
                .name("developmentTools")
                .type(this.developmentToolsSectionType)
                .dataFetcher(developmentToolsSectionFetcher())
                .build();

        GraphQLFieldDefinition basicInformationField = newFieldDefinition()
                .name("basicInformation")
                .type(this.BasicInformationType)
                .dataFetcher(basicInformationDataFetcher())
                .build();

        this.userType = newObject()
                .name("User")
                .field(idField)
                .field(facebookIdField)
                .field(googleIdField)
                .field(linkedinIdField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(jobTitleField)
                .field(workExperiencesField)
                .field(educationsField)
                .field(referencesField)
                .field(awardsField)
                .field(contactField)
                .field(professionalSkillsSectionField)
                .field(developmentToolsSectionField)
                .field(basicInformationField)
                .build();
    }

    private void createAwardType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition titleField = newFieldDefinition()
                .name("title")
                .type(GraphQLString).build();

        GraphQLFieldDefinition aboutField = newFieldDefinition()
                .name("about")
                .type(GraphQLString).build();

        this.awardType = newObject()
                .name("Award")
                .field(idField)
                .field(titleField)
                .field(aboutField)
                .build();

    }

    private void createSkillType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition titleField = newFieldDefinition()
                .name("title")
                .type(GraphQLString).build();

        GraphQLFieldDefinition percentageField = newFieldDefinition()
                .name("percentage")
                .type(GraphQLInt).build();

        this.skillType = newObject()
                .name("Skill")
                .field(idField)
                .field(titleField)
                .field(percentageField)
                .build();

    }

    private void createSkillCategoryType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition titleField = newFieldDefinition()
                .name("title")
                .type(GraphQLString).build();

        GraphQLFieldDefinition skillsField = newFieldDefinition()
                .name("skills")
                .dataFetcher(skillsDataFetcher())
                .type(new GraphQLList(this.skillType)).build();

        this.skillCategoryType = newObject()
                .name("SkillCategory")
                .field(idField)
                .field(titleField)
                .field(skillsField)
                .build();

    }

    private void createDevelopmentToolsSeciotnType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString)
                .build();

        GraphQLFieldDefinition skillCategoriesField = newFieldDefinition()
                .name("skillCategories")
                .type(new GraphQLList(this.skillCategoryType))
                .dataFetcher(devToolSkillCategoryFetcher())
                .build();

        this.developmentToolsSectionType = newObject()
                .name("DevelopmentToolsSection")
                .field(idField)
                .field(skillCategoriesField)
                .build();

    }

    private void createProfessionalSkillsSeciotnType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString)
                .build();

        GraphQLFieldDefinition skillCategoriesField = newFieldDefinition()
                .name("skillCategories")
                .type(new GraphQLList(this.skillCategoryType))
                .dataFetcher(profSkillsSkillCategoryFetcher())
                .build();

        this.professionalSkillsSectionType = newObject()
                .name("ProfessionalSkillsSection")
                .field(idField)
                .field(skillCategoriesField)
                .build();

    }

    private void createContactType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition phoneNumberField = newFieldDefinition()
                .name("phoneNumber")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition websiteField = newFieldDefinition()
                .name("website")
                .type(GraphQLString).build();

        this.contactType = newObject()
                .name("Contact")
                .field(idField)
                .field(phoneNumberField)
                .field(emailField)
                .field(websiteField)
                .build();

    }

    private void createReferenceType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition nameField = newFieldDefinition()
                .name("name")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition jobTitleField = newFieldDefinition()
                .name("jobTitle")
                .type(GraphQLString).build();

        this.referenceType = newObject()
                .name("Reference")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(jobTitleField)
                .build();

    }

    private void createEducationType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition degreeField = newFieldDefinition()
                .name("degree")
                .type(GraphQLString).build();

        GraphQLFieldDefinition universityField = newFieldDefinition()
                .name("university")
                .type(GraphQLString).build();

        GraphQLFieldDefinition startDateField = newFieldDefinition()
                .name("startDate")
                .type(GraphQLLong).build();

        GraphQLFieldDefinition endDateField = newFieldDefinition()
                .name("endDate")
                .type(GraphQLLong).build();

        this.educationType = newObject()
                .name("Education")
                .field(idField)
                .field(degreeField)
                .field(universityField)
                .field(startDateField)
                .field(endDateField)
                .build();
    }

    private void createWorkExperienceType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition titleField = newFieldDefinition()
                .name("title")
                .type(GraphQLString).build();

        GraphQLFieldDefinition companyField = newFieldDefinition()
                .name("company")
                .type(GraphQLString).build();

        GraphQLFieldDefinition startDateField = newFieldDefinition()
                .name("startDate")
                .type(GraphQLLong).build();

        GraphQLFieldDefinition endDateField = newFieldDefinition()
                .name("endDate")
                .type(GraphQLLong).build();

        GraphQLFieldDefinition dutiesField = newFieldDefinition()
                .name("duties")
                .type(new GraphQLList(GraphQLString)).build();

        GraphQLFieldDefinition toolsField = newFieldDefinition()
                .name("tools")
                .type(new GraphQLList(GraphQLString)).build();

        this.workExperienceType = newObject()
                .name("WorkExperience")
                .field(idField)
                .field(titleField)
                .field(companyField)
                .field(startDateField)
                .field(endDateField)
                .field(dutiesField)
                .field(toolsField)
                .build();
    }

    private void createBasicInformation() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition nameField = newFieldDefinition()
                .name("name")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition aboutField = newFieldDefinition()
                .name("about")
                .type(GraphQLString).build();

        GraphQLFieldDefinition jobTitleField = newFieldDefinition()
                .name("jobTitle")
                .type(GraphQLString).build();

        this.BasicInformationType = newObject()
                .name("BasicInformation")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(jobTitleField)
                .build();

    }

    private DataFetcher userDataFetcher() {

        return new DataFetcher<User>() {
            @Override
            public User get(DataFetchingEnvironment environment) {

                String id = environment.getArgument("_id");
                String jwtId = environment.getContext().toString();

                try {
                    if (id == null) {
                        return userRepo.getById(new ObjectId(jwtId));
                    } else {
                        return userRepo.getById(new ObjectId(id));
                    }
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };

    }

    private DataFetcher workExperiencesDataFetcher() {

        return new DataFetcher<List<WorkExperience>>() {
            @Override
            public List<WorkExperience> get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return workExpRepo.getAllByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher skillsDataFetcher() {

        return new DataFetcher<List<Skill>>() {
            @Override
            public List<Skill> get(DataFetchingEnvironment environment) {
                SkillCategory skillCategory = (SkillCategory) environment.getSource();
                try {
                    return skillRepo.getAllBySkillCategory(skillCategory);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher educationsDataFetcher() {

        return new DataFetcher<List<Education>>() {
            @Override
            public List<Education> get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return educationRepo.getAllByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher referencesDataFetcher() {

        return new DataFetcher<List<Reference>>() {
            @Override
            public List<Reference> get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return referenceRepo.getAllByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher awardsDataFetcher() {

        return new DataFetcher<List<Award>>() {
            @Override
            public List<Award> get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return awardRepo.getAllByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher contactDataFetcher() {

        return new DataFetcher<Contact>() {
            @Override
            public Contact get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return contactRepo.getByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher devToolSkillCategoryFetcher() {

        return new DataFetcher<List<SkillCategory>>() {
            @Override
            public List<SkillCategory> get(DataFetchingEnvironment environment) {
                DevelopmentToolsSection developmentToolsSection = (DevelopmentToolsSection) environment.getSource();
                try {
                    return skillCategoryRepo.getAllByDevelopmentToolsSection(developmentToolsSection);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher profSkillsSkillCategoryFetcher() {

        return new DataFetcher<List<SkillCategory>>() {
            @Override
            public List<SkillCategory> get(DataFetchingEnvironment environment) {
                ProfessionalSkillsSection professionalSkillsSection = (ProfessionalSkillsSection) environment.getSource();
                try {
                    return skillCategoryRepo.getAllByProfessionalSkillsSection(professionalSkillsSection);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }

            }
        };
    }

    private DataFetcher professionalSkillsSectionFetcher() {

        return new DataFetcher<ProfessionalSkillsSection>() {
            @Override
            public ProfessionalSkillsSection get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return professionalSkillsSectionRepo.getByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher developmentToolsSectionFetcher() {

        return new DataFetcher<DevelopmentToolsSection>() {
            @Override
            public DevelopmentToolsSection get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return developmentToolsSectionRepo.getByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    private DataFetcher basicInformationDataFetcher() {

        return new DataFetcher<BasicInformation>() {
            @Override
            public BasicInformation get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                try {
                    return basicInfoRepo.getByUser(user);
                } catch (DbError ex) {
                    throw new GraphQLException(ex.getMessage());
                }
            }
        };
    }

    public GraphQLObjectType getUserType() {
        return this.userType;
    }

    public GraphQLObjectType getWorkExperienceType() {
        return this.workExperienceType;
    }

    public GraphQLObjectType getAwardType() {
        return awardType;
    }

    public GraphQLObjectType getContactType() {
        return contactType;
    }

    public GraphQLObjectType getDevelopmentToolsSectionType() {
        return developmentToolsSectionType;
    }

    public GraphQLObjectType getEducationType() {
        return educationType;
    }

    public GraphQLObjectType getProfessionalSkillsSectionType() {
        return professionalSkillsSectionType;
    }

    public GraphQLObjectType getReferenceType() {
        return referenceType;
    }

    public GraphQLObjectType getSkillType() {
        return skillType;
    }

    public GraphQLObjectType getSkillCategoryType() {
        return skillCategoryType;
    }

    public GraphQLObjectType basicInformationType() {
        return BasicInformationType;
    }

    public GraphQLSchema getSchema() {
        return this.schema;
    }
}
