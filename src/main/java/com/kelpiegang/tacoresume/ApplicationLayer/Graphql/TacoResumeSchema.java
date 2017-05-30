package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.DbLayer.WorkExperienceRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;
import com.kelpiegang.tacoresume.ModelLayer.WorkExperience;
import static graphql.Scalars.GraphQLLong;
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
    private UserRepository userRepo;
    private WorkExperienceRepository workExpRepo;
    private Gson gson;

    public TacoResumeSchema(UserRepository userRepo, WorkExperienceRepository workExpRepo, Gson gson) {
        this.userRepo = userRepo;
        this.workExpRepo = workExpRepo;
        this.gson = gson;
        createSchema();
    }

    private void createSchema() {

        createWorkExperienceType();
        createUserType();

        GraphQLFieldDefinition user
                = GraphQLFieldDefinition
                        .newFieldDefinition()
                        .name("User")
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

        TacoResumeSchemaMutations tacoResumeSchemaMutations = new TacoResumeSchemaMutations(this, userRepo, workExpRepo, gson);
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

        GraphQLFieldDefinition nameField = newFieldDefinition()
                .name("name")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition aboutField = newFieldDefinition()
                .name("about")
                .type(GraphQLString).build();

        GraphQLFieldDefinition phoneNumberField = newFieldDefinition()
                .name("phoneNumber")
                .type(GraphQLString).build();

        GraphQLFieldDefinition jobTitleField = newFieldDefinition()
                .name("jobTitle")
                .type(GraphQLString).build();

        GraphQLFieldDefinition websiteField = newFieldDefinition()
                .name("website")
                .type(GraphQLString).build();

        GraphQLFieldDefinition workExperiencesField = newFieldDefinition()
                .name("workExperiences")
                .type(new GraphQLList(this.workExperienceType))
                .dataFetcher(workExperiencesDataFetcher())
                .build();

        this.userType = newObject()
                .name("User")
                .field(idField)
                .field(facebookIdField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(phoneNumberField)
                .field(jobTitleField)
                .field(websiteField)
                .field(workExperiencesField)
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

        GraphQLFieldDefinition startedAtField = newFieldDefinition()
                .name("startedAt")
                .type(GraphQLLong).build();

        GraphQLFieldDefinition endedAtField = newFieldDefinition()
                .name("endedAt")
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
                .field(startedAtField)
                .field(endedAtField)
                .field(dutiesField)
                .field(toolsField)
                .build();
    }

    private DataFetcher userDataFetcher() {

        return new DataFetcher<User>() {
            @Override
            public User get(DataFetchingEnvironment environment) {
                String id = environment.getArgument("_id");
                try {
                    return userRepo.getById(new ObjectId(id));
                } catch (DbError ex) {
                    System.out.println("Data base error");
                }
                return null;
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
                    System.out.println("Data base error");
                    return null;
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

    public GraphQLSchema getSchema() {
        return this.schema;
    }
}
