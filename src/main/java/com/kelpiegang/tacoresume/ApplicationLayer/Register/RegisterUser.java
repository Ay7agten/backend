package com.kelpiegang.tacoresume.ApplicationLayer.Register;

import com.kelpiegang.tacoresume.ApplicationLayer.Error.DbError;
import com.kelpiegang.tacoresume.ApplicationLayer.Factory.UserFactory;
import com.kelpiegang.tacoresume.DbLayer.UserRepository;
import com.kelpiegang.tacoresume.ModelLayer.User;

public class RegisterUser {

    private UserRepository userRepo;
    private UserFactory userFactory;

    public RegisterUser(UserRepository userRepo, UserFactory userFactory) {
        this.userRepo = userRepo;
        this.userFactory = userFactory;
    }

    public User registerNewFacebookUser(String facebookId) throws DbError {
        User user = userRepo.getByFacebookId(facebookId);
        if (user == null) {
            User newUser = userFactory.createFacebookUser(facebookId);
            return userRepo.add(newUser);
        } else {
            return user;
        }
    }

    public User registerNewGoogleUser(String googleId) throws DbError {
        User user = userRepo.getByGoogleId(googleId);
        if (user == null) {
            User newUser = userFactory.createGoogleUser(googleId);
            return userRepo.add(newUser);
        } else {
            return user;
        }
    }

    public User registerNewLinkedInUser(String linkedInId) throws DbError {
        User user = userRepo.getByLinkedInId(linkedInId);
        if (user == null) {
            User newUser = userFactory.createLinkedInUser(linkedInId);
            return userRepo.add(newUser);
        } else {
            return user;
        }
    }

}
