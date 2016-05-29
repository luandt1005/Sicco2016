package com.sicco.task.erp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.adapter.SpinnerStatusAdapter;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Keyboard;
import com.sicco.task.adapter.TaskAdapter;
import com.sicco.task.callback.OnSuccess;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogConfirmDeleteTask;
import com.sicco.task.ultil.DialogSetProcess;

import java.util.ArrayList;

public class OtherTaskActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private TextView emptyView;
	private ListView listTask;
	private ProgressBar loading;
	private Button retry;
	private Task task;
	private ArrayList<Task> arrTask;
	private TaskAdapter adapter;

	private TextView title_actionbar;

	private Button btnAssignNewTask;
	private Spinner spnFilter;
	private SpinnerStatusAdapter spinnerStatusAdapter;
	private long keyFilter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assigned_task);

		init();

	}

	@Override
	protected void onResume() {
		//spnFilter.setSelection(0);
		displayLisview();
		super.onResume();
	}

	private void init() {
		context = OtherTaskActivity.this;
		searchView = (LinearLayout) findViewById(R.id.searchview);
		back = (ImageView) findViewById(R.id.back);
		search = (ImageView) findViewById(R.id.search);
		close = (ImageView) searchView.findViewById(R.id.close);
		empty = (ImageView) searchView.findViewById(R.id.empty);
		editSearch = (EditText) searchView.findViewById(R.id.edit_search);
		emptyView = (TextView) findViewById(R.id.empty_view);
		listTask = (ListView) findViewById(R.id.listTask);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		title_actionbar = (TextView) findViewById(R.id.title_actionbar);
		btnAssignNewTask = (Button) findViewById(R.id.btnAssignNew);
		title_actionbar.setText(getResources().getString(
				R.string.danh_sach_viec));
		title_actionbar.setVisibility(View.GONE);
		spnFilter = (Spinner) findViewById(R.id.spnFilter);
		spnFilter.setVisibility(View.VISIBLE);
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listTask.setOnItemClickListener(this);
		btnAssignNewTask.setOnClickListener(this);
		btnAssignNewTask.setVisibility(View.GONE);

		//filter
		ArrayList<Status> listStatus = new ArrayList<Status>();
		listStatus.add(new Status(getResources().getString(R.string.all),
				Task.FILTER_ALL_TYPE));
		listStatus.add(new Status(
				getResources().getString(R.string.hoan_thanh),
				Task.FILTER_CVHT_TYPE));
		listStatus.add(new Status(getResources().getString(
				R.string.tam_dung), Task.FILTER_CVTD_TYPE));
		listStatus.add(new Status(getResources().getString(
				R.string.da_huy), Task.FILTER_CVDH_TYPE));

		spinnerStatusAdapter = new SpinnerStatusAdapter(
				getApplicationContext(), listStatus);
		spnFilter.setAdapter(spinnerStatusAdapter);

		spnFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Status status = (Status) parent.getAdapter().getItem(position);
				if (!arrTask.isEmpty()) {
					keyFilter = status.getKey();
					adapter.setData(task.filter(arrTask, status.getKey()));
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	// display lisview
	public void displayLisview() {
		// set adapter
		task = new Task(context);
		arrTask = new ArrayList<Task>();
		arrTask = task.getData(context,
				context.getString(R.string.api_get_task),
				new Task.OnLoadListener() {

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						adapter.setData(task.filter(arrTask, keyFilter));
						adapter.notifyDataSetChanged();
						if (adapter.getCount() <= 0) {
							listTask.setEmptyView(emptyView);
						}
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});
		adapter = new TaskAdapter(context, arrTask, 3);
		adapter.setOnActionClickLisstener(new TaskAdapter.OnActionClickLisstener() {
			@Override
			public void onClick(View view, final ArrayList<Status> listStatus, final ArrayList<Status> listProcess, int type, final Task task, final boolean isUpdateStatusAndRate) {
				final Context context = OtherTaskActivity.this;
				PopupMenu popupMenu = new PopupMenu(context, view);
				if (type == 1) {
					popupMenu.getMenuInflater().inflate(R.menu.assigned_task,
							popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater()
							.inflate(R.menu.assigned_task1,
									popupMenu.getMenu());
				}

				popupMenu.show();
				popupMenu
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Intent intent = new Intent();
								switch (item.getItemId()) {
									case R.id.action_report:
										intent.setClass(context,
												SteerReportTaskActivity.class);
										intent.putExtra("task", task);
										context.startActivity(intent);
										break;
									case R.id.action_detail:
										intent.setClass(context,
												DetailTaskActivity.class);
										intent.putExtra("task", task);
										context.startActivity(intent);
										break;
									case R.id.action_edit:
										intent = new Intent(context, EditTaskActivity.class);
										intent.putExtra("TASK", task);
										context.startActivity(intent);
										break;
									case R.id.action_delete:
										new DialogConfirmDeleteTask(context, task);
										break;
									case R.id.action_change_status:
										if (task.getTrang_thai().equals("complete")) {
											Toast.makeText(context,
													context.getResources().getString(R.string.not_update_status),
													Toast.LENGTH_SHORT).show();
										} else {
											if (isUpdateStatusAndRate) {
												DialogChangeStatusTask dialogChangeStatusTask = new DialogChangeStatusTask(context,
														listStatus, task);
												dialogChangeStatusTask.setOnSuccess(new OnSuccess() {
													@Override
													public void onSuccess() {
														onResume();
													}
												});
											} else {
												Toast.makeText(
														context,
														context.getResources().getString(
																R.string.info_update_status),
														Toast.LENGTH_SHORT).show();
											}
										}
										break;
									case R.id.action_update_rate:
										if (task.getTrang_thai().equals("complete")) {
											Toast.makeText(context,
													context.getResources().getString(R.string.not_update_rate),
													Toast.LENGTH_SHORT).show();
										} else {
											if (isUpdateStatusAndRate) {
												DialogSetProcess dialog = new DialogSetProcess(
														context, listProcess, task);
												dialog.setOnSuccess(new OnSuccess() {
													@Override
													public void onSuccess() {
														onResume();
													}
												});
												dialog.showDialog();
											} else {
												Toast.makeText(
														context,
														context.getResources().getString(R.string.info_update_rate),
														Toast.LENGTH_SHORT).show();
											}
										}
										break;

								}
								return false;
							}
						});
			}
		});
		listTask.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.search:
			showSearchView();
			break;
		case R.id.close:
			closeSearchView();
			break;
		case R.id.empty:
			editSearch.setText("");
			break;
		case R.id.btnAssignNew:
			Intent intent = new Intent(OtherTaskActivity.this,
					AssignTaskActivity.class);
			startActivity(intent);
			break;
		case R.id.retry:
			adapter.setData(task.getData(OtherTaskActivity.this, getResources()
					.getString(R.string.api_get_task),
					new Task.OnLoadListener() {

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
							if (adapter.getCount() <= 0) {
								listTask.setEmptyView(emptyView);
							}
						}

						@Override
						public void onFalse() {
							loading.setVisibility(View.GONE);
							connectError.setVisibility(View.VISIBLE);
						}
					}));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Task task = (Task) arg0.getAdapter().getItem(arg2);
		Intent intent = new Intent(this, DetailTaskActivity.class);
		intent.putExtra("task", task);
		intent.putExtra("TASK_TYPE", 3);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (searchView.getVisibility() == View.VISIBLE) {
			editSearch.setText("");
			searchView.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(OtherTaskActivity.this, editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.toString().trim().length() > 0) {
					empty.setVisibility(View.VISIBLE);
				} else {
					empty.setVisibility(View.GONE);
				}
				ArrayList<Task> searchData = task.search(
						arg0.toString().trim(), arrTask);
				adapter.setData(searchData);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	private void closeSearchView() {
		Keyboard.hideKeyboard(OtherTaskActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

}