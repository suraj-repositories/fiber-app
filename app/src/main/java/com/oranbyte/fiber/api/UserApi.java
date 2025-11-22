package com.oranbyte.fiber.api;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("user/login.php")
    Call<LoginResponse> login(@Body Map<String, String> body);

    @POST("user/register.php")
    Call<ApiResponse> register(@Body Map<String, String> body);
}
