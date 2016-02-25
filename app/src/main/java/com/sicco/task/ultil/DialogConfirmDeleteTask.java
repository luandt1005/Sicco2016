package com.sicco.task.ultil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.model.Status;
import com.sicco.task.erp.AssignedTaskActivity;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;

public class DialogConfirmDeleteTask implements OnClickListener {
	private Context context;
	private long id_task;

	private View layout;
	private TextView txtTitle;
	private TextView txtContent;
	private Button btnDelete;
	private Button btnCancel;
	private ImageView imgBack;
	private Status status;
	private Task task;

	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;
	private ProgressDialog progressDialog;

	public DialogConfirmDeleteTask(Context context, Task task) {
		super();
		this.context = context;
		this.task = task;
		init();
		showDialog();
	}

	private void init() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		layout = layoutInflater.inflate(R.layout.dialog_confirm_delete_task,
				null);

		layout.setMinimumWidth((int) (rect.width() * 1f));

		txtContent = (TextView) layout.findViewById(R.id.tvContent);
		txtTitle = (TextView) layout.findViewById(R.id.title_actionbar);

		imgBack = (ImageView) layout.findViewById(R.id.back);

		btnCancel = (Button) layout.findViewById(R.id.btnCancel);
		btnDelete = (Button) layout.findViewById(R.id.btnDelete);

		progressDialog = new ProgressDialog(context);

		txtTitle.setText(context.getResources().getString(
				R.string.confirm_delete));
		txtContent.setText(Html.fromHtml(context
				.getString(R.string.content_dialog_delete_task)
				+ "<font color=\"#ff0000\">"
				+ task.getTen_cong_viec()
				+ "</font>"));
		imgBack.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		progressDialog.setMessage(context.getString(R.string.waiting));
	}

	private void showDialog() {
		builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		alertDialog = builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			alertDialog.dismiss();
			break;
		case R.id.btnDelete:
			alertDialog.dismiss();
			task.deleteTaskById(context.getString(R.string.api_xoacongviec),
					task.getId(), new OnLoadListener() {

						@Override
						public void onSuccess() {
							AssignedTaskActivity.displayLisview();
							progressDialog.dismiss();
							Toast.makeText(context,
									context.getString(R.string.success),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onStart() {
							Log.d("NgaDV", "onstart");
							progressDialog.show();
						}

						@Override
						public void onFalse() {
							Log.d("NgaDV", "onfalse");
							progressDialog.dismiss();
							Toast.makeText(context,
									context.getString(R.string.error_occured),
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case R.id.btnCancel:
			alertDialog.dismiss();
			break;
		default:
			break;
		}
	}

}
