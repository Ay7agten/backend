package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.*;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;

public class TacoResumeSchemaMutations {

    private GraphQLObjectType updateUserMutation;
    private TacoResumeSchema tacoResumeSchema;
    private UserRepository userRepo;
    private Gson gson;

    public TacoResumeSchemaMutations(TacoResumeSchema tacoResumeSchema, UserRepository userRepo, Gson gson) {
        this.tacoResumeSchema = tacoResumeSchema;
        this.userRepo = userRepo;
        this.gson = gson;
        createUpdateUserMutation();
    }

    private void createUpdateUserMutation() {

        GraphQLInputObjectType userInputType = GraphQLInputObjectType.newInputObject()
                .name("UserInput")
                .field(newInputObjectField()
                        .name("_id")
                        .type(Scalars.GraphQLString).build())
                .field(newInputObjectField()
                        .name("name")
                        .type(Scalars.GraphQLString).build())
                .field(newInputObjectField()
                        .name("email")
                        .type(Scalars.GraphQLString).build())
                .field(newInputObjectField()
                        .name("gender")
                        .type(Scalars.GraphQLString)).build();

        updateUserMutation = newObject()
                .name("updateUserMutation")
                .field(newFieldDefinition()
                        .name("updateUser")
                        .type(tacoResumeSchema.getUserType())
                        .argument(newArgument()
                                .name("UserInput")
                                .type(userInputType)
                        )
                        .dataFetcher(mutationDataFetcher())
                )
                .build();
    }

    private DataFetcher mutationDataFetcher() {
        return new DataFetcher() {
            @Override
            public User get(DataFetchingEnvironment environment) {
                LinkedHashMap userInput = environment.getArgument("UserInput");
                userInput.put("_id", new ObjectId(userInput.get("_id").toString()));
                User user = gson.fromJson(gson.toJson(userInput), User.class);
                User updatedUser;
                try {

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
