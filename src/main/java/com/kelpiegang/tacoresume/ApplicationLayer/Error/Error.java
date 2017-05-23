package com.kelpiegang.tacoresume.ApplicationLayer.Error;

public abstract class Error extends Throwable {

    private String message;
    private String code;
    private String details;

    public Error(String message, String code, String details) {
        this.message = message;
        this.code = code;
        this.details = details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getDetails() {
        return details;
    }

}
