package com.midterm.threads_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.midterm.threads_clone.User.MeResponse;
import com.midterm.threads_clone.User.UserManager;
import com.midterm.threads_clone.api_service.ApiClient;
import com.midterm.threads_clone.api_service.ApiService;
import com.midterm.threads_clone.api_service.TokenManager;
import com.midterm.threads_clone.auth.VerifyEmailRequest;
import com.midterm.threads_clone.auth.VerifyEmailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        if (data != null && data.getQueryParameter("token") != null) {
            String token = data.getQueryParameter("token");
            callVerifyEmailApi(token);
        }
    }

    private void callVerifyEmailApi(String token) {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        Call<VerifyEmailResponse> call = api.verifyEmail(token);
        call.enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    VerifyEmailResponse res = response.body();
                    String access = res.getData().getAccessToken();
                    String refresh = res.getData().getRefreshToken();

                    TokenManager tokenManager = TokenManager.getInstance(VerifyEmailActivity.this);
                    tokenManager.saveTokens(access, refresh);
                    Log.e("VerifyEmail", "access = " + access);
                    Log.e("VerifyEmail", "refresh = " + refresh);
                    Toast.makeText(VerifyEmailActivity.this, "Xác thực thành công!", Toast.LENGTH_LONG).show();
                    ApiService api = ApiClient.getRetrofitInstance().create(ApiService.class);
                    api.getMe().enqueue(new Callback<MeResponse>() {
                        @Override
                        public void onResponse(Call<MeResponse> call, Response<MeResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                MeResponse me = response.body();
                                UserManager userManager = UserManager.getInstance(VerifyEmailActivity.this);
                                // Lưu thông tin user vào SharedPreferences hoặc TokenManager
                                userManager.saveUser(me.getData());

                                Log.e("VerifyEmail", "UserId = " + me.getData().getUserId());
                                Log.e("VerifyEmail", "Username = " + me.getData().getUsername());

                                // Chuyển thẳng vào MainPage
                                startActivity(new Intent(VerifyEmailActivity.this, MainPage.class));
                                finish();
                            } else {
                                Log.e("VerifyEmail", "Lỗi gọi /me: " + response.code());
                                Toast.makeText(VerifyEmailActivity.this, "Không thể lấy thông tin user!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MeResponse> call, Throwable t) {
                            Log.e("VerifyEmail", "Lỗi gọi /me", t);
                            Toast.makeText(VerifyEmailActivity.this, "Không thể kết nối server!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(VerifyEmailActivity.this, MainPage.class));
                    finish();

                } else {
                    // IN RA LỖI CHI TIẾT
                    try {
                        String errorJson = response.errorBody() != null
                                ? response.errorBody().string()
                                : "null";
                        Log.e("VerifyEmail", "❌ Lỗi API");
                        String token = getIntent().getData().getQueryParameter("token");
                        Log.e("VerifyEmail", "TOKEN = " + token);
                        Log.e("VerifyEmail", "Code: " + response.code());
                        Log.e("VerifyEmail", "Message: " + response.message());
                        Log.e("VerifyEmail", "Error body: " + errorJson);

                        Toast.makeText(
                                VerifyEmailActivity.this,
                                "Xác thực thất bại: " + response.code(),
                                Toast.LENGTH_LONG
                        ).show();

                    } catch (Exception e) {
                        Log.e("VerifyEmail", "❌ Lỗi parse errorBody: " + e.getMessage());
                    }

                    finish();
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                Toast.makeText(VerifyEmailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
