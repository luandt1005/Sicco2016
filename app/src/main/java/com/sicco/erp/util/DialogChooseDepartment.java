package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
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
import android.widget.TextView;

import com.sicco.erp.ConvertDispatchActivity;
import com.sicco.erp.R;
import com.sicco.erp.adapter.DepartmentAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Department.OnLoadListener;
import com.sicco.task.erp.AssignTaskActivity;

public class DialogChooseDepartment {

	private Context context;
	private ArrayList<Department> listDep;
	private DepartmentAdapter adapter;
	private ListView listView;
	private ProgressBar loading;
	private Button retry, btnDone;
	private LinearLayout connectError;

	private Department department;
	public static long idDepSelected;
	private boolean clickItem = false;

	public DialogChooseDepartment(Context context, ArrayList<Department> listDep) {
		this.context = context;
		this.listDep = listDep;

		department = new Department();
		
		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_change_status_dispatch,
				null);
		layout.setMinimumWidth((int) (rect.width() * 1f));

		TextView title = (TextView) layout.findViewById(R.id.title_actionbar);
		title.setText(context.getResources().getString(
				R.string.chose_dipartment));

		listView = (ListView) layout.findViewById(R.id.lvStatus);
		btnDone = (Button) layout.findViewById(R.id.done);

		// ------------
		loading = (ProgressBar) layout.findViewById(R.id.loading);
		retry = (Button) layout.findViewById(R.id.retry);
		connectError = (LinearLayout) layout.findViewById(R.id.connect_error);
		if (!Department.getJsonDep) {
			btnDone.setVisibility(View.GONE);
			getData();
		}

		adapter = new DepartmentAdapter(context, listDep);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				clickItem = true;
				department = (Department) arg0.getAdapter().getItem(arg2);
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		final AlertDialog alertDialog = builder.show();
		ImageView imgBack = (ImageView) layout.findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (clickItem) {
					if (DialogChooseHandler.VIEW_CURRENT == 1) {
						AssignTaskActivity.txtDepartment.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
						AssignTaskActivity.txtDepartment.setText(department.getDepartmentName());
					}else if (DialogChooseHandler.VIEW_CURRENT == 2) {
						ConvertDispatchActivity.txtDepartment.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
						ConvertDispatchActivity.txtDepartment.setText(department
							.getDepartmentName());
					}
					idDepSelected = department.getId();
					alertDialog.dismiss();
				}else{
					alertDialog.dismiss();
				}
			}
		});
		// click retry
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
			}
		});
	}

	public void getData() {
		listDep = department.getData(
				context.getResources().getString(R.string.api_get_deparment),
				new OnLoadListener() {

					@Override
					public void onSuccess() {
						adapter.setData(listDep);
						
						loading.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						btnDone.setVisibility(View.VISIBLE);
						
						adapter.notifyDataSetChanged();
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
	}

}
