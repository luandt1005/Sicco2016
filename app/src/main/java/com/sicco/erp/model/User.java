package com.sicco.erp.model;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class User {
	public static int LOGIN_FALSE = 0;
	public static int LOGIN_SUCCESS = 1;
	public static boolean getJsonUser = false;

	private Context context;
	private String username;
	private String password;
	private String id;
	private String department;
	private int position = -1;
	private ArrayList<User> listUser;
	
	public User(String id, String username, String department) {
		this.username = username;
		this.id = id;
		this.department = department;
	}

	public User() {
	}
	
	public User(Context context) {
		this.context = context;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean equal(User user){
		boolean ret = false;
		if(this.username.equals(user.username)){
			ret = true;
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "id = " + id + "; username = " + username + "; department = " + department;
	}

	//get all user
	public ArrayList<User> getData(final String url) {
		listUser = new ArrayList<User>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							
							String id = row.getString("id");
							String username = row.getString("username");
							String department = row.getString("phong_ban");
							
							listUser.add(new User(id, username, department));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				getJsonUser = true;
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				getJsonUser = false;
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		return listUser	;
	}
	
	public ArrayList<User> getData(final String url, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		listUser = new ArrayList<User>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							
							String id = row.getString("id");
							String username = row.getString("username");
							String department = row.getString("phong_ban");
							listUser.add(new User(id, username, department));
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
				onLoadListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		return listUser	;
	}
	
	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
