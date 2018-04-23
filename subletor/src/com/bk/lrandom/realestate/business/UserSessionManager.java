package com.bk.lrandom.realestate.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bk.lrandom.realestate.models.User;

public class UserSessionManager {
	public static final String TAG_ID = "id";
	public static final String TAG_FB_ID = "fb_id";
	public static final String TAG_FULL_NAME = "full_name";
	public static final String TAG_USER_NAME = "user_name";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_PHONE = "phone";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_WEBSITE = "websites";
	public static final String TAG_LOGIN = "login";
	public static final String TAG_AVT = "avt";
	public static final String TAG_SKYPE = "skype";
	public static final int IS_LOGIN = 1;
	public static final int IS_LOGOUT = 0;
	public static final int PRIVATE_MODE = 0;
	public static final String SHARED_PREF_NAME = "user";
	public Context context;
	SharedPreferences sharePre;
	Editor editor;

	public UserSessionManager() {
		// TODO Auto-generated constructor stub
	}

	public UserSessionManager(Context context) {
		this.context = context;
		sharePre = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
		editor = sharePre.edit();
	}

	public void storeUserSession(User user) {
		SharedPreferences sharePre = context.getSharedPreferences(
				SHARED_PREF_NAME, PRIVATE_MODE);
		Editor editor = sharePre.edit();
		editor.putInt(TAG_ID, user.getId());
		editor.putString(TAG_FB_ID, user.getFbId());
		editor.putString(TAG_FULL_NAME, user.getFullName());
		editor.putString(TAG_EMAIL, user.getEmail());
		editor.putString(TAG_PHONE, user.getPhone());
		editor.putString(TAG_ADDRESS, user.getAddress());
		editor.putString(TAG_SKYPE, user.getSkype());
		editor.putString(TAG_AVT, user.getAvt());
		editor.putString(TAG_USER_NAME, user.getUserName());
		editor.putInt(TAG_LOGIN, IS_LOGIN);
		editor.commit();
	}

	public void clearUserSession() {

	}

	public void clearAll() {
		SharedPreferences sharePre = context.getSharedPreferences(
				SHARED_PREF_NAME, PRIVATE_MODE);
		Editor editor = sharePre.edit();
		editor.clear();
		editor.commit();
	}

	public User getUserSession() {
		if (sharePre.contains(TAG_LOGIN)) {
			int isLogIn = sharePre.getInt(TAG_LOGIN, 0);
			if (isLogIn == IS_LOGIN) {
				User user = new User();
				user.setId(sharePre.getInt(TAG_ID, 0));
				user.setFbId(sharePre.getString(TAG_FB_ID, null));
				user.setEmail(sharePre.getString(TAG_EMAIL, null));
				user.setAddress(sharePre.getString(TAG_ADDRESS, null));
				user.setPhone(sharePre.getString(TAG_PHONE, null));
				user.setSkype(sharePre.getString(TAG_SKYPE, null));
				user.setFullName(sharePre.getString(TAG_FULL_NAME, null));
				user.setUserName(sharePre.getString(TAG_USER_NAME, null));
				user.setAvt(sharePre.getString(TAG_AVT, null));
				return user;
			}
		}
		return null;
	}
}
