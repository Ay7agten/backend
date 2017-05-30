package com.kelpiegang.tacoresume.ApplicationLayer.Factory;

import com.kelpiegang.tacoresume.ModelLayer.User;

public class UserFactory {

    public User create(String facebookId) {
        return new User(facebookId);
    }

}
