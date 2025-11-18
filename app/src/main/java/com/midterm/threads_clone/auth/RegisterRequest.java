package com.midterm.threads_clone.auth;

public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String confirmPassword;

    public RegisterRequest(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getter/Setter nếu cần
}
