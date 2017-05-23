package com.kelpiegang.tacoresume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.ValidationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Graphql.TacoResumeSchema;
import com.kelpiegang.tacoresume.DbLayer.MongoConfig;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import graphql.ExecutionResult;
import graphql.GraphQL;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MongoConfig mongoConfig = MongoConfig.getInstance();
        UserRepository userRepo = UserRepository.getInstance(mongoConfig.getDatastore());
        TacoResumeSchema tacoResumeSchema = new TacoResumeSchema(userRepo, gson);
        GraphQL graphql = new GraphQL(tacoResumeSchema.getSchema());

        post("/graphql", (request, response) -> {

            try {
                HashMap<String, Object> body = new ObjectMapper().readValue(request.body(), HashMap.class);
                String query = (String) body.get("query");
                Map<String, Object> variables = (Map<String, Object>) body.get("variables");
                ExecutionResult executionResult = graphql.execute(query, (Object) null, variables);
                Map<String, Object> result = new LinkedHashMap<>();
                if (executionResult.getErrors().size() > 0) {
                    result.put("errors", executionResult.getErrors());
                }
                result.put("data", executionResult.getData());
                response.type("application/json");
                return gson.toJson(result);
            } catch (IOException ex) {
                response.type("application/json");
                return gson.toJson(new ValidationError(ex.getMessage()));
            } catch (Exception ex) {
                response.type("application/json");
                return gson.toJson(new ValidationError(ex.getMessage()));
            }
        });
    }
}
