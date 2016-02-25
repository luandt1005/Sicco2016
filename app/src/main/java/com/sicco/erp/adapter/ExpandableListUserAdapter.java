package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;

public class ExpandableListUserAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<Department> listDep;
	private HashMap<String, ArrayList<User>> listUser;
	private ArrayList<User> listChecked;

	
	public ExpandableListUserAdapter(Context mContext,
			ArrayList<Department> listDep,
			HashMap<String, ArrayList<User>> listUser, ArrayList<User> listChecked) {
		this.context = mContext;
		this.listDep = listDep;
		this.listUser = listUser;
		this.listChecked = listChecked;
	}

	public void setListDep(ArrayList<Department> listDep) {
		this.listDep = listDep;
	}

	public void setListUser(HashMap<String, ArrayList<User>> listUser) {
		this.listUser = listUser;
	}

	@Override
	public int getGroupCount() {
		return listDep.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Department department = listDep.get(groupPosition);
		return listUser.get(department.getDepartmentName()).size();
	}

	@Override
	public Department getGroup(int groupPosition) {
		return listDep.get(groupPosition);
	}

	@Override
	public User getChild(int groupPosition, int childPosition) {
		return listUser.get(listDep.get(groupPosition).getDepartmentName())
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Department department = getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater
					.inflate(R.layout.item_group_department, null);
		}
		TextView departmentName = (TextView) convertView
				.findViewById(R.id.departmentName);
		departmentName.setText(department.getDepartmentName());
		departmentName.setTag(department.getId());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final User user = getChild(groupPosition, childPosition);
		CheckBox username;
		boolean createNew = true;
		//Check if getChildView is recalled with the same groupPos & childPos => ignore create new view
		if(convertView != null){
			username = (CheckBox) convertView
					.findViewById(R.id.userName);
			if(username != null){
				User tag = (User) username.getTag();
				if(tag != null){
					if(tag.equal(user)) createNew = false;
				}
			}
		}
		//inflate new view
		if (createNew) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.item_user, null);
		}
		 
		username = (CheckBox) convertView
				.findViewById(R.id.userName);
		
		if (!listChecked.isEmpty()) {
			for (int i = 0; i < listChecked.size(); i++) {
				if (listChecked.get(i).equal(user)) {
					username.setChecked(true);
					break;
				} else {
					username.setChecked(false);
				}
			}
		}
		username.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					if (listChecked.isEmpty()) {
						listChecked.add(user);
					} else {
						boolean exist = false;
						for (int i = 0; i < listChecked.size(); i++) {
							if (listChecked.get(i).equal(user)) {
								exist = true;
							}
						}
						if (exist == false) {
							listChecked.add(user);
						}
					}
				} else {
					for (int i = 0; i < listChecked.size(); i++) {
						if (listChecked.get(i).equal(user)) {
							listChecked.remove(listChecked.get(i));
						}
					}
				}
			}
		});
		username.setText(user.getUsername());
		username.setTag(user);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
