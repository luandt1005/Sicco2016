package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.DealtWithActivity;
import com.sicco.erp.OtherActivity;
import com.sicco.erp.R;
import com.sicco.erp.adapter.StatusAdapter;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.Status;
import com.sicco.erp.service.GetAllNotificationService;

public class DialogChangeStatusDispatch {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private Button btnDone;
	private Button btnRetry;
	private ListView lvStatus;
	private Dispatch dispatch;
	private Status status;
	
	int type;

	public DialogChangeStatusDispatch(Context context,
			ArrayList<Status> listStatus, Dispatch dispatch, int type) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		this.dispatch = dispatch;
		this.type = type;
		status = new Status();
		status.setKey(Long.parseLong(dispatch.getStatus()));

		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View layout = layoutInflater.inflate(
				R.layout.dialog_change_status_dispatch, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
		// layout.setMinimumHeight((int) (rect.height() * 1f));

		txtTitle = (TextView) layout.findViewById(R.id.title_actionbar);
		lvStatus = (ListView) layout.findViewById(R.id.lvStatus);
		btnDone = (Button) layout.findViewById(R.id.done);
		btnRetry = (Button) layout.findViewById(R.id.retry);

		statusAdapter = new StatusAdapter(context, listStatus);
		lvStatus.setAdapter(statusAdapter);

		lvStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				status = (Status) parent.getAdapter().getItem(position);

			}
		});

		lvStatus.setItemChecked(Integer.parseInt(dispatch.getStatus()) - 2,
				true);

		txtTitle.setText(context.getResources().getString(
				R.string.change_status));

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

				final ProgressDialog progressDialog = new ProgressDialog(
						context);
				progressDialog.setMessage(context
						.getResources().getString(
								R.string.waiting));

				dispatch.changeStatusDispatch(
						context.getResources().getString(
								R.string.api_change_status),
						Long.toString(dispatch.getId()),
						Long.toString(status.getKey()), new OnLoadListener() {

							@Override
							public void onStart() {
								progressDialog.show();
							}

							@Override
							public void onSuccess() {
								// ToanNM
								startGetAllNotificationService();
								setCount(type);
//								int count = querryFromDB(context);
//								setCount(count);
								// end of ToanNM
								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.success),
										Toast.LENGTH_LONG).show();
								alertDialog.dismiss();
							}

							@Override
							public void onFalse() {

								progressDialog.dismiss();
								Toast.makeText(context, context.getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
							}
						});

			}
		});
		// click retry
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Do something
			}
		});
	}

	// ToanNM
	NotificationDBController db;
	Cursor cursor;

	int querryFromDB(Context context) {
		int count;
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DSTATE_COL + " = \"new\"";
		cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		return count;
	}
	
	void startGetAllNotificationService() {
		Intent intent = new Intent(context, GetAllNotificationService.class);
		intent.putExtra("ACTION", 0);
		context.startService(intent);
	}

	void setCount(int type) {
		if (type == 1) {
			int count = Utils.getInt(context, GetAllNotificationService.CL_KEY, 0);
			if (count != 0) {
				count--;
			} else if (count == 0) {
				cancelNotification(context, 3);
			}
			db = NotificationDBController.getInstance(context);
			db.changeStateDisPatch(dispatch, dispatch.getId(), 0, "new");
			Utils.saveInt(context, GetAllNotificationService.CL_KEY, count);
		} else if (type == 0) {
			int count = Utils.getInt(context,
					GetAllNotificationService.CVXL_KEY, 0);
			if (count != 0) {
				count--;
			} else if (count == 0) {
				cancelNotification(context, 2);
			}
			db = NotificationDBController.getInstance(context);
			db.changeStateDisPatch(dispatch, dispatch.getId(), 1, "new");
			Utils.saveInt(context, GetAllNotificationService.CVXL_KEY, count);
		}
	}

	void cancelNotification(Context context, int notification_id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(notification_id);
	}
	// End of ToanNM

}