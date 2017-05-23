package com.kelpiegang.tacoresume.ApplicationLayer.Error;

public class DbError extends Error {

    public DbError(String message) {
        super(message, "DB_ERROR", "Something went wrong!");
    }

}
