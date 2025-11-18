package com.midterm.threads_clone.User;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";

    private static UserManager instance;
    private SharedPreferences prefs;

    private UserManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) instance = new UserManager(context);
        return instance;
    }

    public void saveUser(UserInfo user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_STATUS, user.getStatus());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.apply();
    }

    public UserInfo getUser() {
        UserInfo user = new UserInfo();
        user.setUserId(prefs.getString(KEY_USER_ID, null));
        user.setUsername(prefs.getString(KEY_USERNAME, null));
        user.setStatus(prefs.getString(KEY_STATUS, null));
        user.setRole(prefs.getString(KEY_ROLE, null));
        user.setFirstName(prefs.getString(KEY_FIRST_NAME, null));
        user.setLastName(prefs.getString(KEY_LAST_NAME, null));
        return user;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
