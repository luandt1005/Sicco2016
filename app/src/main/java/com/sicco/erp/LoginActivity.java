package com.sicco.erp;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.manager.AlertDialogManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.service.ServiceStart;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login;

	String login_file;
	// Session Manager Class
	SessionManager session;
	String p = "";
	String u;
	String mToken, mUser_id, mId_phonng_ban;

	ProgressBar mLoginDialog;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		// click
		login.setOnClickListener(this);

		// progress
		// mLoginDialog = new ProgressDialog(this);
		mLoginDialog = (ProgressBar) findViewById(R.id.login_progress);
		mLoginDialog.setVisibility(View.GONE);

		// session
		session = SessionManager.getInstance(getApplicationContext());

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login:
			login();
			break;
		}
	}

	private void login() {

		u = username.getText().toString().trim().toLowerCase();
		p = password.getText().toString().trim().toLowerCase();

		if (u.trim().length() > 0 && p.trim().length() > 0) {
			MyAsync();
		} else {
			String t = getApplicationContext().getResources().getString(
					R.string.error_title);
			String s = getApplicationContext().getResources().getString(
					R.string.error_null);
			alert.showAlertDialog(LoginActivity.this, t, s, false);
		}
	}

	void MyAsync() {
		AsyncHttpClient handler = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("username", u);
		params.add("password", p);
		mLoginDialog.setVisibility(View.VISIBLE);
		login.setEnabled(false);

		handler.post(getApplicationContext(),
				getResources().getString(R.string.api_get_one_user), params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						int error_code = -1;
						String st = response.toString();
						mLoginDialog.setVisibility(View.GONE);
						login.setEnabled(true);
						try {
							JSONObject json = new JSONObject(st);
							error_code = Integer.valueOf(json
									.getString("error"));
							u = json.getString("username");
							mToken = json.getString("token");
							mUser_id = json.getString("id");
							mId_phonng_ban = json.getString("phongban");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (error_code == 0) {
							session.createLoginSession(u, p, mToken, mUser_id,
									mId_phonng_ban, true);

							Intent i = new Intent(getApplicationContext(),
									HomeActivity.class);
							startActivity(i);
							ServiceStart
									.startGetNotificationService(getApplicationContext());
							finish();
						} else if (error_code != 0) {
							String t = getApplicationContext().getResources()
									.getString(R.string.error_title);
							String s = getApplicationContext().getResources()
									.getString(R.string.error);
							alert.showAlertDialog(LoginActivity.this, t, s,
									false);
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						mLoginDialog.setVisibility(View.GONE);
						login.setEnabled(true);
						String s = getApplicationContext().getResources()
								.getString(R.string.internet_false);
						String t = getApplicationContext().getResources()
								.getString(R.string.error_title);
						alert.showAlertDialog(LoginActivity.this, t, s, false);

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});

	}

}