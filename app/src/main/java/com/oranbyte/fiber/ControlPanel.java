package com.oranbyte.fiber;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oranbyte.fiber.services.SessionManager;

public class ControlPanel extends AppCompatActivity {

    Button logoutButton;
    ImageView toolbarProfileImage;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(
                ContextCompat.getColor(this, R.color.background_light)
        );

        setContentView(R.layout.activity_control_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.app_name));

        ImageView userProfile = toolbar.findViewById(R.id.toolbar_user_profile);
        FloatingActionButton fab = findViewById(R.id.createDevice);
        toolbarProfileImage = findViewById(R.id.toolbar_user_profile);
        logoutButton = findViewById(R.id.logoutButton);
        sessionManager = new SessionManager(this);

        logoutButton.setVisibility(View.GONE);
        logoutButton.setOnClickListener(e->{
            sessionManager.logout();
            Intent intent = new Intent(ControlPanel.this, MainActivity.class);
            startActivity(intent);
            finish();
        });



        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {

            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.deviceControlFragment) {
                    fab.show();
                } else {
                   fab.hide();
                }

                if(destination.getId() == R.id.profileFragment){
                    logoutButton.setVisibility(View.VISIBLE);
                    userProfile.setVisibility(View.GONE);
                }else{
                    logoutButton.setVisibility(View.GONE);
                    userProfile.setVisibility(View.VISIBLE);
                }
            });

            fab.setOnClickListener(v -> navController.navigate(R.id.createDeviceFragment));

            userProfile.setOnClickListener(v -> navController.navigate(R.id.profileFragment));
        }

        Log.d("REGULAR_CHECKUP", "this part is running");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            return navHostFragment.getNavController().navigateUp() || super.onSupportNavigateUp();
        }

        return super.onSupportNavigateUp();
    }
}
