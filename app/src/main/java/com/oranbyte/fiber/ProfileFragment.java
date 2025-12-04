package com.oranbyte.fiber;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.Device;
import com.oranbyte.fiber.models.User;
import com.oranbyte.fiber.services.UserService;
import com.oranbyte.fiber.services.impl.UserServiceImpl;

import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private CardView btnChangePhoto;
    private EditText usernameInput, nameInput, emailInput;
    private ActivityResultLauncher<String> pickImageLauncher;
    private UserService userService;
    private final Gson gson = new Gson();
    private LinearLayout xApiKeyArea;
    private TextView xApiKey;
    private Button copyApiKeyBtn, updateProfileButton;
    private Uri selectedImageUri = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgProfile = view.findViewById(R.id.img_profile);
        btnChangePhoto = view.findViewById(R.id.btn_change_photo);

        usernameInput = view.findViewById(R.id.et_username);
        nameInput = view.findViewById(R.id.et_full_name);
        emailInput = view.findViewById(R.id.et_email_address);
        updateProfileButton = view.findViewById(R.id.btn_save_profile);
        updateProfileButton.setOnClickListener(v -> updateProfile());


        xApiKeyArea = view.findViewById(R.id.api_key_area);
        xApiKey = view.findViewById(R.id.api_key_txt);
        copyApiKeyBtn = view.findViewById(R.id.copy_button);
        xApiKeyArea.setVisibility(View.GONE);

        userService = new UserServiceImpl(requireContext());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;

                        Glide.with(requireActivity())
                                .load(uri)
                                .centerCrop()
                                .into(imgProfile);
                    }
                }
        );


        btnChangePhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        loadProfile();
    }

    private void updateProfile() {

        String username = usernameInput.getText().toString();
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();

        Log.d("UPLOAD_IMAGE_URI : ", selectedImageUri.toString());
        updateProfileButton.setEnabled(false);
        updateProfileButton.setText("Updating...");

        userService.updateProfile(name, username, email, selectedImageUri, requireContext(), new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                updateProfileButton.setEnabled(true);
                updateProfileButton.setText("Save Changes");
                Log.d("UPDATE_PROFILE_RESPONSE", response.body().toString());

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(requireContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                Log.d("PROFILE_UPDATED", "Success");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                updateProfileButton.setEnabled(true);
                updateProfileButton.setText("Save Changes");
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void loadProfile() {

        userService.getUserProfile(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (!isAdded()) return; // fragment must be attached

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Error: " + response.code());
                    return;
                }

                ApiResponse api = response.body();
                if (!api.isSuccess()) {
                    Toast.makeText(requireContext(), api.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = parseUser(api);

                if (user != null) {
                    usernameInput.setText(user.getUsername());
                    emailInput.setText(user.getEmail());
                    nameInput.setText(user.getName());

                    Glide.with(requireActivity())
                            .load(user.getImage())
                            .centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GLIDE_ERROR", "Failed to load image", e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target, DataSource dataSource,
                                                               boolean isFirstResource) {
                                    Log.d("GLIDE_SUCCESS", "Image loaded!");
                                    return false;
                                }
                            })
                            .into(imgProfile);

                    Log.i("USER_PROFILE", user.toString());
                    if (user.getKeyVisibility() != null && user.getKeyVisibility().equals("visible")
                            && user.getApiKey() != null) {
                        xApiKey.setText(maskKey(user.getApiKey()));
                        xApiKeyArea.setVisibility(View.VISIBLE);

                        copyApiKeyBtn.setOnClickListener(view -> {
                            String realKey = user.getApiKey();

                            if (realKey != null && !realKey.isEmpty()) {
                                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("API Key", realKey);
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(requireActivity(), "API Key copied!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "No API Key available!", Toast.LENGTH_SHORT).show();
                            }

                            userService.updateKeyStatus(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("STATUS_UPDATE_SUCCESS", "Api key status update successfull!");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                                    Log.d("STATUS_UPDATE_FAILED", throwable.getMessage());
                                }
                            });
                        });
                    }


                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private User parseUser(ApiResponse api) {
        if (api.getData() == null) return null;
        String json = gson.toJson(api.getData());
        Log.d("USER-JSON", json);
        return gson.fromJson(json, User.class);
    }

    public String maskKey(String key) {
        if (key == null || key.length() < 6) return "*********************";

        int visibleStart = 6;
        int visibleEnd = 4;

        String start = key.substring(0, visibleStart);
        String end = key.substring(key.length() - visibleEnd);

        return start + "*********************" + end;
    }

}
