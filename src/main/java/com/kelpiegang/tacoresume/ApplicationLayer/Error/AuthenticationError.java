package com.kelpiegang.tacoresume.ApplicationLayer.Error;

public class AuthenticationError extends Error {

    public AuthenticationError(String message) {
        super(message, "AUTHENTICATION_ERROR", "Something went wrong!");
    }

}
