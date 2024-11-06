package com.example.splash1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefManager {
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_PREFERENCE_SET = "is_preference_set";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";  // New key to track login status
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save User ID
    public void saveUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
        Log.d("SharedPrefManager", "User ID saved: " + userId);
    }

    // Get User ID
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Save Username
    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
        Log.d("SharedPrefManager", "Username saved: " + username);
    }

    // Get Username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "User");
    }

    // Save Preference set status
    public void setIsPreferenceSet(boolean isSet) {
        editor.putBoolean(KEY_IS_PREFERENCE_SET, isSet);
        editor.apply();
    }

    // Get Preference set status
    public boolean getIsPreferenceSet() {
        return sharedPreferences.getBoolean(KEY_IS_PREFERENCE_SET, false);
    }

    // Save Login status
    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    // Get Login status
    public boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Clear preferences (Used for logout)
    public void clear() {
        editor.clear();
        editor.apply();
        Log.d("SharedPrefManager", "All preferences cleared.");
    }
}
