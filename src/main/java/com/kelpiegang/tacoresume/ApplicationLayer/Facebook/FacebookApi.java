package com.kelpiegang.tacoresume.ApplicationLayer.Facebook;

import com.google.gson.Gson;
import com.restfb.types.User;
import com.kelpiegang.tacoresume.Utility.HttpRequest;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;

public class FacebookApi {

    private String appId = "";
    private String domain;
    private String scope = "email";
    private String appSecret = "";
    private String responseType = "code";

    private HttpRequest httpRequest;
    private Gson gson;

    public FacebookApi(HttpRequest httpRequest, Gson gson, String backendServerUrl) {

        this.domain = backendServerUrl + "/api/fb-redirect";
        this.gson = gson;
        this.httpRequest = httpRequest;

    }

    public String authenticate() {
        return "https://www.facebook.com/v2.8/dialog/oauth?client_id=" + this.appId + "&redirect_uri=" + this.domain + "&scope=" + this.scope + "&response_type=" + this.responseType;
    }

    public String getFbToken(String code) throws Exception {
        String accessTokenRequest = "https://graph.facebook.com/v2.8/oauth/access_token?client_id=" + this.appId + "&redirect_uri=" + this.domain + "&client_secret=" + this.appSecret + "&code=" + code;
        String resBody = httpRequest.sendGet(accessTokenRequest);
        AccessToken accessToken = gson.fromJson(resBody, AccessToken.class);
        return accessToken.getAccessToken();
    }

    public String getFbUserID(String fbToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        return user.getId();
    }
}
