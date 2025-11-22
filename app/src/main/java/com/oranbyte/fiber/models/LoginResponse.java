package com.oranbyte.fiber.models;

public class LoginResponse {
    public boolean success;
    public String message;
    public String token;
    public User user;

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


}

