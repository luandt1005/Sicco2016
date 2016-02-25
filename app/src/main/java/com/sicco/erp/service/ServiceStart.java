package com.sicco.erp.service;

import android.content.Context;
import android.content.Intent;

public class ServiceStart {
	Context context;
	static Intent getNotificationIntentStartService = null;
	static Intent handleNotificationIntentStartService = null;

	public static void startGetNotificationService(Context context) {
		getNotificationIntentStartService = new Intent(context,
				GetAllNotificationService.class);
		getNotificationIntentStartService.putExtra("ACTION", 1);
		context.startService(getNotificationIntentStartService);
	}

	public static void startHandleNotificationService(Context context) {
		handleNotificationIntentStartService = new Intent(context,
				GetAllNotificationService.class);
		context.startService(handleNotificationIntentStartService);
	}

	public static void stopGetNotificationService(Context context) {
		if (getNotificationIntentStartService == null)
			return;
		context.stopService(getNotificationIntentStartService);
		getNotificationIntentStartService = null;
	}

	public static void stopHandleNotificationService(Context context) {
		if (handleNotificationIntentStartService == null)
			return;
		context.stopService(handleNotificationIntentStartService);
		handleNotificationIntentStartService = null;
	}

	public static void stopAllService(Context context) {
		stopGetNotificationService(context);
		stopHandleNotificationService(context);
	}
}