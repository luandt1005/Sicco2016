package com.sicco.task.erp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Project;
import com.sicco.erp.model.User;
import com.sicco.erp.util.ChooseFileActivity;
import com.sicco.erp.util.DialogChooseDepartment;
import com.sicco.erp.util.DialogChooseHandler;
import com.sicco.erp.util.DialogChooseProject;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;
import com.sicco.task.adapter.SpinnerPriorityAdapter;
import com.sicco.task.model.Priority;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;

public class AssignTaskActivity extends ChooseFileActivity implements
		OnClickListener {
	private static int FLAG_DATE_PICKER = 1;
	private ImageView back;
	private ScrollView scrollView;
	private LinearLayout lnDateHandle, lnHandler, lnViewer, lnDepartment,
			lnDateFinish, lnFile, lnProject, lnTitle, lnDescription,lnPriority;

	private TextView txtDateHandle;
	private TextView txtDateFinish,txtPriority;
	private Spinner  spnPriority;
	public static TextView txtDepartment, txtHandler, txtViewer, txtProject;
	private EditText edtDes;
	private EditText edtTitle;
	private Button btnAssign;
	private TextView txtFile;

	private ArrayList<Department> listDep;
	private ArrayList<Project> listProject;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked, listCheckedHandler;
	private Task task;

	static final int DATE_DIALOG_ID = 111;
	private int date;
	private int months;
	private int years_now;

	private StringBuilder toDate;
	private Department department;
	private Project project;
	private User user;
	private File file;
	private String path;

	private SpinnerPriorityAdapter spinnerPriorityAdapter;
	private ArrayList<Priority> listPriority;
	private String keyPriority;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assign_task);

		DialogChooseHandler.VIEW_CURRENT = 1;
		DialogChooseProject.VIEW_CURRENT = 1;

		init();	
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);

		scrollView 		= (ScrollView) 	 findViewById(R.id.scrMain);
		lnTitle 		= (LinearLayout) findViewById(R.id.lnTitle);
		lnDescription 	= (LinearLayout) findViewById(R.id.lndes);
		lnDateHandle 	= (LinearLayout) findViewById(R.id.lnDateHandle);
		lnDateFinish 	= (LinearLayout) findViewById(R.id.lnDateFinish);
		lnHandler 		= (LinearLayout) findViewById(R.id.lnHandler);
		lnViewer 		= (LinearLayout) findViewById(R.id.lnViewer);
		lnDepartment 	= (LinearLayout) findViewById(R.id.lnDepartment);
		lnFile 			= (LinearLayout) findViewById(R.id.lnFile);
		lnProject 		= (LinearLayout) findViewById(R.id.lnProject);
		lnPriority		= (LinearLayout) findViewById(R.id.lnPriority);
		
		txtDateHandle 	= (TextView) findViewById(R.id.txtDateHandle);
		txtDateFinish 	= (TextView) findViewById(R.id.txtDateFinish);
//		txtPriority   	= (TextView) findViewById(R.id.txtPriority);
		txtHandler 		= (TextView) findViewById(R.id.txtHandler);
		txtViewer 		= (TextView) findViewById(R.id.txtViewer);
		txtDepartment = (TextView) findViewById(R.id.txtDepartment);
		txtFile = (TextView) findViewById(R.id.txtFile);
		txtProject = (TextView) findViewById(R.id.txtProject);
		
		//----------------------spinner Priority -----------------------------///
		spnPriority		= (Spinner)	 findViewById(R.id.spnPriority);
		listPriority	= new ArrayList<Priority>();
		
		listPriority.add(new Priority(getResources().getString(R.string.low), "0"));
		listPriority.add(new Priority(getResources().getString(R.string.medium), "1"));
		listPriority.add(new Priority(getResources().getString(R.string.hight), "2"));
		spinnerPriorityAdapter = new SpinnerPriorityAdapter(getApplicationContext(), listPriority);

		spnPriority.setAdapter(spinnerPriorityAdapter);
		spnPriority.setSelection(1);
		spnPriority.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Priority priority = (Priority)parent.getAdapter().getItem(position);
				
				keyPriority = priority.getKeyPriority();
//				Status status = (Status) parent.getAdapter().getItem(position);
//				Log.d("NgaDV", "priority.getKey(): " + pr.getKey());

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		//----------------------spinner Priority -----------------------------///
		edtDes = (EditText) findViewById(R.id.edtDes);
		edtTitle = (EditText) findViewById(R.id.edtTitle);

		btnAssign = (Button) findViewById(R.id.btnAssign);

		// click
		back.setOnClickListener(this);

		lnDateHandle.setOnClickListener(this);
		lnDateFinish.setOnClickListener(this);
		lnPriority.setOnClickListener(this);
		lnHandler.setOnClickListener(this);
		lnViewer.setOnClickListener(this);
		lnDepartment.setOnClickListener(this);
		lnFile.setOnClickListener(this);
		lnProject.setOnClickListener(this);
//		spnPriority.setOnClickListener(l)
		btnAssign.setOnClickListener(this);

		listChecked = new ArrayList<User>();
		listCheckedHandler = new ArrayList<User>();

		department = new Department();
		project = new Project();
		user = new User();
		listDep = new ArrayList<Department>();
		listProject = new ArrayList<Project>();
		allUser = new ArrayList<User>();

		listDep = department.getData(getResources().getString(
				R.string.api_get_deparment));
		listProject = project.getData(getResources().getString(
				R.string.api_get_project));
		allUser = user.getData(getResources().getString(
				R.string.api_get_all_user));

		// set data
		final Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);

		toDate = new StringBuilder().append(padding_str(years_now)).append("-")
				.append(padding_str(months + 1)).append("-")
				.append(padding_str(date));

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			if (FLAG_DATE_PICKER == 1) {
				txtDateHandle.setTextColor(Color.parseColor(getResources()
						.getString(R.color.actionbar_color)));
				txtDateHandle.setText(new StringBuilder()
						.append(padding_str(years_now)).append("-")
						.append(padding_str(months + 1)).append("-")
						.append(padding_str(date)));
			} else {
				txtDateFinish.setTextColor(Color.parseColor(getResources()
						.getString(R.color.actionbar_color)));
				txtDateFinish.setText(new StringBuilder()
						.append(padding_str(years_now)).append("-")
						.append(padding_str(months + 1)).append("-")
						.append(padding_str(date)));
			}

		}

	};

	@Deprecated
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				datePickerListener, years_now, months, date);
		datePickerDialog.getDatePicker().setSpinnersShown(false);
		datePickerDialog.getDatePicker().setCalendarViewShown(true);
		return datePickerDialog;
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			// clear data
			listChecked.clear();
			listCheckedHandler.clear();
			DialogChooseHandler.strUsersHandl = getResources().getString(
					R.string.handler1);
			DialogChooseHandler.idUsersHandl = "";
			DialogChooseUser.strUsersView = getResources().getString(
					R.string.viewer);
			DialogChooseUser.idUsersView = "";
			finish();
			break;
		case R.id.lnDateHandle:
			FLAG_DATE_PICKER = 1;
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnDateFinish:
			FLAG_DATE_PICKER = 0;
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnHandler:
			new DialogChooseHandler(AssignTaskActivity.this, listDep, allUser,
					listCheckedHandler);
			break;
		case R.id.lnViewer:
			ActionAdapter.flag = "chooseViewer";
			new DialogChooseUser(AssignTaskActivity.this, listDep, allUser,
					listChecked);
			break;
		case R.id.lnDepartment:
			new DialogChooseDepartment(AssignTaskActivity.this, listDep);
			break;
		case R.id.lnFile:
			showFileChooser();
			break;
		case R.id.lnProject:
			new DialogChooseProject(AssignTaskActivity.this, listProject);
			break;
		case R.id.btnAssign:

			if (validatFromAssignTask()) {
				final ProgressDialog progressDialog = new ProgressDialog(
						AssignTaskActivity.this);
				progressDialog.setMessage(getResources().getString(
						R.string.waiting));

				Task task = new Task(getApplicationContext());
				try {
					task.assignTask(
							getResources().getString(R.string.api_add_task),
							Long.parseLong(Utils.getString(
									AssignTaskActivity.this, "user_id")),
							edtTitle.getText().toString().trim(), edtDes
									.getText().toString().trim(), txtDateHandle
									.getText().toString().trim(), txtDateFinish
									.getText().toString().trim(), ""
									+ DialogChooseHandler.idUsersHandl,
							txtHandler.getText().toString().trim(), ""
									+ DialogChooseUser.idUsersView, txtViewer
									.getText().toString().trim(), "", ""
									+ DialogChooseDepartment.idDepSelected, ""
									+ DialogChooseProject.idProjectSelected,
							path,
							keyPriority,new OnLoadListener() {

								@Override
								public void onSuccess() {
									progressDialog.dismiss();
									Toast.makeText(
											AssignTaskActivity.this,
											getResources().getString(
													R.string.success),
											Toast.LENGTH_LONG).show();

									// clear data
									listChecked.clear();
									listCheckedHandler.clear();
									DialogChooseHandler.strUsersHandl = getResources()
											.getString(R.string.handler1);
									DialogChooseHandler.idUsersHandl = "";
									DialogChooseUser.strUsersView = getResources()
											.getString(R.string.viewer);
									DialogChooseUser.idUsersView = "";

									finish();
								}

								@Override
								public void onStart() {
									progressDialog.show();
								}

								@Override
								public void onFalse() {
									listChecked.removeAll(listChecked);
									progressDialog.dismiss();
									listChecked.removeAll(listChecked);
								}
							});
				} catch (NumberFormatException e) {
					Toast.makeText(AssignTaskActivity.this,
							getResources().getString(R.string.file_error),
							Toast.LENGTH_LONG).show();
					progressDialog.dismiss();
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					Toast.makeText(AssignTaskActivity.this,
							getResources().getString(R.string.file_error),
							Toast.LENGTH_LONG).show();
					progressDialog.dismiss();
					e.printStackTrace();
				} catch (NotFoundException e) {
					Toast.makeText(AssignTaskActivity.this,
							getResources().getString(R.string.file_error),
							Toast.LENGTH_LONG).show();
					progressDialog.dismiss();
					e.printStackTrace();
				}

			}
			break;
		}
	}

	private boolean validatFromAssignTask() {
		boolean validate = true;
		String title = edtTitle.getText().toString().trim();
		String description = edtDes.getText().toString().trim();
		String date_handle = txtDateHandle.getText().toString().trim();
		String date_finish = txtDateFinish.getText().toString().trim();
		String handler = txtHandler.getText().toString().trim();
		String viewer = txtViewer.getText().toString().trim();
		String department = txtDepartment.getText().toString().trim();

		if (title.equals("")) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.title_null),
					Toast.LENGTH_SHORT).show();
			edtTitle.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			edtTitle.requestFocus();
			scrollView.setScrollY((int) lnTitle.getY());
			validate = false;

		} else if (description.equals("")) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.description_null),
					Toast.LENGTH_SHORT).show();
			edtDes.requestFocus();
			edtDes.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnDescription.getY());
			validate = false;

		} else if (date_handle.equals(getResources().getString(
				R.string.date_handle))) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.date_handle_null),
					Toast.LENGTH_SHORT).show();
			lnDateHandle.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnDateHandle.getY());
			validate = false;

		} else if (date_finish.equals(getResources().getString(
				R.string.date_finish))) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.date_finish_null),
					Toast.LENGTH_SHORT).show();
			lnDateFinish.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnDateFinish.getY());
			validate = false;

		} else if (handler.equals(getResources().getString(R.string.handler1))) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.handler_null),
					Toast.LENGTH_SHORT).show();
			lnHandler.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnHandler.getY());
			validate = false;

		} else if (viewer.equals(getResources().getString(R.string.viewer))) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.viewer_null),
					Toast.LENGTH_SHORT).show();
			lnViewer.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnViewer.getY());
			validate = false;

		} else if (department.equals(getResources().getString(
				R.string.department))) {

			Toast.makeText(AssignTaskActivity.this,
					getResources().getString(R.string.department_null),
					Toast.LENGTH_SHORT).show();
			lnDepartment.startAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int) lnDepartment.getY());
			validate = false;

		} else {

			validate = true;

		}

		return validate;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				path = uri.getPath();

				file = new File(path);

				txtFile.setText(file.getName());
				txtFile.setTextColor(Color.parseColor(getResources().getString(
						R.color.actionbar_color)));
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		DialogChooseHandler.VIEW_CURRENT = 0;
		super.onDestroy();
	}
}