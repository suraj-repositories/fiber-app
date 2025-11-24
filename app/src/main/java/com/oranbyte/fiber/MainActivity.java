package com.oranbyte.fiber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oranbyte.fiber.models.LoginResponse;
import com.oranbyte.fiber.models.LoginResponse;
import com.oranbyte.fiber.services.SessionManager;
import com.oranbyte.fiber.services.UserService;
import com.oranbyte.fiber.services.impl.UserServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    TextView createNewAccount;
    EditText usernameInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SessionManager session = new SessionManager(this);
        String token = session.getToken();

        if (token != null && !token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ControlPanel.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        createNewAccount = findViewById(R.id.createNewAccount);
        createNewAccount.setOnClickListener(view -> {
            Intent loginIntent = new Intent(this, SignUpActivity.class);
            startActivity(loginIntent);
        });

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        UserService userService = new UserServiceImpl();

        loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(e -> {

            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            userService.login(username, password, new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Server error " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LoginResponse res = response.body();
                    if (res == null) {
                        Toast.makeText(MainActivity.this, "Empty server response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    session.saveToken(res.getToken());

                    Toast.makeText(MainActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, ControlPanel.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
