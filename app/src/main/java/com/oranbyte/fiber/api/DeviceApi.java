package com.oranbyte.fiber.api;

import com.oranbyte.fiber.models.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeviceApi {
    @GET("device/types.php")
    Call<ApiResponse> getDeviceTypes(@Header("Authorization") String token);

    @GET("device/get.php")
    Call<ApiResponse> getDevices(@Header("Authorization") String token);

    @POST("/device/setvalue.php")
    Call<ApiResponse> updateDeviceValue(@Header("Authorization") String token, @Body Map<String, Object> body);

}

