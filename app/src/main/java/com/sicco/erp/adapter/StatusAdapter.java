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

public class StatusAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Status> listStatus;

	public StatusAdapter(Context context, ArrayList<Status> lisStatus) {
		this.context = context;
		this.listStatus = lisStatus;
	}

	public ArrayList<Status> getListStatus() {
		return listStatus;
	}



	public void setListStatus(ArrayList<Status> listStatus) {
		this.listStatus = listStatus;
	}



	@Override
	public int getCount() {
		return listStatus.size();
	}

	@Override
	public Status getItem(int arg0) {
		return listStatus.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return listStatus.get(arg0).getKey();
	}


	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		final Status status = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_status,
					viewGroup, false);
			holder = new ViewHolder();
			holder.tvStatus = (TextView) view.findViewById(R.id.ckStatus);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvStatus.setText(status.getStatus());
		return view;
	}

	private class ViewHolder {
		TextView tvStatus;
	}
}
