package com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.LinkedIn;

public class AccessToken {

    private String access_token;
    private Long expires_in;

    public AccessToken(String access_token, Long expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public String getAccessToken() {
        return access_token;
    }

    public Long getExpiresIn() {
        return expires_in;
    }

}
