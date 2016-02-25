package com.sicco.erp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Project;
import com.sicco.erp.model.Dispatch.OnRequestListener;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChooseDepartment;
import com.sicco.erp.util.DialogChooseHandler;
import com.sicco.erp.util.DialogChooseProject;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;
import com.sicco.task.adapter.SpinnerPriorityAdapter;
import com.sicco.task.erp.AssignTaskActivity;
import com.sicco.task.model.Priority;

public class ConvertDispatchActivity extends Activity implements
		OnClickListener {
	
	public static int DIALOG_DISPATCH = 1;
	private ImageView back;
	private LinearLayout 	lnFromDate,
							lnHandler, 
							lnViewer, 
							lnDepartment, 
							lnToDate,
							lnProject,
							lnPriority;
	private TextView txtFromDate;
	private TextView txtToDate;
	private TextView txtPriority;
	public static TextView txtDepartment,txtProject;
	private EditText edtTitleJob;
	private EditText edtDes;
	private Button btnConvert;

	public static TextView txtHandler, txtViewer;

	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked, listCheckedHandler;
	private Dispatch dispatch;

	private ArrayList<Project> listProject;
	private Project project;

	static final int DATE_DIALOG_ID = 111;
	private int date;
	private int months;
	private int years_now;

	private StringBuilder toDate;
	private Department department;
	private User user;
	private String file;

	private SpinnerPriorityAdapter spinnerPriorityAdapter;
	private ArrayList<Priority> listPriority;
	private String keyPriority;
	private Spinner  spnPriority;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_convert_dispatch);

		DialogChooseHandler.VIEW_CURRENT = 2;
		DialogChooseProject.VIEW_CURRENT = 2;

		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
		file = dispatch.getContent();
		file = file.replace("%20", " ");
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
//		lnJobType = (LinearLayout) findViewById(R.id.lnJobType);
		lnFromDate = (LinearLayout) findViewById(R.id.lnDateHandle);
//		lnStatus = (LinearLayout) findViewById(R.id.lnStatus);
//		lnProgress = (LinearLayout) findViewById(R.id.lnProgress);
//		lnLevel = (LinearLayout) findViewById(R.id.lnLevel);
		lnHandler = (LinearLayout) findViewById(R.id.lnHandler);
		lnViewer = (LinearLayout) findViewById(R.id.lnViewer);
		lnDepartment = (LinearLayout) findViewById(R.id.lnDepartment);
		lnToDate = (LinearLayout) findViewById(R.id.lnDateFinish);
		lnProject = (LinearLayout)findViewById(R.id.lnProject);
		lnPriority = (LinearLayout)findViewById(R.id.lnPriority);

		txtFromDate = (TextView) findViewById(R.id.txtDateHandle);
		txtHandler = (TextView) findViewById(R.id.txtHandler);
		txtViewer = (TextView) findViewById(R.id.txtViewer);
		txtDepartment = (TextView) findViewById(R.id.txtDepartment);
		txtToDate = (TextView) findViewById(R.id.txtDateFinish);
		txtProject	=	(TextView)findViewById(R.id.txtProject);
		txtPriority	=	(TextView)findViewById(R.id.txtPriorityTask);

		btnConvert = (Button) findViewById(R.id.btnConvert);

		edtTitleJob = (EditText) findViewById(R.id.edtTitle);
		edtDes = (EditText) findViewById(R.id.edtDes);

		txtFromDate.setTextColor(Color.parseColor(getResources()
				.getString(R.color.actionbar_color)));
		txtToDate.setTextColor(Color.parseColor(getResources()
				.getString(R.color.actionbar_color)));
		edtTitleJob.setTextColor(Color.parseColor(getResources()
				.getString(R.color.actionbar_color)));
		edtDes.setTextColor(Color.parseColor(getResources()
				.getString(R.color.actionbar_color)));
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
//						Status status = (Status) parent.getAdapter().getItem(position);
//						Log.d("NgaDV", "priority.getKey(): " + pr.getKey());

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
				//----------------------spinner Priority -----------------------------///
		// click
		back.setOnClickListener(this);

//		lnJobType.setOnClickListener(this);
		lnFromDate.setOnClickListener(this);
//		lnStatus.setOnClickListener(this);
//		lnProgress.setOnClickListener(this);
//		lnLevel.setOnClickListener(this);
		lnHandler.setOnClickListener(this);
		lnViewer.setOnClickListener(this);
		lnDepartment.setOnClickListener(this);
		lnToDate.setOnClickListener(this);
		lnProject.setOnClickListener(this);
		btnConvert.setOnClickListener(this);

		listChecked = new ArrayList<User>();
		listCheckedHandler = new ArrayList<User>();
		listProject = new ArrayList<Project>();

		department = new Department();
		project = new Project();
		user = new User();
		listDep = new ArrayList<Department>();
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

		edtTitleJob.setText(getResources().getString(R.string.action)
				+ dispatch.getNumberDispatch());
		edtDes.setText(dispatch.getDescription());
		txtFromDate.setText(toDate);
		txtToDate.setText(toDate);

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			txtToDate.setText(new StringBuilder()
					.append(padding_str(years_now)).append("-")
					.append(padding_str(months + 1)).append("-")
					.append(padding_str(date)));
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
//		case R.id.lnJobType:
//			Toast.makeText(getApplicationContext(),
//					getResources().getString(R.string.default_value),
//					Toast.LENGTH_SHORT).show();
//			break;
		case R.id.lnDateHandle:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
//		case R.id.lnStatus:
//			Toast.makeText(getApplicationContext(),
//					getResources().getString(R.string.default_value),
//					Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.lnProgress:
//			Toast.makeText(getApplicationContext(),
//					getResources().getString(R.string.default_value),
//					Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.lnLevel:
//			Toast.makeText(getApplicationContext(),
//					getResources().getString(R.string.default_value),
//					Toast.LENGTH_SHORT).show();
//			break;
		case R.id.lnHandler:

			new DialogChooseHandler(ConvertDispatchActivity.this, listDep,
					allUser, listCheckedHandler);

			break;
		case R.id.lnViewer:

			ActionAdapter.flag = "chooseViewer";
			new DialogChooseUser(ConvertDispatchActivity.this, listDep, allUser,
					listChecked);

			break;
		case R.id.lnDepartment:
			new DialogChooseDepartment(ConvertDispatchActivity.this, listDep);
			break;
		case R.id.lnDateFinish:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnProject:
			new DialogChooseProject(ConvertDispatchActivity.this, listProject);
			break;
		case R.id.btnConvert:

			File theFile = new File(file);
			
			boolean error = false;
			if (edtTitleJob.getText().toString().trim().equals("")) {
				edtTitleJob.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (edtDes.getText().toString().trim().equals("")) {
				edtDes.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (txtHandler.getText().toString()
					.equals(getResources().getString(R.string.handler1))) {
				txtHandler.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (txtViewer.getText().toString()
					.equals(getResources().getString(R.string.viewer))) {
				txtViewer.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (txtDepartment.getText().toString()
					.equals(getResources().getString(R.string.department))) {
				txtDepartment.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}

			if (error == false) {
				final ProgressDialog progressDialog = new ProgressDialog(
						ConvertDispatchActivity.this);
				progressDialog.setMessage(getResources().getString(
						R.string.waiting));
				
				Dispatch dispatch = new Dispatch(ConvertDispatchActivity.this);
				dispatch.convertDispatch(
						getResources().getString(R.string.api_chuyencongvan),
						Utils.getString(ConvertDispatchActivity.this, "user_id"),
						edtTitleJob.getText().toString(), 
						edtDes.getText().toString(), 
						txtFromDate.getText()
								.toString(), 
						txtToDate.getText().toString(), 
						DialogChooseHandler.idUsersHandl,
						txtHandler.getText().toString(), 
						DialogChooseUser.idUsersView, 
						txtViewer.getText().toString(), 
						""+ DialogChooseDepartment.idDepSelected,
						""+ DialogChooseProject.idProjectSelected,
						keyPriority,
						theFile.getName(), 
						new OnRequestListener() {

							@Override
							public void onSuccess() {
								progressDialog.dismiss();
								Toast.makeText(
										ConvertDispatchActivity.this,
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
								Toast.makeText(
										ConvertDispatchActivity.this,
										getResources().getString(
												R.string.internet_false),
										Toast.LENGTH_LONG).show();
								listChecked.removeAll(listChecked);

							}

							@Override
							public void onFalse(String stFalse) {
								progressDialog.dismiss();
								Toast.makeText(ConvertDispatchActivity.this,
										stFalse, Toast.LENGTH_LONG).show();
								listChecked.removeAll(listChecked);

							}
						});
			}

			break;
		}
	}
	@Override
	protected void onDestroy() {
		DialogChooseProject.VIEW_CURRENT = 0;
		super.onDestroy();
	}
}