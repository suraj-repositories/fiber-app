package com.oranbyte.fiber.services;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;

import retrofit2.Callback;

public interface UserService {
    void login(String username, String password, Callback<LoginResponse> callback);
    void register(String name, String username, String password, Callback<ApiResponse> callback);
}

