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

public class Project implements Serializable {
	private String name;
	private Long id;
	private ArrayList<Project> listProject;
	private String strProject;
	public static boolean getJsonProject = false;

	public ArrayList<Project> getListProject() {
		return listProject;
	}

	public void setListProject(ArrayList<Project> listProject) {
		this.listProject = listProject;
	}

	public String getStrProject() {
		return strProject;
	}

	public void setStrProject(String strProject) {
		this.strProject = strProject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Project(String name, Long id) {
		super();
		this.name = name;
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project() {
		super();
	}
	public ArrayList<Project> getData( final String url) {
		listProject = new ArrayList<Project>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jSon = response.toString();
				if (!jSon.isEmpty()) {
					try {
						JSONObject jsonObject = new  JSONObject(jSon);
						if (jsonObject.getInt("success") == 1) {
							JSONArray rows = jsonObject.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								listProject.add(new Project(row.getString("ten_du_an"), Long.parseLong(row.getString("id_du_an"))));
							}
							getJsonProject = true;
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				getData(url);
				getJsonProject = false;
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		Log.d("NgaDV", "getJsonProject = " + getJsonProject);
		return listProject;
	}
	public ArrayList<Project> getData(String url,OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		listProject = new ArrayList<Project>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post( url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				onLoadListener.onFalse();
				
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jSon = response.toString();
				if (!jSon.isEmpty()) {
					try {
						JSONObject jsonObject = new  JSONObject(jSon);
						if (jsonObject.getInt("success") == 1) {
							JSONArray rows = jsonObject.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								onLoadListener.onSuccess();
								JSONObject row = rows.getJSONObject(i);
								listProject.add(new Project(row.getString("ten_du_an"), Long.parseLong(row.getString("id_du_an"))));
							}
						}else {
							onLoadListener.onFalse();
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		});
		return listProject;
	}
	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
