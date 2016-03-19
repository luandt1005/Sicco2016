package com.sicco.task.ultil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.Spanned;
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

public class DialogConfirmTask implements OnClickListener {
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

	public DialogConfirmTask(Context context, Task task) {
		super();
		this.context = context;
		this.task = task;
		init();
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
//		txtContent.setText(Html.fromHtml(message
//				+ "<font color=\"#ff0000\">"
//				+ task.getTen_cong_viec()
//				+ "</font>"));
		imgBack.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		progressDialog.setMessage(context.getString(R.string.waiting));
	}

	public void setMessage(String message){
		txtContent.setText(message);
	}


	public void setMessage(Spanned message){
		txtContent.setText(message);
	}

	public void setTitle(String message){
		txtTitle.setText(message);
	}

	public void showDialog() {
		builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		alertDialog = builder.show();
	}

	private  OnButtonDialogConfirmListener onButtonDialogConfirmListener;

	public void setOnButtonDialogConfirmListener(String cancel, String ok, OnButtonDialogConfirmListener onButtonDialogConfirmListener) {
		btnCancel.setText(cancel);
		btnDelete.setText(ok);
		this.onButtonDialogConfirmListener = onButtonDialogConfirmListener;
	}

	public interface OnButtonDialogConfirmListener{
		void onClickBtnOk(View view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			alertDialog.dismiss();
			break;
		case R.id.btnDelete:
			alertDialog.dismiss();
			if(onButtonDialogConfirmListener != null)
				onButtonDialogConfirmListener.onClickBtnOk(btnDelete);
			break;
		case R.id.btnCancel:
			alertDialog.dismiss();
			break;
		default:
			break;
		}
	}

}
