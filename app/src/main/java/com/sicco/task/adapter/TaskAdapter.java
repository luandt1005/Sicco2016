package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.task.erp.DetailTaskActivity;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogConfirmDeleteTask;

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Task> data;
	private int type;
	NotificationDBController db;
	Cursor cursor;

	public TaskAdapter(Context context, ArrayList<Task> data, int type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;
		
	}

	public ArrayList<Task> getData() {
		return data;
	}

	public void setData(ArrayList<Task> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Task getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		final ViewHolder holder;
		final Task task = getItem(arg0);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_task,
					arg2, false);
			holder = new ViewHolder();
			holder.taskName = (TextView) view.findViewById(R.id.taskName);
			holder.handler = (TextView) view.findViewById(R.id.handler);
			holder.date_handle = (TextView) view.findViewById(R.id.date_handle);
			holder.date_finish = (TextView) view.findViewById(R.id.date_finish);
			holder.action = (TextView) view.findViewById(R.id.action);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		if (type == 2) {
			long id = task.getId();
			String state = querryFromDB(context, id);
			if (state
					.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)) {
				view.setBackgroundColor(context.getResources().getColor(
						R.color.item_color));
			} else {
				view.setBackgroundColor(Color.WHITE);
			}
		}

		String date_handle_no_time = task.getNgay_bat_dau().substring(0,10);
		String date_finish_no_time = task.getNgay_ket_thuc().substring(0,10);
		String handler = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.nguoi_thuc_hien)
				+ "</i></u></b></font>" + "  " + task.getNguoi_thuc_hien();
		String date_handle = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.ngaygiao)
				+ "</i></u></b></font>" + ":  " + date_handle_no_time;
		String date_finish = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.hancuoi)
				+ "</i></u></b></font>" + ":  " + date_finish_no_time;

		holder.taskName.setText(task.getTen_cong_viec());
		holder.handler.setText(Html.fromHtml(handler));
		holder.date_handle.setText(Html.fromHtml(date_handle));
		holder.date_finish.setText(Html.fromHtml(date_finish));

		String colorAction = context.getResources().getString(R.color.actionbar_color);
		//mh danh sach viec
		if(type == 3){
			//if (!task.isCo_binh_luan()) {
			//	colorAction = "#aa0000";
			//}
			//if (!task.isCo_binh_luan() && task.getMuc_uu_tien().equals("2")) {
			//	colorAction = "#ff0000";
			//}
			//if (task.isCo_binh_luan()) {
			//	colorAction = "#5E7AF8";
			//}
			//if (task.isDa_qua_han() && task.isCo_binh_luan()) {
			//	colorAction = "#DF06D2";
			//}
			if (task.getTrang_thai().equals("complete")) {
				colorAction = "#01C853";
			}
			if (task.getTrang_thai().equals("inactive")) {
				colorAction = "#868B86";
			}
			if (task.getTrang_thai().equals("cancel")) {
				colorAction = "#A26637";
			}
		}
		
		//mh duoc giao
		if(type == 2){
			if(task.getDaxuly().equals("0")){
				colorAction = "#aa0000";
			} else {
				colorAction = colorAction;
			}
		}
		
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, 0});
        drawable.setColor(Color.parseColor(colorAction));
        drawable.setCornerRadius(context.getResources().getDimension(R.dimen.item_size));
		holder.action.setBackgroundDrawable(drawable);
		holder.action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PopupMenu popupMenu = new PopupMenu(context, holder.action);
				if (type == 1) {
					popupMenu.getMenuInflater().inflate(R.menu.assigned_task,
							popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater()
							.inflate(R.menu.assigned_task1,
									popupMenu.getMenu());
				}

				popupMenu.show();
				popupMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Intent intent = new Intent();
								switch (item.getItemId()) {
								case R.id.action_report:
									intent.setClass(context,
											SteerReportTaskActivity.class);
									intent.putExtra("task", task);
									context.startActivity(intent);
									break;
								case R.id.action_detail:
									intent.setClass(context,
											DetailTaskActivity.class);
									intent.putExtra("task", task);
									context.startActivity(intent);
									break;
								case R.id.action_delete:
									new DialogConfirmDeleteTask(context, task);
									break;

								}
								return false;
							}
						});
			}
		});

		return view;
	}

	private class ViewHolder {
		private TextView taskName;
		private TextView handler;
		private TextView date_handle;
		private TextView date_finish;
		private TextView action;
	}
	
	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.ID_COL + " = " + position;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TRANGTHAI_COL));
			} while (cursor.moveToNext());
		}
		return state;
	}

}
