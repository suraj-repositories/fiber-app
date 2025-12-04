package com.oranbyte.fiber.services.impl;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.oranbyte.fiber.api.UserApi;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;
import com.oranbyte.fiber.network.RetrofitClient;
import com.oranbyte.fiber.services.SessionManager;
import com.oranbyte.fiber.services.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UserServiceImpl implements UserService {

    private final UserApi userApi;

    private final String authToken;

    public UserServiceImpl(Context context) {
        userApi = RetrofitClient.getInstance().create(UserApi.class);

        SessionManager session = new SessionManager(context);
        authToken = session.getToken();
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

    @Override
    public void getUserProfile(Callback<ApiResponse> callback) {
        Call<ApiResponse> call = userApi.getUserProfile("Bearer " + authToken);
        call.enqueue(callback);
    }

    @Override
    public void updateProfile(String name, String username, String email, Uri selectedImageUri,
                              Context context, Callback<ApiResponse> callback) {

        RequestBody rbUsername = RequestBody.create(username, MediaType.parse("text/plain"));
        RequestBody rbName     = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody rbEmail    = RequestBody.create(email, MediaType.parse("text/plain"));

        MultipartBody.Part imagePart = null;

        if (selectedImageUri != null) {
            try {
                InputStream is = context.getContentResolver().openInputStream(selectedImageUri);

                File tempFile = new File(context.getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");

                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[4096];
                int len;

                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                is.close();

                RequestBody reqFile = RequestBody.create(tempFile, MediaType.parse("image/jpeg"));


                imagePart = MultipartBody.Part.createFormData(
                        "image",
                        tempFile.getName(),
                        reqFile
                );

            } catch (Exception e) {
                Log.e("IMAGE_ERROR", "Image processing failed: " + e.getMessage());
            }
        }

        Call<ApiResponse> call = userApi.updateProfile(
                "Bearer " + authToken,
                rbUsername,
                rbName,
                rbEmail,
                imagePart
        );

        Log.d("API_DEBUG", "updateProfile() request sent");

        call.enqueue(callback);
    }

    @Override
    public void updateKeyStatus(Callback<ApiResponse> callback) {
        Call<ApiResponse> call = userApi.updateKeyStatusToCopied("Bearer " + authToken);
        call.enqueue(callback);
    }


}
