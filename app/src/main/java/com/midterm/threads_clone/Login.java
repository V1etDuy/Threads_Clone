package com.midterm.threads_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.midterm.threads_clone.User.MeResponse;
import com.midterm.threads_clone.User.UserInfo;
import com.midterm.threads_clone.User.UserManager;
import com.midterm.threads_clone.api_service.ApiClient;
import com.midterm.threads_clone.api_service.ApiService;
import com.midterm.threads_clone.api_service.TokenManager;
import com.midterm.threads_clone.auth.ClassicResponse;
import com.midterm.threads_clone.auth.SignInRequest;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private ApiService apiService;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvError, tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvError = findViewById(R.id.tvError);
        tvSignup = findViewById(R.id.tvSignup);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent chuyển sang Activity SignupActivity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // 1️⃣ Kiểm tra đầu vào
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2️⃣ Tạo request body
            SignInRequest request = new SignInRequest(email, password);


            Call<ClassicResponse> call = apiService.signIn(request);

            call.enqueue(new Callback<ClassicResponse>() {
                @Override
                public void onResponse(Call<ClassicResponse> call, Response<ClassicResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ClassicResponse res = response.body();
                        String access = res.getData().getAccessToken();
                        String refresh = res.getData().getRefreshToken();

                        // Lưu cả access + refresh token
                        TokenManager tokenManager = TokenManager.getInstance(Login.this);
                        tokenManager.saveTokens(access, refresh);
                        Log.e("Login", "access: " + tokenManager.getAccessToken());
                        Log.e("Login", "refresh: " + tokenManager.getRefreshToken());
                        ApiService api = ApiClient.getRetrofitInstance().create(ApiService.class);
                        api.getMe().enqueue(new Callback<MeResponse>() {
                            @Override
                            public void onResponse(Call<MeResponse> call, Response<MeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    MeResponse me = response.body();
                                    UserManager userManager = UserManager.getInstance(Login.this);
                                    // Lưu thông tin user vào SharedPreferences hoặc TokenManager
                                    userManager.saveUser(me.getData());
                                    UserInfo u = userManager.getUser();

                                    Log.e("Login", "UserId = " + me.getData().getUserId());
                                    Log.e("Login", "Username = " + me.getData().getUsername());
                                    Log.e("Login", "UserId u = " + u.getUserId());
                                    Log.e("Login", "Username u = " + u.getUsername());

                                    // Chuyển thẳng vào MainPage
                                    startActivity(new Intent(Login.this, MainPage.class));
                                    finish();
                                } else {
                                    // Log chi tiết lỗi
                                    try {
                                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";

                                        Log.e("Auth", "Lỗi gọi /me: " + response.code());
                                        Log.e("Auth", "Message: " + response.message());
                                        Log.e("Auth", "Error body: " + errorBody);
                                    } catch (Exception e) {
                                        Log.e("Auth", "Không đọc được errorBody", e);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MeResponse> call, Throwable t) {
                                Log.e("VerifyEmail", "Lỗi gọi /me", t);
                                Toast.makeText(Login.this, "Không thể kết nối server!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String errorStr = response.errorBody() != null ? response.errorBody().string() : "";
                            String errorMessage = "Invalid email or password";

                            if (!errorStr.isEmpty()) {
                                JSONObject json = new JSONObject(errorStr);
                                errorMessage = json.optString("error", errorMessage);
                            }

                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText(errorMessage);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Server response error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClassicResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "Connection failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Login_Activity", "onFailure: " + t.getMessage());
                }
            });
        });



    }
}