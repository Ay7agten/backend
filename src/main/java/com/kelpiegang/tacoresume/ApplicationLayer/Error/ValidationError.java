package com.kelpiegang.tacoresume.ApplicationLayer.Error;

public class ValidationError extends Error {
    
     public ValidationError(String message) {
        super(message, "VALIDATION_ERROR", "Something went wrong!");
    }
    
}
