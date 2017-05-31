package com.kelpiegang.tacoresume.ApplicationLayer.SocialMedia.Google;

public class AccessToken {

    private String access_token;
    private Long expires_in;
    private String id_token;
    private String token_type;

    public AccessToken(String access_token, Long expires_in, String id_token, String token_type) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.id_token = id_token;
        this.token_type = token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public Long getExpiresIn() {
        return expires_in;
    }

    public String getIdToken() {
        return id_token;
    }

    public String getTokenType() {
        return token_type;
    }

}
