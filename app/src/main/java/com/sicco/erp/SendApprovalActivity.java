package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnRequestListener;
import com.sicco.erp.model.User;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;

public class SendApprovalActivity extends Activity implements OnClickListener {
	private ImageView back;
	private EditText document;
	private Button btnChoseHandler, btnApproval;
	public static TextView txtHandler;
	private Department department;
	private User user;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;
	private String nameHandler = "";
	private Dispatch dispat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_send_approval);
		init();
	}

	private void init() {
		Intent intent = getIntent();
		dispat = (Dispatch) intent.getSerializableExtra("dispatch");

		back = (ImageView) findViewById(R.id.back);
		btnChoseHandler = (Button) findViewById(R.id.btnChoseHandler);
		btnApproval = (Button) findViewById(R.id.btnApproval);
		txtHandler = (TextView) findViewById(R.id.txt_handler);
		document = (EditText) findViewById(R.id.document);
		// click
		back.setOnClickListener(this);
		btnApproval.setOnClickListener(this);
		btnChoseHandler.setOnClickListener(this);

		listChecked = new ArrayList<User>();

		department = new Department();
		user = new User();
		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listDep = department.getData(getResources().getString(
				R.string.api_get_deparment));
		allUser = user.getData(getResources().getString(
				R.string.api_get_all_user));
	}

	@Override
	public void onBackPressed() {
		listChecked.removeAll(listChecked);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			listChecked.removeAll(listChecked);
			finish();
			break;
		case R.id.btnChoseHandler:
			ActionAdapter.flag = "";
			new DialogChooseUser(SendApprovalActivity.this, listDep, allUser,
					listChecked);
			break;
		case R.id.btnApproval:
			for (int i = 0; i < listChecked.size(); i++) {
				if (i == listChecked.size() - 1) {
					nameHandler += listChecked.get(i).getUsername();
				} else {
					nameHandler += listChecked.get(i).getUsername() + ",";
				}
			}

			final ProgressDialog progressDialog = new ProgressDialog(
					SendApprovalActivity.this);
			progressDialog.setMessage(getResources()
					.getString(R.string.waiting));

			Dispatch dispatch = new Dispatch(SendApprovalActivity.this);
			dispatch.approvalDispatch(
					getResources().getString(R.string.api_phecongvan),
					Utils.getString(SendApprovalActivity.this, "user_id"), ""
							+ dispat.getId(), document.getText().toString(),
					nameHandler, new OnRequestListener() {

						@Override
						public void onSuccess() {
							// ToanNM
							startGetAllNotificationService();
							// end of ToanNM
							progressDialog.dismiss();
							Toast.makeText(SendApprovalActivity.this,
									getResources().getString(R.string.success),
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);
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
									SendApprovalActivity.this,
									getResources().getString(
											R.string.internet_false),
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);

						}

						@Override
						public void onFalse(String stFalse) {
							progressDialog.dismiss();
							Toast.makeText(SendApprovalActivity.this, stFalse,
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);

						}
					});

			break;

		default:
			break;
		}
	}

	// ToanNM
	@Override
	protected void onStart() {
		super.onStart();
		startGetAllNotificationService();
	}

	void startGetAllNotificationService() {
		Intent intent = new Intent(getApplicationContext(),
				GetAllNotificationService.class);
		intent.putExtra("ACTION", 0);
		getApplicationContext().startService(intent);
		int count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CVCP_KEY, 0);
		count--;
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CVCP_KEY, count);
	}
	// End of ToanNM

}