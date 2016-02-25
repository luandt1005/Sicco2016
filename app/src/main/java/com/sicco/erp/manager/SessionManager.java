package com.sicco.erp.manager;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sicco.erp.LoginActivity;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	public static final String PREF_NAME = "sicco_pref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String KEY_PASSWORD = "password";

	// Email address (make variable public to access from outside)
	public static final String KEY_TOKEN = "token";
	
	public static final String KEY_USER_ID = "user_id";
	
	public static final String KEY_ID_PHONGBAN = "id_phong_ban";
	
	//Remember user information or not
	public static final String KEY_REMEMBER = "remember";
	
	// Constructor
	private SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	private static SessionManager mInstance;
	public static SessionManager getInstance(Context context){
		if(mInstance == null) mInstance = new SessionManager(context);
		return mInstance;
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String pass, String token, String user_id, String id_phong_ban, boolean remember) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);
		// Storing password in pref
		editor.putString(KEY_PASSWORD, pass);

		// Storing token in pref
		editor.putString(KEY_TOKEN, token);
		
		editor.putString(KEY_USER_ID, user_id);
		
		editor.putString(KEY_ID_PHONGBAN, id_phong_ban);
		
		// Storing remember in pref
		editor.putBoolean(KEY_REMEMBER, remember);

		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public boolean checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}
		return this.isLoggedIn();

	}

	/**
	 * Check if remember user infor
	 * */
	public boolean isRememberInfor(){
		return pref.getBoolean(KEY_REMEMBER, false);
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, ""));

		// user email id
		user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, ""));

		// user email id
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, ""));
		// return user
		user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, ""));
		
		user.put(KEY_ID_PHONGBAN, pref.getString(KEY_ID_PHONGBAN, ""));
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
