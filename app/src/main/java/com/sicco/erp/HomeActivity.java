package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.service.ServiceStart;
import com.sicco.erp.util.BadgeUtils;
import com.sicco.erp.util.Utils;
import com.sicco.task.erp.AssignedTaskActivity;
import com.sicco.task.erp.ListTask;
import com.sicco.task.erp.OtherTaskActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnClickListener {
	private LinearLayout canphe, xuly, cacloai, giaoviec, dagiao, danhsachviec;
	private FrameLayout exit;
	private static AlertDialog alertDialog;
	public static ArrayList<Department> listDep;
	public static ArrayList<User> allUser;

	// ToanNM
	private int cvcp_count, cvxl_count, cv_count;
	private SessionManager session;
	private static HomeActivity homeActivity;

	String myPackage = "com.sicco.erp";
	String p = "";
	String u;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		// session
		session = SessionManager.getInstance(getApplicationContext());
		// check session
		HashMap<String, String> hashMap = session.getUserDetails();
		u = hashMap.get(SessionManager.KEY_NAME);
		p = hashMap.get(SessionManager.KEY_PASSWORD);
		if (u.equals("") && p.equals("")) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		// ToanNM
		setContentView(R.layout.activity_home);
		homeActivity = this;
		session = SessionManager.getInstance(getApplicationContext());
		init();
	}

	public static HomeActivity getInstance() {
		return homeActivity;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelAllNotification(getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();
		setCountNotify();
		String process = getAllRunningService();
		if (!process.equalsIgnoreCase(myPackage)) {
			// ServiceStart.startGetNotificationService(getApplicationContext());
			startGetAllNotificationService();
		}
	}

	void startGetAllNotificationService() {
		Intent intent = new Intent(getApplicationContext(),
				GetAllNotificationService.class);
		intent.putExtra("ACTION", 0);
		getApplicationContext().startService(intent);
	}

	public String getAllRunningService() {
		ActivityManager am = (ActivityManager) getApplicationContext()
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> rs = am
				.getRunningServices(100);
		String process = "";
		for (int i = 0; i < rs.size(); i++) {
			ActivityManager.RunningServiceInfo rsi = rs.get(i);
			// get service following by package
			if (rsi.service.getPackageName().contains(myPackage)) {
				process = rsi.process;
			}
		}

		return process;
	}

	void checkNotifyCount(TextView textView, int notifyCount, int type) {
		if (notifyCount != 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText("" + notifyCount);
		} else if (notifyCount == 0) {
			textView.setVisibility(View.GONE);
			cancelNotification(getApplicationContext(), type);
		}
	}

	private void init() {
		// view
		canphe = (LinearLayout) findViewById(R.id.canphe);
		xuly = (LinearLayout) findViewById(R.id.xuly);
		cacloai = (LinearLayout) findViewById(R.id.cacloai);
		giaoviec = (LinearLayout) findViewById(R.id.giaoviec);
		dagiao = (LinearLayout) findViewById(R.id.duocgiao);
		danhsachviec = (LinearLayout) findViewById(R.id.danhsachviec);
		// option = (FrameLayout) findViewById(R.id.option);
		exit = (FrameLayout) findViewById(R.id.exit);
		// click
		canphe.setOnClickListener(this);
		xuly.setOnClickListener(this);
		cacloai.setOnClickListener(this);
		giaoviec.setOnClickListener(this);
		dagiao.setOnClickListener(this);
		danhsachviec.setOnClickListener(this);

		// gaoviec.setVisibility(View.GONE);
		// dagiao.setVisibility(View.GONE);
		// danhsachviec.setVisibility(View.GONE);

		// option.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.canphe:
			startActivity(ApprovalActivity.class);
			break;
		case R.id.xuly:
			startActivity(DealtWithActivity.class);
			break;
		case R.id.cacloai:
			startActivity(OtherActivity.class);
			break;
		case R.id.giaoviec:
			startActivity(AssignedTaskActivity.class);
			break;
		case R.id.duocgiao:
			startActivity(ListTask.class);
			break;
		case R.id.danhsachviec:
			startActivity(OtherTaskActivity.class);
			break;
		case R.id.exit:
			
			new logout().execute("");
			
			break;
		}
	}

	void clearNotifyCount() {
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CVCP_KEY, 0);
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CVXL_KEY, 0);
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CL_KEY, 0);
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CV_KEY, 0);
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.TOTAL_KEY, 0);
		BadgeUtils.setBadge(getApplicationContext(), 0);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void startActivity(Class c) {
		Intent intent = new Intent(HomeActivity.this, c);
		startActivity(intent);
	}

	private void exit() {
		System.exit(0);
	}

	private void showDialogConfirmExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(HomeActivity.this,
						android.R.style.Theme_Holo_Light));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.app_name);
		builder.setMessage(R.string.confirm_exit);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.exit,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						exit();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.dismiss();
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}

	TextView notify_cvcp, notify_cvxl, notify_cv;

	private void setCountNotify() {
		cvcp_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CVCP_KEY, 0);
		cvxl_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CVXL_KEY, 0);
		cv_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CV_KEY, 0);
		notify_cvcp = (TextView) findViewById(R.id.activity_home_notify_canphe);
		notify_cvxl = (TextView) findViewById(R.id.activity_home_notify_xuly);
		notify_cv = (TextView) findViewById(R.id.activity_home_notify_danhsachviec);
		checkNotifyCount(notify_cvcp, cvcp_count, 1);
		checkNotifyCount(notify_cvxl, cvxl_count, 2);
		checkNotifyCount(notify_cv, cv_count, 3);

		if (cvcp_count == 0 && cvxl_count == 0 && cv_count == 0) {
			BadgeUtils.setBadge(getApplicationContext(), 0);
		}
	}

	public static class NotifyBR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (HomeActivity.getInstance() != null)
				HomeActivity.getInstance().setCountNotify();
		}
	}

	void CongVanCancelNotification(Context context) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager
				.cancel(MyNotificationManager.CONGVAN_NOTIFICATION_ID);
	}

	void CongViecCancelNotification(Context context) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager
				.cancel(MyNotificationManager.CONGVIEC_NOTIFICATION_ID);
	}

	void LichBieuCancelNotification(Context context) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager
				.cancel(MyNotificationManager.LICHBIEU_NOTIFICATION_ID);
	}

	void cancelNotification(Context context, int id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(id);
	}

	void cancelAllNotification(Context context) {
		CongVanCancelNotification(context);
		CongViecCancelNotification(context);
		LichBieuCancelNotification(context);
		cancelNotification(context, 4);
		cancelNotification(context, 5);
		cancelNotification(context, 6);
	}

	public static void checkDate(final Context context) {

		AsyncHttpClient client = new AsyncHttpClient();

		client.post("http://tunt.uboxi.com/sicco/time_limit.php",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String resutl = response.toString();
						Log.d("NgaDV", "response : " + resutl);
						if (!resutl.isEmpty()) {
							try {
								JSONObject jsonObject = new JSONObject(resutl);

								String status = jsonObject.getString("status");
								if (status.equals("0")) {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											new ContextThemeWrapper(
													context,
													android.R.style.Theme_Holo_Light));
									builder.setIcon(R.drawable.ic_launcher);
									builder.setTitle(R.string.app_name);
									builder.setMessage(R.string.confirm_exit);
									builder.setCancelable(false);
									builder.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													System.exit(0);
												}
											});
									alertDialog = builder.create();
									alertDialog.show();
								} else {
									// do something
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						super.onSuccess(statusCode, headers, response);
					}

				});

	}
	
	class logout extends AsyncTask<String, Void, String> {

		protected void onPostExecute(Bitmap result) {
		}

		@Override
		protected String doInBackground(String... params) {
			//
			session.logoutUser();
			NotificationDBController db = NotificationDBController
					.getInstance(getApplicationContext());
			getTaskID(db);
			db.deleteAllData();
			Utils.saveBoolean(getApplicationContext(), "FIRSTRUN", true);
			ServiceStart.stopAllService(getApplicationContext());
			Utils.stopAlarm(getApplicationContext());
			cancelAllNotification(getApplicationContext());
			clearNotifyCount();
			finish();
			//
			return null;
		}
	}
	

	private void getTaskID(NotificationDBController db){
		
		String sql = "Select * from " + NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.TASK_STATE + " = \"" + "active" + "\"" 
				+ " or "
				+ NotificationDBController.TASK_STATE + " = \"" + "cancel" + "\""
				+ " or "
				+ NotificationDBController.TASK_STATE + " = \"" + "inactive" + "\"" ;
		
		Cursor cursor = db.rawQuery(sql, null);
		Log.d("ToanNM", "getTaskID : ===== : " + sql);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(NotificationDBController.ID_COL));
				Log.d("ToanNM", "state id : ==== ===== : " + id);
				cancelNotification(getApplicationContext() ,id);
			} while (cursor.moveToNext());
		}
	}

	@Override
	protected void onResume() {
		checkDate(this);
		super.onResume();
	}

}