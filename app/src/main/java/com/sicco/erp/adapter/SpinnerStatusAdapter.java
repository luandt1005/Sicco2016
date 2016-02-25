package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.Status;

public class SpinnerStatusAdapter extends BaseAdapter {
	Context context;
	ArrayList<Status> listStatus;

	public SpinnerStatusAdapter(Context context, ArrayList<Status> listStatus) {
		this.context = context;
		this.listStatus = listStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_status_dispatch, parent, false);
		TextView tvItemStatus = (TextView) view
				.findViewById(R.id.txtStatusDispatch);
		Status status = getItem(position);
		tvItemStatus.setText(status.getStatus());
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_status_dispatch, parent, false);
		TextView tvItemStatus = (TextView) view
				.findViewById(R.id.txtStatusDispatch);
		Status status = getItem(position);
		tvItemStatus.setText(status.getStatus());

		return view;
	}

	@Override
	public int getCount() {
		return listStatus.size();
	}

	@Override
	public Status getItem(int position) {
		return listStatus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
