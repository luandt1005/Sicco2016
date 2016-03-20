package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.sicco.erp.DetailDispatchActivity;
import com.sicco.erp.R;
import com.sicco.erp.SteerReportActivity;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Status;

public class ActionAdapter extends BaseAdapter {
	private Context context;
	ArrayList<Status> listStatus;
	private ArrayList<Dispatch> data;
	public static String flag = "";

	private Cursor cursor;
	private NotificationDBController db;
	int type;

	public ActionAdapter(Context context, ArrayList<Dispatch> data, int type) {
		this.context = context;
		this.data = data;
		this.type = type;

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

		holder.title.setText(dispatch.getNumberDispatch());
		holder.description.setText(dispatch.getDescription());
		
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
					popupMenu.getMenuInflater().inflate(R.menu.menu_task,
							popupMenu.getMenu());

					popupMenu.show();
					popupMenu
							.setOnMenuItemClickListener(new OnMenuItemClickListener() {

								@Override
								public boolean onMenuItemClick(MenuItem item) {
									Intent intent = new Intent();
									switch (item.getItemId()) {
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
	}

}