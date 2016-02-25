package com.sicco.erp.adapter;

import java.util.ArrayList;

import com.sicco.erp.R;
import com.sicco.erp.model.Project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProjectAdapter extends BaseAdapter{
	private Context context;
	ArrayList<Project> listProject;

	public ProjectAdapter(Context context, ArrayList<Project> listProject) {
		this.context = context;
		this.listProject = listProject;
	}
	
	public void setListProject(ArrayList<Project> listProject) {
		this.listProject = listProject;
	}
	
	@Override
	public int getCount() {
		return listProject.size();
	}

	@Override
	public Project getItem(int position) {
		return listProject.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listProject.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;
		final Project project = getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_status, parent,false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.ckStatus);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.title.setText(project.getName());
		return convertView;
	}

	public ArrayList<Project> getListProject() {
		return listProject;
	}
	private class ViewHolder{
		TextView title;
	}
}
