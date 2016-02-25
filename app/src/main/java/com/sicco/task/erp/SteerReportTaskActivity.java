package com.sicco.task.erp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.HomeActivity;
import com.sicco.erp.R;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.ChooseFileActivity;
import com.sicco.erp.util.Utils;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.ReportSteerTaskAdapter;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.ReportSteerTask.OnLoadListener;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogSetProcess;

public class SteerReportTaskActivity extends ChooseFileActivity implements
		OnClickListener, OnItemClickListener {

	private LinearLayout connectError;
	private ImageView back;
	private ListView listReport;
	private ProgressBar loading;
	private Button retry;
	private ImageView imgSendReportSteer;
	private ReportSteerTaskAdapter adapter;
	private ArrayList<ReportSteerTask> arrReportSteers;
	private ReportSteerTask reportSteerTask;
	private EditText edtContent;
	private TextView emptyView, fileName;
	private Button btnChooseFile;
	private Button btnCapNhatTienDo;
	private Button btnThayDoiTrangThai;
	private Task task;
	private ViewDispatch viewDispatch;
	private String path = null;
	private ArrayList<Status> listStatus;
	private ArrayList<Status> listProcess;
	boolean update_status_and_rate = false;
	private long id_task;
	private String s_id_task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steer_report_task);

		Intent intent = getIntent();
		// get id notifi
		id_task = intent.getLongExtra("id_task", -1);

		init();

		// set id cong viec
		if (id_task != -1) {
			s_id_task = "" + id_task;
			task = Task
					.getTaskById(SteerReportTaskActivity.this, getResources()
							.getString(R.string.api_get_infor_task_by_id),
							s_id_task);
		} else {
			task = (Task) intent.getSerializableExtra("task");
			s_id_task = "" + task.getId();
		}

		setListReportSteer(s_id_task);
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		imgSendReportSteer = (ImageView) findViewById(R.id.imgSendReportSteer);
		listReport = (ListView) findViewById(R.id.listReport);
		edtContent = (EditText) findViewById(R.id.edtReportOrSteer);
		emptyView = (TextView) findViewById(R.id.empty_view);
		btnChooseFile = (Button) findViewById(R.id.choose_file);
		fileName = (TextView) findViewById(R.id.file_name);
		reportSteerTask = new ReportSteerTask(SteerReportTaskActivity.this);
		btnThayDoiTrangThai = (Button) findViewById(R.id.btnThayDoiTrangThai);
		btnCapNhatTienDo = (Button) findViewById(R.id.btnCapNhatTienDo);

		// click
		back.setOnClickListener(this);
		retry.setOnClickListener(this);
		btnChooseFile.setOnClickListener(this);
		imgSendReportSteer.setOnClickListener(this);
		listReport.setOnItemClickListener(this);
		btnThayDoiTrangThai.setOnClickListener(this);
		btnCapNhatTienDo.setOnClickListener(this);
	}

	private void setListReportSteer(String id_task) {
		arrReportSteers = new ArrayList<ReportSteerTask>();
		arrReportSteers = reportSteerTask.getData(SteerReportTaskActivity.this,
				getResources().getString(R.string.api_get_steer_report_task),
				id_task, new ReportSteerTask.OnLoadListener() {
					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();

						if (adapter.getCount() <= 0) {
							listReport.setEmptyView(emptyView);
						}

					}

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onFailure() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});

		adapter = new ReportSteerTaskAdapter(SteerReportTaskActivity.this,
				arrReportSteers);
		listReport.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

		final String[] nguoithuchien = task.getNguoi_thuc_hien().split(",");
		final String username = Utils.getString(SteerReportTaskActivity.this,
				"name");
		for (int i = 0; i < nguoithuchien.length; i++) {
			if (username.equals(nguoithuchien[i])) {
				update_status_and_rate = true;
			}
		}

		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			setListReportSteer(s_id_task);
			break;
		case R.id.choose_file:
			showFileChooser();
			break;
		case R.id.imgSendReportSteer:
			sendReportSteer();
			break;
		case R.id.btnCapNhatTienDo:
			if (task.getTrang_thai().equals("complete")) {
				Toast.makeText(SteerReportTaskActivity.this,
						getResources().getString(R.string.not_update_rate),
						Toast.LENGTH_SHORT).show();
			} else {
				if (update_status_and_rate) {
					listProcess = new ArrayList<Status>();

					String[] key = getResources().getStringArray(
							R.array.process_key);
					String[] value = getResources().getStringArray(
							R.array.process_value);
					int max = key.length;
					for (int i = 0; i < max; i++) {
						listProcess.add(new Status(i + 1, key[i], value[i]));
					}

					DialogSetProcess dialog = new DialogSetProcess(
							SteerReportTaskActivity.this, listProcess, task);
					dialog.showDialog();
				} else {
					Toast.makeText(
							SteerReportTaskActivity.this,
							getResources().getString(R.string.info_update_rate),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.btnThayDoiTrangThai:
			if (task.getTrang_thai().equals("complete")) {
				Toast.makeText(SteerReportTaskActivity.this,
						getResources().getString(R.string.not_update_status),
						Toast.LENGTH_SHORT).show();
			} else {
				if (update_status_and_rate) {

					listStatus = new ArrayList<Status>();

					listStatus.add(new Status(0, "active", getResources()
							.getString(R.string.active)));
					listStatus.add(new Status(1, "inactive", getResources()
							.getString(R.string.inactive)));
					listStatus.add(new Status(2, "complete", getResources()
							.getString(R.string.complete)));
					listStatus.add(new Status(3, "cancel", getResources()
							.getString(R.string.cancel)));

					new DialogChangeStatusTask(SteerReportTaskActivity.this,
							listStatus, task);
				} else {
					Toast.makeText(
							SteerReportTaskActivity.this,
							getResources().getString(
									R.string.info_update_status),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}

	}

	// click item
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ReportSteerTask reportSteerTask = (ReportSteerTask) arg0.getAdapter()
				.getItem(arg2);
		if (!reportSteerTask.getFile().equals("")) {
			viewDispatch = new ViewDispatch(SteerReportTaskActivity.this,
					reportSteerTask.getFile());
		} else {
			Toast.makeText(SteerReportTaskActivity.this,
					getResources().getString(R.string.no_attach),
					Toast.LENGTH_SHORT).show();
		}
	}

	// choose file
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				path = uri.getPath();

				File file = new File(path);

				fileName.setText(file.getName());
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// send comment
	private void sendReportSteer() {
		final ProgressDialog progressDialog = new ProgressDialog(
				SteerReportTaskActivity.this);

		String content = edtContent.getText().toString().trim();

		if (!content.equals("")) {
			try {
				reportSteerTask.sendReport(SteerReportTaskActivity.this,
						Long.parseLong(s_id_task), content, path,
						new OnLoadListener() {

							@Override
							public void onSuccess() {
								progressDialog.dismiss();
								edtContent.setText("");
								path = null;
								fileName.setText(path);
								Toast.makeText(
										SteerReportTaskActivity.this,
										getResources().getString(
												R.string.success),
										Toast.LENGTH_SHORT).show();
								setListReportSteer(s_id_task);

							}

							@Override
							public void onStart() {
								progressDialog.setMessage(getResources()
										.getString(R.string.msg_sending));
								progressDialog.show();
							}

							@Override
							public void onFailure() {
								progressDialog.dismiss();
								path = null;
								fileName.setText(path);
								Toast.makeText(
										SteerReportTaskActivity.this,
										getResources().getString(
												R.string.internet_false),
										Toast.LENGTH_LONG).show();

							}
						});
			} catch (FileNotFoundException e) {
				Log.d("LuanDT", "catch sendReport");
				edtContent.setText("");
				path = null;
				fileName.setText(path);
				progressDialog.dismiss();
				Toast.makeText(SteerReportTaskActivity.this,
						getResources().getString(R.string.file_error),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.empty_content_report),
					Toast.LENGTH_SHORT).show();
			edtContent.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
		}

	}

	@Override
	protected void onResume() {
		HomeActivity.checkDate(SteerReportTaskActivity.this);
		super.onResume();
	}

}