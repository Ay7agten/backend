package com.kelpiegang.tacoresume.ApplicationLayer.Factory;

import com.kelpiegang.tacoresume.ModelLayer.User;

public class UserFactory {

    public User createFacebookUser(String facebookId) {
        return new User(facebookId, "facebook");
    }

    public User createGoogleUser(String googleId) {
        return new User(googleId, "google");
    }

    public User createLinkedInUser(String linkedInId) {
        return new User(linkedInId, "linkedIn");
    }

}
