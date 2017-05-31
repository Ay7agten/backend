package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ApplicationLayer.GsonInput.WorkExperiencesInput;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.DbLayer.WorkExperienceRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;
import com.kelpiegang.tacoresume.ModelLayer.WorkExperience;
import graphql.Scalars;
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
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;

public class TacoResumeSchemaMutations {

    private GraphQLObjectType updateUserMutation;
    private TacoResumeSchema tacoResumeSchema;
    private UserRepository userRepo;
    private WorkExperienceRepository workExpRepo;
    private Gson gson;

    public TacoResumeSchemaMutations(TacoResumeSchema tacoResumeSchema, UserRepository userRepo, WorkExperienceRepository workExpRepo, Gson gson) {
        this.tacoResumeSchema = tacoResumeSchema;
        this.userRepo = userRepo;
        this.workExpRepo = workExpRepo;
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
                        .dataFetcher(updateUsermutationDataFetcher())
                )
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

        GraphQLInputObjectField phoneNumberField = newInputObjectField()
                .name("phoneNumber")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField jobTitleField = newInputObjectField()
                .name("jobTitle")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField websiteField = newInputObjectField()
                .name("website")
                .type(Scalars.GraphQLString).build();

        GraphQLInputObjectField workExperiencesField = newInputObjectField()
                .name("workExperiences")
                .type(new GraphQLList(createWorkExperienceInputType())).build();

        GraphQLInputObjectType userInputType = GraphQLInputObjectType.newInputObject()
                .name("UserInput")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(aboutField)
                .field(phoneNumberField)
                .field(jobTitleField)
                .field(websiteField)
                .field(workExperiencesField)
                .build();

        return userInputType;
    }

    private GraphQLInputObjectType createWorkExperienceInputType() {

        GraphQLInputObjectField titleField = newInputObjectField()
                .name("title")
                .type(GraphQLString).build();

        GraphQLInputObjectField companyField = newInputObjectField()
                .name("company")
                .type(GraphQLString).build();

        GraphQLInputObjectField startedAtField = newInputObjectField()
                .name("startedAt")
                .type(GraphQLLong).build();

        GraphQLInputObjectField endedAtField = newInputObjectField()
                .name("endedAt")
                .type(GraphQLLong).build();

        GraphQLInputObjectField dutiesField = newInputObjectField()
                .name("duties")
                .type(new GraphQLList(GraphQLString)).build();

        GraphQLInputObjectField toolsField = newInputObjectField()
                .name("tools")
                .type(new GraphQLList(GraphQLString)).build();

        GraphQLInputObjectType workExperienceInputType = GraphQLInputObjectType.newInputObject()
                .name("WorkExperienceInput")
                .field(titleField)
                .field(companyField)
                .field(startedAtField)
                .field(endedAtField)
                .field(dutiesField)
                .field(toolsField)
                .build();

        return workExperienceInputType;

    }

    private DataFetcher updateUsermutationDataFetcher() {
        return new DataFetcher() {
            @Override
            public User get(DataFetchingEnvironment environment) {
                LinkedHashMap userInput = environment.getArgument("UserInput");
                userInput.put("_id", new ObjectId(userInput.get("_id").toString()));
                User user = gson.fromJson(gson.toJson(userInput), User.class);

                User updatedUser;
                try {

                    if (userInput.get("workExperiences") != null) {
                        WorkExperiencesInput workExperiencesInput = gson.fromJson(gson.toJson(userInput), WorkExperiencesInput.class);
                        for (WorkExperience workExperience : workExperiencesInput.getWorkExperiences()) {
                            workExperience.setUser(user);
                        }
                        if (workExperiencesInput.getWorkExperiences().isEmpty()) {
                            workExpRepo.removeAllByUser(user);
                        } else {
                            workExpRepo.update(workExperiencesInput.getWorkExperiences());
                        }

                    }

                    updatedUser = userRepo.update(user);
                    return updatedUser;
                } catch (DbError ex) {
                    System.out.println(ex.getMessage());
                    return null;
                }

            }
        };
    }

    public GraphQLObjectType getUpdateUserMutation() {
        return this.updateUserMutation;
    }
}
