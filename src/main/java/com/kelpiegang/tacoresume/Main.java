package com.kelpiegang.tacoresume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kelpiegang.tacoresume.ApplicationLayer.Authentication.JwtAuthentication;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.AuthenticationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.AuthorizationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.ValidationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Facebook.FacebookApi;
import com.kelpiegang.tacoresume.ApplicationLayer.Factory.UserFactory;
import com.kelpiegang.tacoresume.ApplicationLayer.Google.GoogleApi;
import com.kelpiegang.tacoresume.ApplicationLayer.Graphql.TacoResumeSchema;
import com.kelpiegang.tacoresume.ApplicationLayer.Register.RegisterUser;
import com.kelpiegang.tacoresume.DbLayer.MongoConfig;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.DbLayer.WorkExperienceRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;
import com.kelpiegang.tacoresume.Utility.HttpRequest;
import graphql.ExecutionResult;
import graphql.GraphQL;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        String backendServerUrl = "http://localhost:4567";
        String fronendServerUrl = "http://127.0.0.1:1111";

        HttpRequest httpRequest = new HttpRequest();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JwtAuthentication auth = new JwtAuthentication("secret");

        FacebookApi facebookApi = new FacebookApi(httpRequest, gson, backendServerUrl);
        GoogleApi googleApi = new GoogleApi(httpRequest, gson, backendServerUrl);

        MongoConfig mongoConfig = MongoConfig.getInstance();
        UserRepository userRepo = UserRepository.getInstance(mongoConfig.getDatastore());
        WorkExperienceRepository workExpRepo = WorkExperienceRepository.getInstance(mongoConfig.getDatastore());

        UserFactory userFactory = new UserFactory();
        RegisterUser regUser = new RegisterUser(userRepo, userFactory);

        TacoResumeSchema tacoResumeSchema = new TacoResumeSchema(userRepo, workExpRepo, gson);
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

        get("/api/facebook/login", (request, response) -> {
            response.redirect(facebookApi.authenticate());
            return null;
        });

        get("/api/fb-redirect", (request, response) -> {

            try {
                String code = request.queryParams("code");
                String facebookToken = facebookApi.getFacebookToken(code);
                String facebookId = facebookApi.getFacebookUserId(facebookToken);
                User user = regUser.registerNewFacebookUser(facebookId);
                response.redirect(fronendServerUrl + "?token=" + auth.getJwtToken(user));
                response.status(204);
                return "";
            } catch (AuthenticationError | DbError ex) {
                response.status(400);
                response.type("application/json");
                return gson.toJson(ex);
            }
        });

        get("/api/google/login", (request, response) -> {
            response.redirect(googleApi.authenticate());
            return null;
        });

        get("/api/google-redirect", (request, response) -> {

            try {
                String code = request.queryParams("code");
                String googleToken = googleApi.getGoogleToken(code);
                String googleId = googleApi.getGoogleUserId(googleToken);
                User user = regUser.registerNewGoogleUser(googleId);
                response.redirect(fronendServerUrl + "?token=" + auth.getJwtToken(user));
                response.status(204);
                return "";
            } catch (AuthenticationError | DbError ex) {
                response.status(400);
                response.type("application/json");
                return gson.toJson(ex);
            }
        });

        before("/graphql", (request, response) -> {
            try {
                String jwtToken = request.headers("Authorization");
                String userId = auth.verifyJwtToken(jwtToken);

                HashMap<String, Object> body = new ObjectMapper().readValue(request.body(), HashMap.class);
                String query = (String) body.get("query");
                Map<String, Object> variables = (Map<String, Object>) body.get("variables");

                if (!variables.isEmpty()) {
                    Map<String, Object> user = (Map<String, Object>) variables.get("user");
                    String _id = (String) user.get("_id");
                    if (!userId.equals(_id)) {
                        AuthorizationError e = new AuthorizationError("This user unauthorized to this action");
                        halt(401, gson.toJson(e));
                    }
                } else {
                    int index1 = query.indexOf("\"", 1) + 1;
                    String _id = query.substring(index1, index1 + 24);
                    if (!userId.equals(_id)) {
                        AuthorizationError e = new AuthorizationError("This user unauthorized to this action");
                        halt(401, gson.toJson(e));
                    }
                }

            } catch (AuthenticationError e) {
                halt(403, gson.toJson(e));
            }
        });
    }
}
