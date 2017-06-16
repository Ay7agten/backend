package com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.LinkedIn;

import com.google.gson.Gson;
import com.kelpiegang.tacoresume.Utility.HttpRequest;

public class LinkedInApi {

    private String appId = System.getenv("LINKEDIN_APP_ID");
    private String domain;
    private String scope = "r_basicprofile";
    private String appSecret = System.getenv("LINKEDIN_APP_SECRET");
    private String responseType = "code";
    private String state = "DCEeFWa45A55sdfKef424";
    private String grantType = "authorization_code";

    private HttpRequest httpRequest;
    private Gson gson;

    public LinkedInApi(HttpRequest httpRequest, Gson gson, String backendServerUrl) {

        this.domain = backendServerUrl + "/api/linkedin-redirect";
        this.gson = gson;
        this.httpRequest = httpRequest;

    }

    public String authenticate() {
        return "https://www.linkedin.com/oauth/v2/authorization?scope=" + this.scope + "&redirect_uri=" + this.domain
                + "&response_type=" + this.responseType + "&client_id=" + this.appId + "&state=" + this.state;
    }

    public String getLinkedInToken(String code) throws Exception {
        String urlParameters = "code=" + code + "&client_id=" + this.appId
                + "&client_secret=" + this.appSecret + "&redirect_uri=" + this.domain + "&grant_type=" + this.grantType;
        String resBody = httpRequest.sendPost("https://www.linkedin.com/oauth/v2/accessToken", urlParameters);
        AccessToken accessToken = gson.fromJson(resBody, AccessToken.class);
        return accessToken.getAccessToken();
    }

    public String getLinkedInUserId(String linkedInToken) throws Exception {
        String resBody = httpRequest.sendGetWithAuthHeader("https://api.linkedin.com/v1/people/~?format=json", "Bearer " + linkedInToken);
        LinkedInUserInfo linkedInUser = gson.fromJson(resBody, LinkedInUserInfo.class);
        return linkedInUser.getId();
    }

}
