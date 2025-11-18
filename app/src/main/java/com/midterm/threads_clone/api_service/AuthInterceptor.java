package com.midterm.threads_clone.api_service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.midterm.threads_clone.Login;
import com.midterm.threads_clone.auth.ClassicResponse;
import com.midterm.threads_clone.auth.RefreshRequest;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        TokenManager tokenManager = TokenManager.getInstance(context);

        String accessToken = tokenManager.getAccessToken();
        String refreshToken = tokenManager.getRefreshToken();

        Request originalRequest = chain.request();

        // 1️⃣ Kiểm tra header dummy "Authorized" → API cần token mới gắn
        String requiresAuth = originalRequest.header("Authorized");
        if (requiresAuth == null || !requiresAuth.equals("true")) {
            return chain.proceed(originalRequest); // API public → bỏ qua
        }

        // 2️⃣ Nếu chưa có access token → gửi request bình thường
        if (accessToken == null) {
            return chain.proceed(originalRequest);
        }

        Log.e("AuthInterceptor", "Original URL: " + originalRequest.url());
        Log.e("AuthInterceptor", "Original Method: " + originalRequest.method());

        // 3️⃣ Gắn access token vào request
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .removeHeader("Authorized") // loại bỏ header dummy
                .build();

        okhttp3.Response response = chain.proceed(newRequest);

        // 4️⃣ Nếu token hết hạn → server trả 401
        if (response.code() == 401) {
            response.close();

            // Thử refresh token
            String newAccess = tryRefreshToken(refreshToken, tokenManager);

            if (newAccess != null) {
                // Gửi lại request mới với token mới
                Request retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + newAccess)
                        .removeHeader("Authorized")
                        .build();

                return chain.proceed(retryRequest);
            }

            // Refresh thất bại → logout an toàn
            logout("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
            throw new IOException("Token expired");
        }

        return response;
    }

    /**
     * Gọi API refresh token (Retrofit, đồng bộ)
     */
    private String tryRefreshToken(String refreshToken, TokenManager tokenManager) {
        if (refreshToken == null) return null;

        try {
            Retrofit retrofit = ApiClient.getRetrofitInstance();
            ApiService api = retrofit.create(ApiService.class);

            Call<ClassicResponse> call = api.refreshToken(new RefreshRequest(refreshToken));
            retrofit2.Response<ClassicResponse> res = call.execute();

            if (res.isSuccessful() && res.body() != null) {
                ClassicResponse data = res.body();

                String newAccess = data.getData().getAccessToken();
                String newRefresh = data.getData().getRefreshToken();

                tokenManager.saveTokens(newAccess, newRefresh);

                return newAccess;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Đăng xuất và chuyển về màn hình Login (MainThread)
     */
    private void logout(String message) {
        TokenManager.getInstance(context).clearTokens();

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}


