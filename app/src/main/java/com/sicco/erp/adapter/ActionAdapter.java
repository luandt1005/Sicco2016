package com.sicco.erp.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sicco.erp.ConvertDispatchActivity;
import com.sicco.erp.DetailDispatchActivity;
import com.sicco.erp.R;
import com.sicco.erp.SteerReportActivity;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;

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

public class ActionAdapter extends BaseAdapter {
	private Context context;
	ArrayList<Status> listStatus;
	private ArrayList<Dispatch> data;
	public static String flag = "";

	private Cursor cursor;
	private NotificationDBController db;
	int type, activity_type = 0;

	public ActionAdapter(Context context, ArrayList<Dispatch> data, int type) {
		this.context = context;
		this.data = data;
		this.type = type;
	}

	public ActionAdapter(Context context, ArrayList<Dispatch> data, int type, int activity_type) {
		this.context = context;
		this.data = data;
		this.type = type;
		this.activity_type = activity_type; // 0 show receiver_congvan, 1 disable
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Dispatch getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;
		final Dispatch dispatch = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_dispatch,
					viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.description = (TextView) view.findViewById(R.id.description);
			holder.approval = (TextView) view.findViewById(R.id.approval);
			holder.date = (TextView) view.findViewById(R.id.date);
			if (type == 0) {
				holder.approval.setText(context.getResources().getString(
						R.string.xu_ly));
			} else {
				holder.approval.setText(context.getResources().getString(
						R.string.task));
			}
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String d = "<font weigth='bold'><b><i>" + formatDate(dispatch.getDate()) + "</i></b></font>";

		holder.title.setText(dispatch.getNumberDispatch());
		holder.description.setText(dispatch.getDescription());
		holder.date.setText(Html.fromHtml(d));
		
		String colorAction = context.getResources().getString(R.color.actionbar_color);
		if (type == 1 && dispatch.getStatus().equals("2")) {
			colorAction = context.getResources().getString(R.color.red);
		}
		
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, 0});
        drawable.setColor(Color.parseColor(colorAction));
        drawable.setCornerRadius(context.getResources().getDimension(R.dimen.item_size));
		holder.approval.setBackgroundDrawable(drawable);

		holder.approval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (type == 0) {
					Intent intent = new Intent();
					intent.setClass(context, SteerReportActivity.class);
					intent.putExtra("dispatch", dispatch);
					context.startActivity(intent);
				} else {
					// popupMenu
					PopupMenu popupMenu = new PopupMenu(context,
							holder.approval);
					boolean checkIsXuly = false;
					boolean checkDaXuly = false;
					final String userName = Utils.getString(context, "name");
					String[] nguoixuly = dispatch.getHandler().split(",");
					String[] daxuly = dispatch.getDa_xu_ly().split(",");
					for (int i = 0; i < nguoixuly.length; i++){
						if(nguoixuly[i].equals(userName)){
							checkIsXuly = true;
							break;
						}
					}
					for (int i = 0; i < daxuly.length; i++){
						if(daxuly[i].equals(userName)){
							checkDaXuly = true;
							break;
						}
					}
					if(activity_type == 1 && checkIsXuly && !checkDaXuly) {
						popupMenu.getMenuInflater().inflate(R.menu.menu_task, popupMenu.getMenu());
					} else{
						popupMenu.getMenuInflater().inflate(R.menu.menu_dispatch_without_receiver_cv, popupMenu.getMenu());
					}
					popupMenu.show();
					popupMenu
							.setOnMenuItemClickListener(new OnMenuItemClickListener() {

								@Override
								public boolean onMenuItemClick(MenuItem item) {
									Intent intent = new Intent();
									switch (item.getItemId()) {
									case R.id.receive_dispatch:
										receiveDispatch(dispatch);
										dispatch.setDa_xu_ly(userName);
										break;
									case R.id.action_steer:
										intent.setClass(context,
												SteerReportActivity.class);
										intent.putExtra("dispatch", dispatch);
										context.startActivity(intent);
										break;
									case R.id.action_detail:
										intent.setClass(context,
												DetailDispatchActivity.class);
										intent.putExtra("dispatch", dispatch);
										context.startActivity(intent);
										break;
									case R.id.btnChuyenTiepXuLy:
										ActionAdapter.flag = "handle";

										new DialogChooseUser(context, dispatch);
										break;
									case R.id.btnChuyenCVThanhCongViec:
										intent = new Intent();
										intent.setClass(context, ConvertDispatchActivity.class);
										intent.putExtra("dispatch", dispatch);
										context.startActivity(intent);
										break;
									default:
										break;
									}
									return false;
								}
							});
				}
			}
		});
		return view;
	}

	private class ViewHolder {
		TextView title;
		TextView description;
		TextView approval;
		TextView date;
	}

	private String formatDate(String dateNeedToFormat){
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try
		{
			date = form.parse(dateNeedToFormat);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("dd/MM/yyyy");
		return postFormater.format(date);
	}

	private boolean isReceivedDispatch(long id){
		boolean isReceivedDispatch = true;

		String received = "";

		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DISPATCH_COL + " = " + id;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				received = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_RECEIVED_COL));
				if(received.equals(""))
					isReceivedDispatch = true;
				else
					isReceivedDispatch = false;
			} while (cursor.moveToNext());
		}

		return isReceivedDispatch;
	}

	private void receiveDispatch(Dispatch dispatch){
		ReportSteer reportSteer = new ReportSteer(context);
		reportSteer.sendReportSteer(dispatch);
	}

}