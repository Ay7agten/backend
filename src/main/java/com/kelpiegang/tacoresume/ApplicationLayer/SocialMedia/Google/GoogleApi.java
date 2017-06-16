package com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.Google;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.Utility.HttpRequest;

public class GoogleApi {

    private String appId = System.getenv("GOOGLE_APP_ID");
    private String domain;
    private String scope = "email";
    private String appSecret = System.getenv("GOOGLE_APP_SECRET");
    private String responseType = "code";
    private String approvalPrompt = "force";
    private String grantType = "authorization_code";

    private HttpRequest httpRequest;
    private Gson gson;

    public GoogleApi(HttpRequest httpRequest, Gson gson, String backendServerUrl) {

        this.domain = backendServerUrl + "/api/google-redirect";
        this.gson = gson;
        this.httpRequest = httpRequest;

    }

    public String authenticate() {
        return "https://accounts.google.com/o/oauth2/auth?scope=" + this.scope + "&redirect_uri=" + this.domain
                + "&response_type=" + this.responseType + "&client_id=" + this.appId + "&approval_prompt=" + this.approvalPrompt;
    }

    public String getGoogleToken(String code) throws Exception {
        String urlParameters = "code=" + code + "&client_id=" + this.appId
                + "&client_secret=" + this.appSecret + "&redirect_uri=" + this.domain + "&grant_type=" + this.grantType;
        String resBody = httpRequest.sendPost("https://accounts.google.com/o/oauth2/token", urlParameters);
        AccessToken accessToken = gson.fromJson(resBody, AccessToken.class);
        return accessToken.getAccessToken();
    }

    public String getGoogleUserId(String googleToken) throws Exception {
        String resBody = httpRequest.sendGet("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + googleToken);
        GoogleUserInfo googleUser = gson.fromJson(resBody, GoogleUserInfo.class);
        return googleUser.getId();
    }

}
