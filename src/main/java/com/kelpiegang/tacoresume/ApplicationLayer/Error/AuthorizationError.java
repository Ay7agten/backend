package com.kelpiegang.tacoresume.ApplicationLayer.Error;


public class AuthorizationError extends Error {
    
    public AuthorizationError(String message) {
        super(message, "AUTHORIZATION_ERROR", "Something went wrong!");
    }
    
}
