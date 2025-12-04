package com.oranbyte.fiber.services;

import android.content.Context;
import android.net.Uri;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;

import retrofit2.Callback;

public interface UserService {
    void login(String username, String password, Callback<LoginResponse> callback);
    void register(String name, String username, String password, Callback<ApiResponse> callback);
    void getUserProfile(Callback<ApiResponse> callback);
    void updateProfile(String name, String username, String email, Uri selectedImageUri, Context context, Callback<ApiResponse> callback);

    void updateKeyStatus(Callback<ApiResponse> callback);
}

