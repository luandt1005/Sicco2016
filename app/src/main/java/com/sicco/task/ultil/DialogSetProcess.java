package com.sicco.task.ultil;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
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

import com.sicco.erp.R;
import com.sicco.erp.adapter.StatusAdapter;
import com.sicco.erp.model.Status;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;

public class DialogSetProcess {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private Button btnDone;
	private Button btnRetry;
	private ListView lvStatus;
	private Task task;
	private Status status;

	int type;

	public DialogSetProcess(Context context, ArrayList<Status> listStatus,
			Task task) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		this.task = task;
		status = new Status();
		status.setKey(Long.parseLong(task.getTien_do()) / 10);
	}

	@SuppressLint("InflateParams")
	public void showDialog() {
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

		int selected = (int) status.getKey();
		Log.d("ToanNM", "task.getTien_do() : " + selected);
		lvStatus.setItemChecked(selected, true);
		Log.d("MyDebug", "selected is : " + selected);

		txtTitle.setText(context.getResources().getString(
				R.string.chose_process));

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
				progressDialog.setMessage(context.getResources().getString(
						R.string.waiting));

				task.changeProcess(
						context.getResources().getString(
								R.string.api_update_rate),
						Long.toString(task.getId()), status.getsKey(),
						new OnLoadListener() {

							@Override
							public void onStart() {
								progressDialog.show();
							}

							@Override
							public void onSuccess() {
								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.success),
										Toast.LENGTH_LONG).show();
								alertDialog.dismiss();
								task.setTien_do(status.getsKey());
								if(status.getsKey().equals("100")){
									task.setTrang_thai("complete");
								}
							}

							@Override
							public void onFalse() {

								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.internet_false),
										Toast.LENGTH_SHORT).show();
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

}