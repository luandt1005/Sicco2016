package com.sicco.erp.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.ConvertDispatchActivity;
import com.sicco.erp.R;
import com.sicco.erp.SendApprovalActivity;
import com.sicco.erp.adapter.ExpandableListUserAdapter;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Department.OnLoadListener;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.User;
import com.sicco.task.erp.AssignTaskActivity;

public class DialogChooseUser {

	private Context context;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private HashMap<String, ArrayList<User>> listUser;
	private ArrayList<User> listChecked;
	private ExpandableListUserAdapter adapter;
	private int mCurrentExpandedGroup = -1;
	private String handler;
	public static String strUsersView = "";
	public static String idUsersView = "";

	private ExpandableListView listView;
	private ProgressBar loading;
	private Button retry, btnDone;
	private LinearLayout connectError;

	private Department department;
	private User user;
	private Dispatch dispatch;

	public DialogChooseUser() {
		// TODO Auto-generated constructor stub
	}
	
	public DialogChooseUser(Context context, ArrayList<Department> listDep,
			ArrayList<User> allUser, ArrayList<User> listChecked) {
		this.context = context;
		this.listDep = listDep;
		this.allUser = allUser;
		this.listChecked = listChecked;

		department = new Department();
		user = new User();

		listUser = getData(listDep, allUser);
		showDialog();
	}
	
	public DialogChooseUser(Context context, Dispatch dispatch, ArrayList<Department> listDep,
			ArrayList<User> allUser, ArrayList<User> listChecked) {
		this.context = context;
		this.dispatch = dispatch;
		this.listDep = listDep;
		this.allUser = allUser;
		this.listChecked = listChecked;

		department = new Department();
		user = new User();

		
		listUser = getData(listDep, allUser);
		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_chose_user, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
		layout.setMinimumHeight((int) (rect.height() * 1f));

		TextView title = (TextView) layout.findViewById(R.id.title_actionbar);
		if (ActionAdapter.flag.equals("")) {
			title.setText(context.getResources().getString(R.string.chose_user));
		} else if (ActionAdapter.flag.equals("handle")) {
			title.setText(context.getResources().getString(
					R.string.choose_handle));
		}

		listView = (ExpandableListView) layout
				.findViewById(R.id.listUser);
		btnDone = (Button) layout.findViewById(R.id.done);

		// ------------
		loading = (ProgressBar) layout.findViewById(R.id.loading);
		retry = (Button) layout.findViewById(R.id.retry);
		connectError = (LinearLayout) layout.findViewById(R.id.connect_error);
		if (!Department.getJsonDep && !User.getJsonUser) {
			btnDone.setVisibility(View.GONE);
			getData();
		}

		adapter = new ExpandableListUserAdapter(context, listDep, listUser,
				listChecked);
		listView.setAdapter(adapter);

		// click child
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {

				return true;
			}
		});

		// click group
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int arg2, long arg3) {
				int count = adapter.getGroupCount();
				for (int i = 0; i < count; i++) {
					if (i != arg2) {
						listView.collapseGroup(i);
					}
				}

				if (mCurrentExpandedGroup >= 0 && mCurrentExpandedGroup < count) {
					if (mCurrentExpandedGroup == arg2) {
						listView.collapseGroup(arg2);
						mCurrentExpandedGroup = -1;
					} else {
						listView.expandGroup(arg2, false);
					}
				} else {
					listView.expandGroup(arg2, false);
				}
				return true;
			}
		});

		// Expand
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				mCurrentExpandedGroup = arg0;
				listView.setSelectedGroup(arg0);

			}
		});

		// Collapse
		listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				View view = adapter.getGroupView(groupPosition, true, null,
						null);
				view.invalidate();

			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		final AlertDialog alertDialog = builder.show();
		ImageView imgBack = (ImageView) layout.findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler = context.getResources().getString(R.string.handler);
				for (int i = 0; i < listChecked.size(); i++) {
					handler += listChecked.get(i).getUsername() + "; ";
				}
				
				if (ActionAdapter.flag.equals("handle")) {
					if (listChecked.isEmpty()) {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.empty_handle),
								Toast.LENGTH_SHORT).show();
					} else {
						alertDialog.dismiss();
						new DialogShowHandler(context, dispatch,listChecked);

					}

				} else if (ActionAdapter.flag.equals("")) {
					SendApprovalActivity.txtHandler.setText(handler);
					alertDialog.dismiss();
				}else if (ActionAdapter.flag.equals("chooseViewer")) {
					strUsersView = "";
					idUsersView = "";
					for (int i = 0; i < listChecked.size(); i++) {
						if (i == listChecked.size() - 1) {
							strUsersView += listChecked.get(i).getUsername();
							idUsersView += listChecked.get(i).getId();
						} else {
							strUsersView += listChecked.get(i).getUsername() + ",";
							idUsersView += listChecked.get(i).getId() + ",";
						}
					}
					if (!strUsersView.equals("")) {
						if (DialogChooseHandler.VIEW_CURRENT == 1) {
							AssignTaskActivity.txtViewer.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
							AssignTaskActivity.txtViewer.setText(strUsersView);
						}else if (DialogChooseHandler.VIEW_CURRENT == 2) {
							ConvertDispatchActivity.txtViewer.setTextColor(Color.parseColor(context.getString(R.color.actionbar_color)));
							ConvertDispatchActivity.txtViewer.setText(strUsersView);
						}
						
					}
					if (listChecked.isEmpty()) {
						if (DialogChooseHandler.VIEW_CURRENT == 1) {
							AssignTaskActivity.txtViewer.setText(context.getResources().getString(R.string.viewer));
						}else if (DialogChooseHandler.VIEW_CURRENT == 2) {
							ConvertDispatchActivity.txtViewer.setText(context.getResources().getString(R.string.viewer));
						}
						
					}
					alertDialog.dismiss();
				}
				
			}
		});
		// click retry
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
			}
		});
	}

	public HashMap<String, ArrayList<User>> getData(
			ArrayList<Department> listDep, final ArrayList<User> users) {
		listUser = new HashMap<String, ArrayList<User>>();
		for (int i = 0; i < listDep.size(); i++) {
			Department department = listDep.get(i);
			ArrayList<User> data = new ArrayList<User>();
			for (int j = 0; j < users.size(); j++) {
				User temp = users.get(j);
				if (Integer.parseInt(temp.getDepartment()) == department.getId()) {
					data.add(new User(temp.getId(), temp.getUsername(), temp
							.getDepartment()));
				}
			}
			listUser.put(listDep.get(i).getDepartmentName(), data);
		}
		return listUser;
	}
	
	public void getData(){
		listDep = department.getData(
				context.getResources().getString(R.string.api_get_deparment),
				new OnLoadListener() {

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						//get all user
						allUser = user.getData(context.getResources().getString(R.string.api_get_all_user), new User.OnLoadListener() {
							
							@Override
							public void onSuccess() {
								listUser = getData(listDep, allUser);
								
								adapter.setListDep(listDep);
								adapter.setListUser(listUser);
								loading.setVisibility(View.GONE);
								listView.setVisibility(View.VISIBLE);
								btnDone.setVisibility(View.VISIBLE);
								adapter.notifyDataSetChanged();
							}

							@Override
							public void onStart() {
								loading.setVisibility(View.VISIBLE);
								connectError.setVisibility(View.GONE);

							}

							@Override
							public void onFalse() {
								loading.setVisibility(View.GONE);
								connectError.setVisibility(View.VISIBLE);
							}
						});
					}

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);

					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});
	}

}
