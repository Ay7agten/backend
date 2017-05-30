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

    public User registerNewUser(String facebookId) throws DbError {
        User user = userRepo.getByFacebookId(facebookId);
        if (user == null) {
            User newUser = userFactory.create(facebookId);
            return userRepo.add(newUser);
        } else {
            return user;
        }
    }

}
