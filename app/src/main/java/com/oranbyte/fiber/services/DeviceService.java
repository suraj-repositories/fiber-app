package com.oranbyte.fiber.services;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.models.DeviceType;

import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public interface DeviceService {

    void getDeviceTypes(Callback<ApiResponse> callback);

    void getDeviceIcons(Callback<ApiResponse> callback);

    void getDevices(Callback<ApiResponse> callback);

    void updateDeviceValue(String key, String value, Callback<ApiResponse> callback);

    void getDevice(String deviceKey, Callback<ApiResponse> callback);

    void createDevice(Device device, Callback<ApiResponse> callback);



}
