package com.sicco.erp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;

import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.service.GetAllNotificationService;

/**
 * TimeAlarm for starting GetNotificationService
 */
public class Utils {

	static final String SHARED_PREFERENCE_BOOLEAN = "SharedPreference_Boolean";
	static final String SHARED_PREFERENCE_STRING = "SharedPreference_String";
	static AlarmManager manager;
	static PendingIntent pIntent;

	public static void scheduleNext(Context context) {
		Intent intent = new Intent(context, GetAllNotificationService.class);
		intent.putExtra("ACTION", 1);
		long time = SystemClock.elapsedRealtime();
		manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		pIntent = PendingIntent.getService(context, 0, intent,
				Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				time + 20 * 60 * 1000, pIntent);

	}

	public static void stopAlarm(Context context) {
		manager.cancel(pIntent);
	}

	public static void saveBoolean(Context context, String keyName,
			Boolean value) {
		SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCE_BOOLEAN, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putBoolean(keyName, value);

		editor.commit();
	}

	public static boolean getBoolean(Context context, String key,
			Boolean defaultValue) {
		boolean ret = false;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					SHARED_PREFERENCE_BOOLEAN, Context.MODE_PRIVATE);
			ret = pref.getBoolean(key, defaultValue);
		} catch (Exception e) {

		}
		return ret;
	}

	public static void saveString(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCE_STRING, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key) {
		SharedPreferences pref = context.getSharedPreferences(
				SessionManager.PREF_NAME, Context.MODE_PRIVATE);

		return pref.getString(key, "-1");
	}

	public static void saveInt(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCE_STRING, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void saveLong(Context context, String key, long value) {
		SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCE_STRING, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key, int defaultValue) {
		int ret = 0;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					SHARED_PREFERENCE_STRING, Context.MODE_PRIVATE);

			ret = pref.getInt(key, defaultValue);
		} catch (Exception e) {

		}
		return ret;
	}

	public static long getLong(Context context, String key) {
		long ret = 0;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					SHARED_PREFERENCE_STRING, Context.MODE_PRIVATE);

			ret = pref.getLong(key, 0);
		} catch (Exception e) {

		}
		return ret;
	}
}