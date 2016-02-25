package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.task.model.Priority;

public class SpinnerPriorityAdapter extends BaseAdapter {
	Context context;
	ArrayList<Priority> listPriority;

	public SpinnerPriorityAdapter(Context context, ArrayList<Priority> listPriority) {
		this.context = context;
		this.listPriority = listPriority;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_priority_task, parent, false);
		TextView tvItemPriority = (TextView) view
				.findViewById(R.id.txtPriorityTask);
		Priority priority = getItem(position);
		tvItemPriority.setText(priority.getPriority());
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_status_dispatch, parent, false);
		TextView tvItemPriority = (TextView) view
				.findViewById(R.id.txtStatusDispatch);
		Priority priority = getItem(position);
		tvItemPriority.setText(priority.getPriority());

		return view;
	}

	@Override
	public int getCount() {
		return listPriority.size();
	}

	@Override
	public Priority getItem(int position) {
		return listPriority.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
