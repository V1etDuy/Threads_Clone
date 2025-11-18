package com.midterm.threads_clone.auth;

public class VerifyEmailRequest {
    private String token; // token gửi kèm trong link email

    public VerifyEmailRequest(String token) {
        this.token = token;
    }
}
