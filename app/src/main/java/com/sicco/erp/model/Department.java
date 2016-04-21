package com.sicco.erp.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String departmentName;
	private int numUser;
	public static boolean getJsonDep = false;

	public Department() {
	}

	public Department(long id, String departmentName, int numUser) {
		this.id = id;
		this.departmentName = departmentName;
		this.numUser = numUser;
	}

	public int getNumUser() {
		return numUser;
	}

	public void setNumUser(int numUser) {
		this.numUser = numUser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public ArrayList<Department> getData( final String url) {
		final ArrayList<Department> listDep = new ArrayList<Department>();
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
							String id = row.getString("id_phong_ban");
							String depName = row.getString("ten_phong_ban");
							String numUser = row.getString("numUser");

							listDep.add(new Department(Long.parseLong(id), depName, Integer.parseInt(numUser)));
							
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				getJsonDep = true;
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				getData(url);
				getJsonDep = false;
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		Log.d("NgaDV", "getJsonDep = " + getJsonDep);
		return listDep;
	}
	
	public ArrayList<Department> getData(final String url, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		final ArrayList<Department> listDep = new ArrayList<Department>();
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
							String id = row.getString("id_phong_ban");
							String depName = row.getString("ten_phong_ban");
							String numUser = row.getString("numUser");
							
							listDep.add(new Department(Long.parseLong(id), depName, Integer.parseInt(numUser)));
							
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
		return listDep;
	}
	
	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
