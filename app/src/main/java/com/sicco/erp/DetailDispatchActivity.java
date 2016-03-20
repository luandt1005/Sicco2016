package com.sicco.erp;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.ReportSteer.OnLoadListener;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.ViewDispatch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailDispatchActivity extends Activity {
	private ImageView back, action;
	private TextView title, shCongVan, trichYeu, nguoiPheDuyet, nguoiXuLy, nguoiXem, loaiCongVan, ngayDen, emptyView;
	private ListView baoCao;
	private ProgressBar loading;
	private LinearLayout connectError;
	private Dispatch dispatch;
	private ReportSteer reportSteer;
	private ReportSteerAdapter baoCaoAdapter;
	private ArrayList<ReportSteer> arrBaoCao;
	private Button retry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_dispatch);
		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
		reportSteer = new ReportSteer(DetailDispatchActivity.this);
		init();
		setListReportSteer(dispatch);
	}

	private void init() {
		// view
		back = (ImageView) findViewById(R.id.back);
		action = (ImageView) findViewById(R.id.action);
		title = (TextView) findViewById(R.id.title);
		shCongVan = (TextView) findViewById(R.id.shCongVan);
		trichYeu = (TextView) findViewById(R.id.trichYeu);
		nguoiPheDuyet = (TextView) findViewById(R.id.nguoiPheDuyet);
		nguoiXuLy = (TextView) findViewById(R.id.nguoiXuLy);
		nguoiXem = (TextView) findViewById(R.id.nguoiXem);
		baoCao = (ListView) findViewById(R.id.baoCao);
		loading = (ProgressBar) findViewById(R.id.loading);
		emptyView = (TextView) findViewById(R.id.emptyView);
		connectError = (LinearLayout) findViewById(R.id.connectError);
		retry = (Button) findViewById(R.id.retry);

		//
		loaiCongVan = (TextView) findViewById(R.id.loaiCongVan);
		ngayDen = (TextView) findViewById(R.id.ngayDen);

		// listener
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setListReportSteer(dispatch);
			}
		});
		//
		title.setText(dispatch.getNumberDispatch());
		// shCongVan.setText(Html.fromHtml("<font><b><u><i>" +
		// getResources().getString(R.string.sh_cong_van) +
		// "</i></u></b></font>" + " " + dispatch.getNumberDispatch()));
		shCongVan.setText(Html.fromHtml("<font><b>" + dispatch.getNumberDispatch() + "</b></font>"));
		trichYeu.setText(Html.fromHtml(dispatch.getDescription()));
		nguoiPheDuyet.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.nguoi_phe_duyet)
				+ "</b></font>" + " " + dispatch.getNguoi_phe_duyet()));
		nguoiXuLy.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.nguoi_xu_ly) + "</b></font>"
				+ " " + dispatch.getHandler()));
		nguoiXem.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.nguoi_xem) + "</b></font>" + " "
				+ dispatch.getNguoiXem()));

		loaiCongVan.setText(Html.fromHtml(
				"<font><b>" + loaiCongVan.getText().toString() + "</b></font>" + " " + dispatch.getIdLoaicongvan()));
		ngayDen.setText(Html.fromHtml(
				"<font><b>" + ngayDen.getText().toString() + "</b></font>" + " " + formatDate(dispatch.getDate())));

		TextView file_attach_text = (TextView) findViewById(R.id.file_attach_text);

		if (dispatch.getContent().equals("")) {
			file_attach_text
					.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.task_attach_file)
							+ "</i></u></b></font>" + " " + getResources().getString(R.string.no_attach)));
		} else {
			File file = new File(dispatch.getContent());
			file_attach_text.setText(Html.fromHtml(
					"<font><b><u><i>" + getResources().getString(R.string.task_attach_file) + "</i></u></b></font>"
							+ " " + "<font color = '#358cd1'><u><i>" + file.getName() + "</i></u></font>"));
		}
		LinearLayout file_attach = (LinearLayout) findViewById(R.id.file_attach);
		file_attach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ViewDispatch(DetailDispatchActivity.this, dispatch.getContent());
			}
		});

		// approval = (TextView) findViewById(R.id.approval);
		// String colorAction = "#FFFFFF";
		// GradientDrawable drawable = new
		// GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new
		// int[]{0, 0});
		// drawable.setColor(Color.parseColor(colorAction));
		// drawable.setCornerRadius(getResources().getDimension(R.dimen.item_size));
		// approval.setBackgroundDrawable(drawable);

		action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PopupMenu popupMenu = new PopupMenu(DetailDispatchActivity.this, action);
				if (dispatch.da_xu_ly.contains(dispatch.getHandler()) && isReceivedDispatch(dispatch.getId())) {
					popupMenu.getMenuInflater().inflate(R.menu.menu_dispatch_without_detail, popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater().inflate(R.menu.menu_dispatch_without_receiver_cv_detail, popupMenu.getMenu());
				}
				popupMenu.show();
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.receive_dispatch:
							receiveDispatch();
							break;
						case R.id.action_steer:
							intent.setClass(DetailDispatchActivity.this, SteerReportActivity.class);
							intent.putExtra("dispatch", dispatch);
							startActivity(intent);
							finish();
							break;
						case R.id.btnChuyenTiepXuLy:
							Department department = new Department();
							ArrayList<User> listChecked = new ArrayList<User>();
							ArrayList<Department> listDep = new ArrayList<Department>();
							ArrayList<User> allUser = new ArrayList<User>();
							listDep = department.getData(getResources().getString(R.string.api_get_deparment));
							new DialogChooseUser(DetailDispatchActivity.this, dispatch, listDep, allUser, listChecked);
							break;
						case R.id.btnChuyenCVThanhCongViec:
							intent = new Intent();
							intent.setClass(DetailDispatchActivity.this, ConvertDispatchActivity.class);
							intent.putExtra("dispatch", dispatch);
							startActivity(intent);
							break;
						default:
							break;
						}
						return false;
					}
				});
			}
		});

	}

	private void setListReportSteer(Dispatch dispatch) {
		arrBaoCao = reportSteer.getData(getResources().getString(R.string.api_get_steer_report),
				SessionManager.KEY_USER_ID, Long.toString(dispatch.getId()), new OnLoadListener() {
					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						baoCaoAdapter.notifyDataSetChanged();

						if (ReportSteer.CHECK_TOTAL_DATA == 0) {
							baoCao.setEmptyView(emptyView);
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

		baoCaoAdapter = new ReportSteerAdapter(getApplicationContext(), arrBaoCao);

		baoCao.setAdapter(baoCaoAdapter);
	}

	private boolean isReceivedDispatch(long id) {
		boolean isReceivedDispatch = true;

		String received = "";

		NotificationDBController db = NotificationDBController.getInstance(DetailDispatchActivity.this);
		Cursor cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null, null, null, null, null, null);
		String sql = "Select * from " + NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DISPATCH_COL + " = " + id;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				received = cursor.getString(cursor.getColumnIndexOrThrow(NotificationDBController.D_RECEIVED_COL));
				if (received.equals(""))
					isReceivedDispatch = true;
				else
					isReceivedDispatch = false;
			} while (cursor.moveToNext());
		}

		return isReceivedDispatch;
	}

	private void receiveDispatch() {
		ReportSteer reportSteer = new ReportSteer(DetailDispatchActivity.this);
		reportSteer.sendReportSteer(dispatch);
	}

	private String formatDate(String dateNeedToFormat) {
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = form.parse(dateNeedToFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("dd/MM/yyyy");
		return postFormater.format(date);
	}

}
