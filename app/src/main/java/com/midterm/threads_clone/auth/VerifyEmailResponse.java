package com.midterm.threads_clone.auth;

public class VerifyEmailResponse {
    private int statusCode;
    private String message;
    private String path;
    private String timestamp;
    private Data data;

    public static class Data {
        private String accessToken;
        private String refreshToken;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }

    public Data getData() { return data; }
    public String getMessage() { return message; }
}
