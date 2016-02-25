package com.sicco.task.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.util.Utils;

public class ReportSteerTask {
	private Context context;
	private long id;
	private String handler;
	private String date;
	private String content;
	private String file;
	private String id_cv;
	private ArrayList<ReportSteerTask> data;

	public ReportSteerTask(Context context) {
		this.context = context;
	}

	public ReportSteerTask(long id, String handler, String date,
			String content, String file) {
		super();
		this.id = id;
		this.handler = handler;
		this.date = date;
		this.content = content;
		this.file = file;
	}

	public ReportSteerTask(long id, String handler, String content, String id_cv) {
		super();
		this.id = id;
		this.handler = handler;
		this.content = content;
		this.id_cv = id_cv;
	}

	public String getIdCV() {
		return id_cv;
	}

	public void setTaskCode(String id_cv) {
		this.id_cv = id_cv;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ArrayList<ReportSteerTask> getData() {
		return data;
	}

	public void setData(ArrayList<ReportSteerTask> data) {
		this.data = data;
	}

	// getData
	public ArrayList<ReportSteerTask> getData(final Context context,
			String url, String id_task, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<ReportSteerTask>();

		RequestParams params = new RequestParams();
		// params.add("user_id", Utils.getString(context, "user_id"));
		params.add("id_cv", id_task);

		Log.d("LuanDT", "params: " + params);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				Log.d("LuanDT", "json: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);

							String id = row.getString("id");
							String handler = row.getString("nguoi_binh_luan");
							String date = row.getString("thoi_gian");
							String content = row.getString("noi_dung");
							String file = row.getString("dinh_kem");

							file = file.replace(" ", "%20");

							data.add(new ReportSteerTask(Long.parseLong(id),
									handler, date, content, file));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				onLoadListener.onSuccess();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Log.d("LuanDT", "onFailure");
				onLoadListener.onFailure();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		return data;
	}

	// send report
	public void sendReport(final Context context, long id_cv, String content,
			String pathFile, OnLoadListener OnLoadListener)
			throws FileNotFoundException {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		RequestParams params = new RequestParams();

		if (pathFile != null) {
			File file = new File(pathFile);
			params.put("dinh_kem", file);
		} else {
			params.put("dinh_kem", "");
		}

		params.add("username", Utils.getString(context, "name"));
		params.add("id_user", Utils.getString(context, "user_id"));
		params.put("id_cv", id_cv);
		params.put("noi_dung", content);

		Log.d("LuanDT", "params: " + params);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(
				context.getResources()
						.getString(R.string.api_send_comment_task), params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String jsonRead = response.toString();

						Log.d("LuanDT", "json: " + jsonRead);
						if (!jsonRead.isEmpty()) {
							try {
								JSONObject object = new JSONObject(jsonRead);
								int success = object.getInt("success");
								if (success == 1) {
									onLoadListener.onSuccess();
									int id = object.getInt("id_binh_luan");
									Log.d("LuanDT", "Id binh luan: " + id);
									insertToDB(context, id, "", "");
								} else {
									onLoadListener.onFailure();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						onLoadListener.onFailure();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFailure();
	}

	private OnLoadListener onLoadListener;

	void insertToDB(Context context, int id, String content, String handler) {
		String username = Utils.getString(context, SessionManager.KEY_NAME);
		String date = getCurrentDate();
		NotificationDBController db = NotificationDBController
				.getInstance(context);

		ContentValues values = new ContentValues();
		values.put(NotificationDBController.ID_COL, id);
		values.put(NotificationDBController.USERNAME_COL, username);
		values.put(NotificationDBController.REPORT_CONTENT, content);
		values.put(NotificationDBController.REPORT_DATE, date);
		values.put(NotificationDBController.REPORT_HANDLER, handler);

		db.insert(NotificationDBController.REPORT_TABLE_NAME, null, values);

	}

	String getCurrentDate() {
		String currentDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String currentDateandTime = sdf.format(new Date());
			Date cdate = sdf.parse(currentDateandTime);
			Calendar now2 = Calendar.getInstance();
			now2.add(Calendar.DATE, 0);

			int d = now2.get(Calendar.DATE);
			int m = now2.get(Calendar.MONTH) + 1;
			String month = "", date = "";
			if (m < 10) {
				month = "0" + m;
			} else {
				month = "" + m;
			}
			if (d < 10) {
				date = "0" + d;
			} else {
				date = "" + d;
			}

			int year = now2.get(Calendar.YEAR);
			String beforedate = year + "/" + month + "/" + date;
			Date BeforeDate1 = sdf.parse(beforedate);
			cdate.compareTo(BeforeDate1);

			currentDate = beforedate.replace("/", "-");

			Log.d("MyDebug", "secondary List : " + currentDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return currentDate;
	}

}