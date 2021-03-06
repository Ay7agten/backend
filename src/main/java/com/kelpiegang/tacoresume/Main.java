package com.kelpiegang.tacoresume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.kelpiegang.tacoresume.ApplicationLayer.Authentication.JwtAuthentication;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.AuthenticationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.AuthorizationError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.ValidationError;
import com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.Facebook.FacebookApi;
import com.kelpiegang.tacoresume.ApplicationLayer.Factory.UserFactory;
import com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.Google.GoogleApi;
import com.kelpiegang.tacoresume.ApplicationLayer.Graphql.TacoResumeSchema;
import com.kelpiegang.tacoresume.ApplicationLayer.GsonAdaptorFactory.LinkedHashMapAdapterFactory;
import com.kelpiegang.tacoresume.ApplicationLayer.Register.RegisterUser;
import com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.LinkedIn.LinkedInApi;
import com.kelpiegang.tacoresume.DbLayer.MongoConfig;
import com.kelpiegang.tacoresume.DbLayer.*;
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
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        enableCORS("*", "*", "*");

        String backendServerUrl = "https://tacoresume.herokuapp.com";
        String fronendServerUrl = "http://localhost:3000";

        HttpRequest httpRequest = new HttpRequest();
        Gson gsonWithAdaptor = new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(new LinkedHashMapAdapterFactory()).create();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JwtAuthentication auth = new JwtAuthentication("secret");

        FacebookApi facebookApi = new FacebookApi(httpRequest, gsonWithAdaptor, backendServerUrl);
        GoogleApi googleApi = new GoogleApi(httpRequest, gsonWithAdaptor, backendServerUrl);
        LinkedInApi linkedInApi = new LinkedInApi(httpRequest, gsonWithAdaptor, backendServerUrl);

        MongoConfig mongoConfig = MongoConfig.getInstance();
        UserRepository userRepo = UserRepository.getInstance(mongoConfig.getDatastore());
        WorkExperienceRepository workExpRepo = WorkExperienceRepository.getInstance(mongoConfig.getDatastore());
        AwardRepository awardRepo = AwardRepository.getInstance(mongoConfig.getDatastore());
        ContactRepository contactRepo = ContactRepository.getInstance(mongoConfig.getDatastore());
        DevelopmentToolsSectionRepository developmentToolsSectionRepo = DevelopmentToolsSectionRepository.getInstance(mongoConfig.getDatastore());
        EducationRepository educationRepo = EducationRepository.getInstance(mongoConfig.getDatastore());
        ProfessionalSkillsSectionRepository professionalSkillsSectionRepo = ProfessionalSkillsSectionRepository.getInstance(mongoConfig.getDatastore());
        ReferenceRepository referenceRepo = ReferenceRepository.getInstance(mongoConfig.getDatastore());
        SkillCategoryRepository skillCategoryRepo = SkillCategoryRepository.getInstance(mongoConfig.getDatastore());
        SkillRepository skillRepo = SkillRepository.getInstance(mongoConfig.getDatastore());
        BasicInformationRepository basicInfoRepo = BasicInformationRepository.getInstance(mongoConfig.getDatastore());

        UserFactory userFactory = new UserFactory();
        RegisterUser regUser = new RegisterUser(userRepo, userFactory);

        TacoResumeSchema tacoResumeSchema = new TacoResumeSchema(userRepo, workExpRepo, awardRepo, contactRepo, developmentToolsSectionRepo,
                educationRepo, professionalSkillsSectionRepo, referenceRepo, skillCategoryRepo, skillRepo, basicInfoRepo, gsonWithAdaptor);
        GraphQL graphql = new GraphQL(tacoResumeSchema.getSchema());

        get("/api/hello-world", (request, response) -> {
            return "Hello World!";
        });

        before("/graphql", (request, response) -> {
            if (!request.requestMethod().equals("OPTIONS")) {
                try {

                    String jwtToken = request.headers("Authorization");
                    String userId = auth.verifyJwtToken(jwtToken);
                    request.attribute("userId", userId);

                } catch (AuthenticationError e) {
                    halt(403, gson.toJson(e));
                }
            }
        });

        post("/graphql", (request, response) -> {

            try {
                HashMap<String, Object> body = new ObjectMapper().readValue(request.body(), HashMap.class);
                String query = (String) body.get("query");
                Map<String, Object> variables = (Map<String, Object>) body.get("variables");
                if (variables == null) {
                    variables = new HashMap<String, Object>();
                }

                ExecutionResult executionResult = graphql.execute(query, (Object) request.attribute("userId"), variables);
                Map<String, Object> result = new LinkedHashMap<>();
                if (executionResult.getErrors().size() > 0) {
                    result.put("errors", executionResult.getErrors());
                }
                result.put("data", executionResult.getData());
                response.type("application/json");
                return new ObjectMapper().writeValueAsString(result);
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

        get("/api/linkedin/login", (request, response) -> {
            response.redirect(linkedInApi.authenticate());
            return null;
        });

        get("/api/linkedin-redirect", (request, response) -> {

            try {
                String code = request.queryParams("code");
                String linkedInToken = linkedInApi.getLinkedInToken(code);
                String linkedInId = linkedInApi.getLinkedInUserId(linkedInToken);
                User user = regUser.registerNewLinkedInUser(linkedInId);
                response.redirect(fronendServerUrl + "?token=" + auth.getJwtToken(user));
                response.status(204);
                return "";
            } catch (AuthenticationError | DbError ex) {
                response.status(400);
                response.type("application/json");
                return gson.toJson(ex);
            }
        });
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);

            }

            return "OK";
        });

        before((request, response) -> {

            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
