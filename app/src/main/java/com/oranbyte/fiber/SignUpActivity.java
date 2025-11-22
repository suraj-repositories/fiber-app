package com.oranbyte.fiber;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oranbyte.fiber.models.ApiResponse;
import com.oranbyte.fiber.models.LoginResponse;
import com.oranbyte.fiber.services.UserService;
import com.oranbyte.fiber.services.impl.UserServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    TextView alreadyHaveAnAccount;
    Button signupButton;
    EditText nameInput, usernameInput, passwordInput;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.name);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        userService = new UserServiceImpl();

        alreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAccount);
        alreadyHaveAnAccount.setOnClickListener(view -> {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
        });

        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            userService.register(name, username, password , new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Server error " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiResponse res = response.body();
                    if (res == null) {
                        Toast.makeText(SignUpActivity.this, "Empty server response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(SignUpActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });



    }
}