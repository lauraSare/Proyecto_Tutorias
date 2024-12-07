package com.example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ActivityLogin extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button loginButton;
    private Retrofit retrofit;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        emailEditText = findViewById(R.id.editTextEmail);
        loginButton = findViewById(R.id.buttonLogin);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://your-server-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();

        JsonObject loginData = new JsonObject();
        loginData.addProperty("username", username);
        loginData.addProperty("password", password);
        loginData.addProperty("email", email);

        authService.login(loginData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean success = responseBody.get("success").getAsBoolean();
                    if (success) {
                        Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Redirect to another activity
                    } else {
                        String errorMessage = responseBody.get("message").getAsString();
                        Toast.makeText(ActivityLogin.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ActivityLogin.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



