package com.midterm.threads_clone.auth;

public class RegisterResponse {
    private int statusCode;
    private String message;
    private String path;
    private String timestamp;
    private Data data;

    public static class Data {
        private String username;
        private String email;
        private String registeredAt;

        // Getter
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRegisteredAt() { return registeredAt; }
    }

    public Data getData() { return data; }
    public String getMessage() { return message; }
}
