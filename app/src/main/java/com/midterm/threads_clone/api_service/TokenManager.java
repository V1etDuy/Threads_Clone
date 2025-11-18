package com.midterm.threads_clone.api_service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import org.json.JSONObject;

public class TokenManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_ACCESS = "access_token";
    private static final String KEY_REFRESH = "refresh_token";

    private static TokenManager instance;
    private final SharedPreferences prefs;

    private TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_ACCESS, accessToken)
                .putString(KEY_REFRESH, refreshToken)
                .apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS, null);
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH, null);
    }

    public void clearTokens() {
        prefs.edit().remove(KEY_ACCESS).remove(KEY_REFRESH).apply();
    }

    public boolean isTokenExpired(String token) {
        if (token == null || token.trim().isEmpty()) return true;

        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return true;

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(payload);

            long exp = jsonObject.getLong("exp");
            long now = System.currentTimeMillis() / 1000;

            return now >= exp;
        } catch (Exception e) {
            return true;
        }
    }
}
