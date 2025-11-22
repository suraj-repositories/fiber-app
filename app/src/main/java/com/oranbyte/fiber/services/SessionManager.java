package com.oranbyte.fiber.services;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "fiber_app";

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveToken(String token) {
        editor.putString("auth_token", token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString("auth_token", null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

