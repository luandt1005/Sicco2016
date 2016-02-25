package com.sicco.erp.manager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sicco.erp.ApprovalActivity;
import com.sicco.erp.DealtWithActivity;
import com.sicco.erp.OtherActivity;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.util.Utils;
import com.sicco.task.erp.DetailTaskActivity;
import com.sicco.task.erp.ListTask;
import com.sicco.task.erp.OtherTaskActivity;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;

public class MyNotificationManager {
	Context mContext;
	public static int CONGVAN_NOTIFICATION_ID = 1;
	public static int CONGVIEC_NOTIFICATION_ID = 2;
	public static int LICHBIEU_NOTIFICATION_ID = 3;
	public static int NOTIFICATION_ID = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	NotificationModel notificationModel;
	String message = "";
	String contentText = "";
	String name = "";
	String content = "";
	String url = "";
	String noti = "";
	int noti_count = 0;
	int notify_type;
	PendingIntent pendInt;
	String tag;
	String taskCode;
	static int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP;

	public void notifyType(Context context, ArrayList<NotificationModel> arrayList, int notification_count) {
		// int notification_count = arrayList.size();
		noti_count = notification_count;

		for (int i = 0; i < notification_count; i++) {
			// get data
			int notification_type = arrayList.get(i).getNotify_type();
			String ten = arrayList.get(i).getSoHieuCongVan();
			String noi_dung = arrayList.get(i).getTrichYeu();

			if (notification_count == 1) {
				if (notification_type == 1) {
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_van);
					tag = congvan;
					notify_type = 1;
				}
				if (notification_type == 2) {
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_viec);
					tag = congviec;
					notify_type = 2;
				}
				if (notification_type == 3) {
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.lich_bieu);
					tag = lichbieu;
					notify_type = 3;
				}
				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + "\n";
				contentText = noi_dung;
				notify(context, NOTIFICATION_ID, notify_type);
			}

			if (notification_count > 1) {
				if (notification_type == 1) {
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_van);
					name += "" + ten + "\n";
					tag = congvan;
					notify_type = 1;
				}
				if (notification_type == 2) {
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_viec);
					name += "" + ten + "\n";
					tag = congviec;
					notify_type = 2;
				}
				if (notification_type == 3) {
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.lich_bieu);
					name += "" + ten + "\n";
					tag = lichbieu;
					notify_type = 3;
				}
				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + " " + "\n";
				contentText = name;
				notify(context, NOTIFICATION_ID, notify_type);
			}
		}
	}

	public void notifyCongViec(Context context, ArrayList<Task> data) {
		int notification_count = data.size();
		noti = context.getResources().getString(R.string.notify_new_congviec);
		for (int i = 0; i < notification_count; i++) {
			String ten = data.get(i).getTen_cong_viec();
			String nguoi_xem = data.get(i).getNguoi_xem();
			String mo_ta = data.get(i).getMo_ta();
			String nguoi_thuc_hien = data.get(i).getNguoi_thuc_hien();

			if (notification_count == 1) {
				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + " " + "\n";

				String ten_cv = context.getResources().getString(R.string.ten_cv);
				String nguoi_xem_cv = context.getResources().getString(R.string.nguoi_xem_cv);
				String nguoi_thuc_hien_cv = context.getResources().getString(R.string.nguoi_thuc_hien_cv);
				String mota = context.getResources().getString(R.string.mota);

				contentText = "" + ten_cv + " " + ten + "\n" + nguoi_xem_cv + " " + nguoi_xem + "\n"
						+ nguoi_thuc_hien_cv + " " + nguoi_thuc_hien + "\n";

			} else if (notification_count > 1) {
				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ " " + noti + " " + "\n";
				name += "" + ten + "\n";
				contentText = name;
			}

		}
		notify(context, 4, 4);
	}

	public void notifyBinhLuan(Context context, ArrayList<ReportSteerTask> data) {
		int notification_count = data.size();
		noti = context.getResources().getString(R.string.notify_new_comment);
		for (int i = 0; i < notification_count; i++) {
			String handler = data.get(i).getHandler();
			String content = data.get(i).getContent();
			taskCode = data.get(i).getIdCV();

			if (notification_count == 1) {
				String username = Utils.getString(context, SessionManager.KEY_NAME);
				String in = context.getResources().getString(R.string.in);
				String ten_cong_vec = getTaskData(context, taskCode, username);
				message = "Sicco";

				contentText = handler + " " + context.getResources().getString(R.string.notify_new_comment_msg) + " "
						+ in + " " + context.getResources().getString(R.string.congviec) + ": " + ten_cong_vec + ".\n"
						+ content + "\n";

			} else if (notification_count > 1) {
				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + " " + "\n";
				String cv_handler_new_comment = context.getResources().getString(R.string.cv_handler_new_comment);

				name += handler + "\n";
				contentText = "" + cv_handler_new_comment + "\n" + name;
			}

		}
		Utils.saveInt(context, "STEER_ACTION", Integer.parseInt(taskCode));
		notify(context, 5, 5);
	}

	public void notifyCacLoai(Context context, ArrayList<Dispatch> arrayList, int notification_count) {
		// int notification_count = arrayList.size();
		noti_count = notification_count;

		for (int i = 0; i < notification_count; i++) {
			// get data
			// int notification_type = arrayList.get(i).getNumberDispatch();
			String ten = arrayList.get(i).getNumberDispatch();
			String noi_dung = arrayList.get(i).getDescription();
			if (notification_count == 1) {
				noti = context.getResources().getString(R.string.lich_bieu);

				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + "\n";
				contentText = noi_dung;
				notify(context, 3, 3);
			}
			if (notification_count > 1) {
				NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
				noti = context.getResources().getString(R.string.lich_bieu);
				name += "" + ten + "\n";
				tag = lichbieu;
				notify_type = 3;

				message = context.getResources().getString(R.string.new_noti_mess) + " " + notification_count + " "
						+ noti + " " + "\n";
				contentText = name;
				notify(context, 3, 3);
			}
		}
	}

	public void notifyByState(Context context, Task task, int state) {
		// int notification_count = arrayList.size();

		// get data
		String ten = task.getTen_cong_viec();
		long id = task.getId();
		String noti = "";
		if (state == 1) {
			noti = context.getResources().getString(R.string.inactive);
		} else if (state == 2) {
			noti = context.getResources().getString(R.string.complete);
		} else if (state == 3) {
			noti = context.getResources().getString(R.string.cancel);
		}

		noti.toLowerCase();
		String da = context.getResources().getString(R.string.da);

		message = ten + " " + da + " " + noti;

		int notifi_id = safeLongToInt(id);
		if (notifi_id != 0) {
			notify(context, notifi_id, 6);
		}

	}

	public void notifyState(Context context, ArrayList<Task> data) {
		int notification_count = data.size();
		long id = 6;

		String da = context.getResources().getString(R.string.da);
		for (int i = 0; i < notification_count; i++) {

			String tencv = "";
			String state = data.get(i).getTrang_thai();
			if (state.equals("inactive")) {
				noti = context.getResources().getString(R.string.inactive);
				tencv = getTaskByState(data, "inactive");
			} else if (state.equals("complete")) {
				noti = context.getResources().getString(R.string.complete);
				tencv = getTaskByState(data, "complete");
			} else if (state.equals("cancel")) {
				noti = context.getResources().getString(R.string.cancel);
				tencv = getTaskByState(data, "cancel");
			}
			id = data.get(i).getId();

			Log.d("ToanNM", " === notifyState === " + state);
			if (notification_count == 1) {
//				
				String cv = context.getResources().getString(R.string.congviec_1);
				message = cv + " " +  da + " " +  noti + ":" + "\n";
//
				contentText = tencv + "\n";
//
			} else if (notification_count > 1) {
				String cv = context.getResources().getString(R.string.congviec);
				message = 2 + " " + cv + " " + da + " " + noti + ":" + "\n";
				contentText += "" + tencv + "\n";
			}

		}
		int notifi_id = safeLongToInt(id);
		if (notifi_id != 0) {
			notify(context, notifi_id, 6);
		}
	}
	
	private String getTaskByState(ArrayList<Task> data, String state){
		int max = data.size();
		String name = "";
		for (int i = 0; i < max; i++) {
			String s = data.get(i).getTrang_thai();
			if(s.equals(state)){
				name += data.get(i).getTen_cong_viec();
			}
		}
		Log.d("ToanNM", "State Notification : ===== : name : " + name);
		return name;
	}

	private int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}

	String getAllRunningService(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(100);
		String process = "";
		for (int i = 0; i < rs.size(); i++) {
			ActivityManager.RunningServiceInfo rsi = rs.get(i);
			// get service following by package
			if (rsi.service.getPackageName().contains("com.sicco.erp")) {
				process = rsi.process;
			}
		}

		return process;
	}

	// ==========================================================================
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void notify(Context context, int notification_id, int notify_type) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Intent notIntent = null;
		if (notify_type == 1) {
			notIntent = new Intent(context, ApprovalActivity.class);
		} else if (notify_type == 2) {
			notIntent = new Intent(context, DealtWithActivity.class);
		} else if (notify_type == 3) {
			notIntent = new Intent(context, OtherActivity.class);
		} else if (notify_type == 4) {
			notIntent = new Intent(context, ListTask.class);
		} else if (notify_type == 5) {
//			notIntent = new Intent(context, SteerReportTaskActivity.class);
			notIntent = new Intent(context, DetailTaskActivity.class);
			notIntent.putExtra("id_task", Long.parseLong(taskCode));
		} else if (notify_type == 6) {
			notIntent = new Intent(context, OtherTaskActivity.class);
		}

		notIntent.addFlags(flags);
		// notIntent.putExtra("com.sicco.erp", 1);
		pendInt = PendingIntent.getActivity(context, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		//

		builder.setContentIntent(pendInt).setOngoing(false).setAutoCancel(true)
				// .setPriority(Notification.PRIORITY_HIGH)
				.setSound(alarmSound);

		int build_version = android.os.Build.VERSION.SDK_INT;
		if (build_version >= 16) {
			builder.setPriority(Notification.PRIORITY_HIGH);
		}
		NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
		style.bigText(contentText);
		// style.setSummaryText("Swipe Left or Right to dismiss this
		// Notification.");
		style.build();

		// ==============================

		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(message);
		builder.setContentText(contentText);
		builder.setStyle(style);

		// mo rong
		long[] pattern = { (long) 100, (long) 100, (long) 100, (long) 100, (long) 100 };
		builder.setVibrate(pattern);
		builder.setLights(0xFFFFFFFF, 500, 500);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(notification_id);
		manager.notify(notification_id, notification);

	}

	NotificationDBController db;
	Cursor cursor;

	String getTaskData(Context context, String taskCode, String username) {
		String ten_cong_vec = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null, null, null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.ID_COL + " = \"" + taskCode + "\"" + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
		Log.d("ToanNM", "getTaskData sql : " + sql);
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				ten_cong_vec = cursor
						.getString(cursor.getColumnIndexOrThrow(NotificationDBController.TASK_TENCONGVIEC));
			} while (cursor.moveToNext());
		}
		Log.d("ToanNM", "ten cong viec la day : " + ten_cong_vec);
		return ten_cong_vec;
	}

}