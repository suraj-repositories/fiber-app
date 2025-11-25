package com.oranbyte.fiber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private CardView btnChangePhoto;

    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = view.findViewById(R.id.img_profile);
        btnChangePhoto = view.findViewById(R.id.btn_change_photo);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Glide.with(this).load(uri).centerCrop().into(imgProfile);
                    }
                }
        );

        btnChangePhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        return view;
    }
}
