package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.ReportSteer.OnLoadListener;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;

public class SteerReportActivity extends Activity implements OnClickListener {

	private LinearLayout connectError;
	private ImageView back;
	private ListView listReport;
	private ProgressBar loading;
	private Button retry;
	private ImageView imgSendReportSteer;
	private ReportSteerAdapter reportSteerAdapter;
	private ArrayList<ReportSteer> arrReportSteers;
	private ReportSteer reportSteer;
	private EditText edtContent;
	private Dispatch dispatch;
	private TextView emptyView;
	private Button btnChuyenTiepXuLy;
	private Button btnChuyenCVThanhCongViec;
	private String daxuly = "1";
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;
	private User user;
	private Department department;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steer_report);

		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
		init();
		setListReportSteer(dispatch);
		
		listChecked = new ArrayList<User>();

		department = new Department();
		user = new User();
		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listDep = department.getData(getResources().getString(
				R.string.api_get_deparment));
		allUser = user.getData(getResources().getString(
				R.string.api_get_all_user));

	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		imgSendReportSteer = (ImageView) findViewById(R.id.imgSendReportSteer);
		listReport = (ListView) findViewById(R.id.listReport);
		reportSteer = new ReportSteer(SteerReportActivity.this);
		edtContent = (EditText) findViewById(R.id.edtReportOrSteer);
		emptyView = (TextView) findViewById(R.id.empty_view);
		btnChuyenCVThanhCongViec = (Button) findViewById(R.id.btnChuyenCVThanhCongViec);
		btnChuyenTiepXuLy = (Button) findViewById(R.id.btnChuyenTiepXuLy);
		
		// click
		back.setOnClickListener(this);
		retry.setOnClickListener(this);
		imgSendReportSteer.setOnClickListener(this);
		btnChuyenCVThanhCongViec.setOnClickListener(this);
		btnChuyenTiepXuLy.setOnClickListener(this);
		// set adapter

	}

	private void setListReportSteer(Dispatch dispatch) {
		arrReportSteers = reportSteer.getData(
				getResources().getString(R.string.api_get_steer_report),
				SessionManager.KEY_USER_ID, Long.toString(dispatch.getId()),
				new OnLoadListener() {
					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						reportSteerAdapter.notifyDataSetChanged();

						if (ReportSteer.CHECK_TOTAL_DATA == 0) {
							listReport.setEmptyView(emptyView);
							daxuly = "0";
						}

					}

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});

		reportSteerAdapter = new ReportSteerAdapter(getApplicationContext(),
				arrReportSteers);

		listReport.setAdapter(reportSteerAdapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			setListReportSteer(dispatch);
			break;
		case R.id.imgSendReportSteer:
			sendReportSteer();
			break;
		case R.id.btnChuyenTiepXuLy:
			ActionAdapter.flag = "handle";
			new DialogChooseUser(SteerReportActivity.this, dispatch,
					listDep, allUser, listChecked);
			break;
		case R.id.btnChuyenCVThanhCongViec:
			Intent intent = new Intent();
			intent.setClass(SteerReportActivity.this,
					ConvertDispatchActivity.class);
			intent.putExtra("dispatch", dispatch);
			startActivity(intent);
			break;
		}

	}
	
	//comment
	private void sendReportSteer() {

		final ProgressDialog progressDialog = new ProgressDialog(
				SteerReportActivity.this);

		String content = edtContent.getText().toString().trim();

		if (!content.equals("")) {
			reportSteer.sendReportSteer(
					getResources().getString(R.string.api_send_report), Utils
							.getString(SteerReportActivity.this,
									SessionManager.KEY_USER_ID), Long
							.toString(dispatch.getId()), content, daxuly,
					new OnLoadListener() {

						@Override
						public void onSuccess() {
							edtContent.setText("");

							setListReportSteer(dispatch);
							progressDialog.dismiss();
						}

						@Override
						public void onStart() {
							progressDialog.setMessage(getResources().getString(
									R.string.msg_sending));
							progressDialog.show();
						}

						@Override
						public void onFalse() {
							progressDialog.dismiss();
							Toast.makeText(
									SteerReportActivity.this,
									getResources().getString(
											R.string.internet_false),
									Toast.LENGTH_SHORT).show();

						}
					});

		} else {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.empty_content_report),
					Toast.LENGTH_SHORT).show();
			edtContent.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
		}

	}
}
