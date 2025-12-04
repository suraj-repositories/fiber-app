package com.oranbyte.fiber.api;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApi {

    @POST("user/login.php")
    Call<LoginResponse> login(@Body Map<String, String> body);

    @POST("user/register.php")
    Call<ApiResponse> register(@Body Map<String, String> body);

    @GET("user/user_profile.php")
    Call<ApiResponse> getUserProfile(@Header("Authorization") String token);
    @Multipart
    @POST("user/update_profile.php")
    Call<ApiResponse> updateProfile(
            @Header("Authorization") String token,
            @Part("username") RequestBody username,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );

    @POST("user/update_key_status.php")
    Call<ApiResponse> updateKeyStatusToCopied(@Header("Authorization") String token);

}
