package com.oranbyte.fiber.services.impl;

import android.content.Context;

import com.oranbyte.fiber.api.DeviceApi;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.network.RetrofitClient;
import com.oranbyte.fiber.services.DeviceService;
import com.oranbyte.fiber.services.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class DeviceServiceImpl implements DeviceService {

    private final DeviceApi deviceApi;
    private final String authToken;

    public DeviceServiceImpl(Context context) {
        deviceApi = RetrofitClient.getInstance().create(DeviceApi.class);

        SessionManager session = new SessionManager(context);
        authToken = session.getToken();
    }


    @Override
    public void getDeviceTypes(Callback<ApiResponse> callback) {

        Call<ApiResponse> call = deviceApi.getDeviceTypes(
                "Bearer " + authToken
        );

        call.enqueue(callback);
    }

    @Override
    public void getDeviceIcons(Callback<ApiResponse> callback) {
        Call<ApiResponse> call = deviceApi.getDeviceIcons(
                "Bearer " + authToken
        );

        call.enqueue(callback);
    }

    @Override
    public void getDevices(Callback<ApiResponse> callback) {

        Call<ApiResponse> call = deviceApi.getDevices(
                "Bearer " + authToken
        );

        call.enqueue(callback);
    }

    @Override
    public void updateDeviceValue(String key, String value, Callback<ApiResponse> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("device_key", key);
        body.put("value", value);

        Call<ApiResponse> call = deviceApi.updateDeviceValue(
                "Bearer " + authToken,
                body
        );
        call.enqueue(callback);
    }

    @Override
    public void getDevice(String deviceKey, Callback<ApiResponse> callback) {
        Call<ApiResponse> call = deviceApi.getDevice(
                "Bearer " + authToken,
                deviceKey
        );

        call.enqueue(callback);
    }

    @Override
    public void createDevice(Device device, Callback<ApiResponse> callback) {
        Call<ApiResponse> call = deviceApi.createDevice(
                "Bearer " + authToken,
                device
        );
        call.enqueue(callback);
    }


}
