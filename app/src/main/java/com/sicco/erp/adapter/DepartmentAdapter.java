package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.Department;

public class DepartmentAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Department> data;

	public DepartmentAdapter(Context context, ArrayList<Department> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<Department> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Department getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	public ArrayList<Department> getData() {
		return data;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		final Department dispatch = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_status,
					viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.ckStatus);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText(dispatch.getDepartmentName());
		return view;
	}

	private class ViewHolder {
		TextView title;
	}
}
