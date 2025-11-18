package com.midterm.threads_clone.api_service;

import com.midterm.threads_clone.User.MeResponse;
import com.midterm.threads_clone.auth.ClassicResponse;
import com.midterm.threads_clone.auth.RefreshRequest;
import com.midterm.threads_clone.auth.RegisterRequest;
import com.midterm.threads_clone.auth.RegisterResponse;
import com.midterm.threads_clone.auth.SignInRequest;
import com.midterm.threads_clone.auth.VerifyEmailRequest;
import com.midterm.threads_clone.auth.VerifyEmailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/v1/auth/refresh")
    Call<ClassicResponse> refreshToken(@Body RefreshRequest req);
    @POST("/v1/auth/login")
    Call<ClassicResponse> signIn(@Body SignInRequest request);
    @POST("/v1/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    @GET("/v1/auth/verify-email")
    Call<VerifyEmailResponse> verifyEmail(@Query("token") String token);
    @Headers("Authorized: true")
    @GET("/v1/auth/me")
    Call<MeResponse> getMe();


}
