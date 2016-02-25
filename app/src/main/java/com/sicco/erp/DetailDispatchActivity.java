package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.ReportSteer.OnLoadListener;

public class DetailDispatchActivity extends Activity{
	private ImageView back;
	private TextView title, shCongVan, trichYeu, nguoiPheDuyet, nguoiXuLy, emptyView;
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
	
	private void init(){
		//view
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		shCongVan  =(TextView) findViewById(R.id.shCongVan);
		trichYeu  =(TextView) findViewById(R.id.trichYeu);
		nguoiPheDuyet  =(TextView) findViewById(R.id.nguoiPheDuyet);
		nguoiXuLy  =(TextView) findViewById(R.id.nguoiXuLy);
		baoCao = (ListView) findViewById(R.id.baoCao);
		loading = (ProgressBar) findViewById(R.id.loading);
		emptyView = (TextView) findViewById(R.id.emptyView);
		connectError = (LinearLayout) findViewById(R.id.connectError);
		retry = (Button) findViewById(R.id.retry);
		
		//listener
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
		shCongVan.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.sh_cong_van) + "</i></u></b></font>" + " " + dispatch.getNumberDispatch()));
		trichYeu.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.trich_yeu) + "</i></u></b></font>" + " " + dispatch.getDescription()));
		nguoiPheDuyet.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.nguoi_phe_duyet) + "</i></u></b></font>" + " " + dispatch.getNguoi_phe_duyet()));
		nguoiXuLy.setText(Html.fromHtml("<font><b><u><i>" + getResources().getString(R.string.nguoi_xu_ly) + "</i></u></b></font>" + " " + dispatch.getHandler()));
	}
	
	private void setListReportSteer(Dispatch dispatch) {
		arrBaoCao = reportSteer.getData(
				getResources().getString(R.string.api_get_steer_report),
				SessionManager.KEY_USER_ID, Long.toString(dispatch.getId()),
				new OnLoadListener() {
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

		baoCaoAdapter = new ReportSteerAdapter(getApplicationContext(),
				arrBaoCao);

		baoCao.setAdapter(baoCaoAdapter);
	}
}
