package com.sicco.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.DispatchType;

import java.util.ArrayList;

public class SpinnerTypeAdapter extends BaseAdapter {
	Context context;
	ArrayList<DispatchType> listType;

	public SpinnerTypeAdapter(Context context, ArrayList<DispatchType> listType) {
		this.context = context;
		this.listType = listType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_status_dispatch, parent, false);
		TextView tvItemStatus = (TextView) view
				.findViewById(R.id.txtStatusDispatch);
		DispatchType type = getItem(position);
		tvItemStatus.setText(type.getTitle());
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_spinner_status_dispatch, parent, false);
		TextView tvItemStatus = (TextView) view
				.findViewById(R.id.txtStatusDispatch);
		DispatchType type = getItem(position);
		tvItemStatus.setText(type.getTitle());

		return view;
	}

	@Override
	public int getCount() {
		return listType.size();
	}

	@Override
	public DispatchType getItem(int position) {
		return listType.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
