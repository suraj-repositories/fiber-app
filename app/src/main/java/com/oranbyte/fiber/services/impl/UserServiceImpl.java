package com.oranbyte.fiber.services.impl;

import com.oranbyte.fiber.api.UserApi;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;
import com.oranbyte.fiber.network.RetrofitClient;
import com.oranbyte.fiber.services.UserService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class UserServiceImpl implements UserService {

    private final UserApi userApi;

    public UserServiceImpl() {
        userApi = RetrofitClient.getInstance().create(UserApi.class);

    }

    @Override
    public void login(String username, String password, Callback<LoginResponse> callback) {

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        Call<LoginResponse> call = userApi.login(body);
        call.enqueue(callback);
    }

    @Override
    public void register(String name, String username, String password, Callback<ApiResponse> callback) {

        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("username", username);
        body.put("password", password);

        Call<ApiResponse> call = userApi.register(body);
        call.enqueue(callback);
    }
}
