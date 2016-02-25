package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
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
import com.sicco.erp.adapter.ProjectAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Project;
import com.sicco.erp.model.Project.OnLoadListener;
import com.sicco.task.erp.AssignTaskActivity;

public class DialogChooseProject {
	/*
	VIEW_CURRENT == 1 => AssignTaskActivity
	VIEW_CURRENT == 2 => ConvertDispatchActivity
 	*/
	public static int VIEW_CURRENT = 0;
	private Context context;
	private ArrayList<Project> listProject;
	
	private ListView lvProject;
	private ProjectAdapter projectAdapter;
	private ProgressBar loading;
	private Button btnDone,retry;
	private LinearLayout connectError;
	
	public static long idProjectSelected;
	private Project project;
	private boolean clickItem;
	public DialogChooseProject(Context context, ArrayList<Project> listProject) {
		this.context = context;
		this.project = new Project();
		this.listProject = listProject;
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
		
		lvProject = (ListView) layout.findViewById(R.id.lvStatus);
		btnDone = (Button) layout.findViewById(R.id.done);

		loading = (ProgressBar) layout.findViewById(R.id.loading);
		retry = (Button) layout.findViewById(R.id.retry);
		connectError = (LinearLayout) layout.findViewById(R.id.connect_error);
		if (!Project.getJsonProject) {
			btnDone.setVisibility(View.GONE);
			getData();
		}

		projectAdapter = new ProjectAdapter(context, listProject);
		lvProject.setAdapter(projectAdapter);
		lvProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				clickItem = true;
				project = (Project) arg0.getAdapter().getItem(arg2);
//				department = (Department) arg0.getAdapter().getItem(arg2);
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		final AlertDialog alertDialog = builder.show();
		ImageView imgBack = (ImageView) layout.findViewById(R.id.back);
		TextView tvTitle  = (TextView)	layout.findViewById(R.id.title_actionbar);
		
		tvTitle.setText(context.getString(R.string.choose_project));
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
					if (VIEW_CURRENT == 1) {
						AssignTaskActivity.txtProject.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
						AssignTaskActivity.txtProject.setText(project.getName());
					}else if (VIEW_CURRENT == 2) {
						ConvertDispatchActivity.txtProject.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
						ConvertDispatchActivity.txtProject.setText(project.getName());
					}
					idProjectSelected = project.getId();
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
	private void getData() {
		Log.d("NgaDV", "run getData()");
		listProject = project.getData(
				context.getString(R.string.api_get_project), 
				new OnLoadListener() {
			
					@Override
					public void onSuccess() {
						Log.d("NgaDV", "on success ngadv");
						projectAdapter.setListProject(listProject);
						
						loading.setVisibility(View.GONE);
						lvProject.setVisibility(View.VISIBLE);
						btnDone.setVisibility(View.VISIBLE);
						
						projectAdapter.notifyDataSetChanged();
					}
		
					@Override
					public void onStart() {
						Log.d("NgaDV", "on start ngadv");
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}
		
					@Override
					public void onFalse() {
						Log.d("NgaDV", "on False ngadv");
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
		});
//		return listProject;
	}
}
