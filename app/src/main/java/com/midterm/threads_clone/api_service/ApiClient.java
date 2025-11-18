package com.midterm.threads_clone.api_service;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    // Hàm dùng cho app khi tạo Retrofit
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://192.168.50.12:8080/") // Base URL của bạn
                    .baseUrl("http://103.249.200.211:8080/") // Base URL của bạn
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    // Hàm cho Interceptor gọi khi cần refresh token
    public static Retrofit getRetrofitInstance() {
        return retrofit;
    }
}
