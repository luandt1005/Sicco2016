package com.sicco.task.erp;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.ReportSteerTaskAdapter;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;

@SuppressWarnings("deprecation")
public class DetailTaskActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private TextView title, content, assigner, implementers, assigned_at,
			expired_at, completed_infact, process, emptyView, attach_file;
	private ImageView back;
	private SlidingDrawer drawer;
	private ViewDispatch viewDispatch;

	private long id_task;
	private Task task;

	private LinearLayout connectError, connectError1;
	private ScrollView scroll;
	private ListView listReport;
	private ProgressBar loading, loading1;
	private Button retry, retry1;

	private ArrayList<ReportSteerTask> arrReportSteers;
	private ReportSteerTaskAdapter adapter;
	private ReportSteerTask reportSteerTask;
	private String s_id_task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_detail_task);
	
		Intent intent = getIntent();
		// get id notifi
		id_task = intent.getLongExtra("id_task", -1);

		Log.d("LuanDT", "id_task: " + id_task);
		
		// set id cong viec
		if (id_task != -1) {
			s_id_task = "" + id_task;
			setDetailTask(s_id_task);
			
		} else {
			task = (Task) intent.getSerializableExtra("task");
			s_id_task = "" + task.getId();
			init();
			setListReportSteer(s_id_task);
		}
	}

	private void init() {
		reportSteerTask = new ReportSteerTask(DetailTaskActivity.this);
		drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		back = (ImageView) findViewById(R.id.back);

		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.task_content);
		assigner = (TextView) findViewById(R.id.assigner);
		implementers = (TextView) findViewById(R.id.implementers);
		assigned_at = (TextView) findViewById(R.id.assigned_at);
		expired_at = (TextView) findViewById(R.id.expired_at);
		completed_infact = (TextView) findViewById(R.id.completed_infact);
		process = (TextView) findViewById(R.id.process);
		attach_file = (TextView) findViewById(R.id.attach_file);

		scroll = (ScrollView) findViewById(R.id.scroll);
		
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		
		loading1 = (ProgressBar) findViewById(R.id.loading1);
		retry1 = (Button) findViewById(R.id.retry1);
		connectError1 = (LinearLayout) findViewById(R.id.connect_error1);
		
		listReport = (ListView) findViewById(R.id.listReport);
		emptyView = (TextView) findViewById(R.id.empty_view);

		attach_file.setOnClickListener(this);
		back.setOnClickListener(this);

		retry.setOnClickListener(this);
		listReport.setOnItemClickListener(this);

		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {

			}
		});
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {

			}
		});

		// setData
		title.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_title) + "</i></u></b></font>" + " " + task.getTen_cong_viec()));
		content.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_content) + "</i></u></b></font>" + " " + task.getMo_ta()));
		assigner.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_assigner) + "</i></u></b></font>" + " " + task.getNguoi_giao()));
		implementers.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.nguoi_thuc_hien_cv) + "</i></u></b></font>" + " " + task.getNguoi_thuc_hien()));
		assigned_at.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_assigned_at) + "</i></u></b></font>" + " " + task.getNgay_bat_dau().substring(0, 10)));
		expired_at.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_expired_at) + "</i></u></b></font>" + " " + task.getNgay_ket_thuc().substring(0, 10)));
		if(!task.getNgay_httt().equals("null")){
			completed_infact.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_completed_infact) + "</i></u></b></font>" + " " + task.getNgay_httt().substring(0, 10)));
		} else {
			completed_infact.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_completed_infact) + "</i></u></b></font>" + " " + task.getNgay_httt()));
		}
		process.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_process) + "</i></u></b></font>" + " " + task.getTien_do() + "%"));
		if(task.getDinh_kem().equals("")){
			attach_file.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_attach_file) + "</i></u></b></font>" + " " + getResources().getString(R.string.no_attach)));
		} else {
			File file = new File(task.getDinh_kem());
			attach_file.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_attach_file) + "</i></u></b></font>" + " " + "<font color = '#358cd1'><u><i>" + file.getName() + "</i></u></font>"));
		}
		
		if(!task.getTrang_thai().equals("complete")){
			completed_infact.setVisibility(View.GONE);
		}
		
		if(task.getTrang_thai().equals("complete")){
			process.setVisibility(View.GONE);
		}

	}

	private void setListReportSteer(String id_task) {
		arrReportSteers = new ArrayList<ReportSteerTask>();
		arrReportSteers = reportSteerTask.getData(DetailTaskActivity.this,
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

		adapter = new ReportSteerTaskAdapter(DetailTaskActivity.this,
				arrReportSteers);
		listReport.setAdapter(adapter);
	}
	
	private void setDetailTask(String id_task){
		task = new Task(DetailTaskActivity.this);
		task = task
				.getTaskById(DetailTaskActivity.this, getResources()
						.getString(R.string.api_get_infor_task_by_id),
						id_task, new Task.OnLoadListener() {
							
							@Override
							public void onSuccess() {
								loading1.setVisibility(View.GONE);
								init();
								scroll.setVisibility(View.VISIBLE);
								drawer.setVisibility(View.VISIBLE);
								setListReportSteer(s_id_task);
								
							}
							
							@Override
							public void onStart() {
								drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
								back = (ImageView) findViewById(R.id.back);
								scroll = (ScrollView) findViewById(R.id.scroll);
								loading1 = (ProgressBar) findViewById(R.id.loading1);
								retry1 = (Button) findViewById(R.id.retry1);
								connectError1 = (LinearLayout) findViewById(R.id.connect_error1);
								retry1.setOnClickListener(DetailTaskActivity.this);
								
								loading1.setVisibility(View.VISIBLE);
								connectError1.setVisibility(View.GONE);
								scroll.setVisibility(View.GONE);
								drawer.setVisibility(View.GONE);
							}
							
							@Override
							public void onFalse() {
								loading1.setVisibility(View.GONE);
								connectError1.setVisibility(View.VISIBLE);
								
							}
						});
	}
	
	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.attach_file:
			// open ViewDispatch
			if (!task.getDinh_kem().equals("")) {
				viewDispatch = new ViewDispatch(DetailTaskActivity.this, task.getDinh_kem());
			} else

				Toast.makeText(
						getApplicationContext(), getResources().getString(
								R.string.no_attach), Toast.LENGTH_SHORT).show();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			setListReportSteer("" + id_task);
			break;
		case R.id.retry1:
			setDetailTask(s_id_task);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ReportSteerTask reportSteerTask = (ReportSteerTask) arg0.getAdapter()
				.getItem(arg2);
		if (!reportSteerTask.getFile().equals("")) {
			viewDispatch = new ViewDispatch(DetailTaskActivity.this,
					reportSteerTask.getFile());
		} else {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.no_attach),
					Toast.LENGTH_SHORT).show();
		}
	}

}