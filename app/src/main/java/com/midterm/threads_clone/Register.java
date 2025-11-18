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

import com.midterm.threads_clone.api_service.ApiClient;
import com.midterm.threads_clone.api_service.ApiService;
import com.midterm.threads_clone.auth.ClassicResponse;
import com.midterm.threads_clone.auth.RegisterRequest;
import com.midterm.threads_clone.auth.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private TextView tvSignIn;
    private Button btnRegister;
    private EditText etEmail, etUsername, etPassword;
    private TextView tvErrorUsername, tvErrorPassword, tvErrorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvSignIn = findViewById(R.id.tvSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvErrorUsername = findViewById(R.id.tvErrorUsername);
        tvErrorPassword = findViewById(R.id.tvErrorPassword);
        tvErrorEmail = findViewById(R.id.tvErrorEmail);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent chuyển sang Activity SignupActivity
                Intent intent = new Intent(Register.this, PostRegister.class);
                startActivity(intent);
            }
        });
        tvErrorEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra khi EditText MẤT focus (hasFocus == false)
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra khi EditText MẤT focus (hasFocus == false)
                if (!hasFocus) {
                    validatePassword();
                }
            }
        });
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra khi EditText MẤT focus (hasFocus == false)
                if (!hasFocus) {
                    validateUsername();
                }
            }
        });
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etPassword.getText().toString().trim();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Kiểm tra trường rỗng
                Toast.makeText(this, "Vui lòng điền đầy đủ tất cả các trường.", Toast.LENGTH_SHORT).show();
                return; // Dừng lại, không gửi request
            }

            if (!isValidEmail(email)) {
                // Kiểm tra định dạng email (cần triển khai hàm isValidEmail)
                Toast.makeText(this, "Địa chỉ email không hợp lệ.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (username.length() < 6) {
                // Kiểm tra độ dài mật khẩu tối thiểu
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                // Kiểm tra độ dài mật khẩu tối thiểu
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest request = new RegisterRequest(email, username, password, confirmPassword);

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            Call<RegisterResponse> call = api.register(request);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse res = response.body();

                        Toast.makeText(Register.this, "Đăng ký thành công: " + res.getData().getUsername(), Toast.LENGTH_LONG).show();

                        // Chuyển sang LoginActivity (hoặc MainPage)
                        startActivity(new Intent(Register.this, Login.class));
                        finish();

                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                Log.e("REGISTER_ERROR", errorJson);
                                Toast.makeText(Register.this, "Đăng ký thất bại: " + errorJson, Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("REGISTER_ERROR", "Unknown error");
                            }
                        } catch (Exception e) {
                            Log.e("REGISTER_ERROR", "Error parsing errorBody: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(Register.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("API_ERROR", "Lỗi: " + t.getMessage());

                }
            });
        });

    }
    private void validateEmail() {
        String email = etEmail.getText().toString();

        if (email.length() < 8) {
            // Trường hợp lỗi: ít hơn 6 ký tự
            tvErrorEmail.setVisibility(View.VISIBLE); // Hiện thông báo lỗi
        } else {
            // Trường hợp hợp lệ
            tvErrorEmail.setVisibility(View.GONE); // Ẩn thông báo lỗi
        }
    }
    private void validatePassword() {
        String password = etPassword.getText().toString();

        if (password.length() < 8) {
            // Trường hợp lỗi: ít hơn 6 ký tự
            tvErrorPassword.setVisibility(View.VISIBLE); // Hiện thông báo lỗi
        } else {
            // Trường hợp hợp lệ
            tvErrorPassword.setVisibility(View.GONE); // Ẩn thông báo lỗi
        }
    }
    private void validateUsername() {
        String username = etUsername.getText().toString();

        if (username.length() < 6) {
            // Trường hợp lỗi: ít hơn 6 ký tự
            tvErrorUsername.setVisibility(View.VISIBLE); // Hiện thông báo lỗi
        } else {
            // Trường hợp hợp lệ
            tvErrorUsername.setVisibility(View.GONE); // Ẩn thông báo lỗi
        }
    }
    private boolean isValidEmail(CharSequence target) {
        // Sử dụng Android's built-in Patterns.EMAIL_ADDRESS
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}