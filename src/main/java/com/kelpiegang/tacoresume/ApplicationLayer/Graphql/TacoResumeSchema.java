package com.kelpiegang.tacoresume.ApplicationLayer.Graphql;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import graphql.schema.*;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import graphql.schema.GraphQLSchema;
import org.bson.types.ObjectId;

public class TacoResumeSchema {

    private GraphQLSchema schema;
    private GraphQLObjectType userType;
    private UserRepository userRepo;
    private Gson gson;

    public TacoResumeSchema(UserRepository userRepo, Gson gson) {
        this.userRepo = userRepo;
        this.gson = gson;
        createSchema();
    }

    private void createSchema() {

        createUserType();

        DataFetcher<User> userDataFetcher = new DataFetcher<User>() {
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

        GraphQLFieldDefinition user
                = GraphQLFieldDefinition
                        .newFieldDefinition()
                        .name("User")
                        .type(userType)
                        .dataFetcher(userDataFetcher)
                        .argument(newArgument().name("_id")
                                .type(GraphQLString).build())
                        .build();

        GraphQLObjectType rootQuery
                = GraphQLObjectType
                        .newObject()
                        .name("rootQuery")
                        .field(user)
                        .build();

        TacoResumeSchemaMutations tacoResumeSchemaMutations = new TacoResumeSchemaMutations(this, userRepo, gson);
        GraphQLObjectType updateUserMutation = tacoResumeSchemaMutations.getUpdateUserMutation();

        schema = GraphQLSchema.newSchema().query(rootQuery).mutation(updateUserMutation).build();
    }

    private void createUserType() {

        GraphQLFieldDefinition idField = newFieldDefinition()
                .name("_id")
                .type(GraphQLString).build();

        GraphQLFieldDefinition nameField = newFieldDefinition()
                .name("name")
                .type(GraphQLString).build();

        GraphQLFieldDefinition emailField = newFieldDefinition()
                .name("email")
                .type(GraphQLString).build();

        GraphQLFieldDefinition genderField = newFieldDefinition()
                .name("gender")
                .type(GraphQLString).build();

        GraphQLFieldDefinition birthDateField = newFieldDefinition()
                .name("birthDate")
                .type(GraphQLLong).build();

        this.userType = newObject()
                .name("User")
                .field(idField)
                .field(nameField)
                .field(emailField)
                .field(genderField)
                .field(birthDateField)
                .build();
    }

    public GraphQLObjectType getUserType() {
        return this.userType;
    }

    public GraphQLSchema getSchema() {
        return this.schema;
    }
}
