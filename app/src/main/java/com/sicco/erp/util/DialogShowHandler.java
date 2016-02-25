package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnRequestListener;
import com.sicco.erp.model.User;

public class DialogShowHandler {

	private Context context;
	private ArrayList<User> listChecked;
	private String handler;
	private Button btnCancel, btnDone;
	private Dispatch dispatch;

	public DialogShowHandler(Context context, ArrayList<User> listChecked) {
		this.context = context;
		this.listChecked = listChecked;

		showDialog();
	}

	public DialogShowHandler(Context context, Dispatch dispatch,
			ArrayList<User> listChecked) {
		this.context = context;
		this.dispatch = dispatch;
		this.listChecked = listChecked;

		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_show_handler, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
		// layout.setMinimumHeight((int) (rect.height() * 1f));

		TextView txtListHandeler = (TextView) layout
				.findViewById(R.id.listHandler);

		handler = "";
		for (int i = 0; i < listChecked.size(); i++) {
			if (i == listChecked.size() - 1)
				handler += listChecked.get(i).getUsername();
			else
				handler += listChecked.get(i).getUsername() + ",";
		}
		txtListHandeler.setText(context.getResources().getString(
				R.string.handler)
				+ handler);

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

		btnDone = (Button) layout.findViewById(R.id.done);
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alertDialog.dismiss();
				guiXuLy();
			}
		});

		btnCancel = (Button) layout.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alertDialog.dismiss();
				listChecked.removeAll(listChecked);
			}
		});
	}

	private void guiXuLy() {
		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(context.getResources().getString(R.string.waiting));
		Dispatch dDispatch = new Dispatch(context);
		dDispatch.guiXuLy(
				context.getResources().getString(
						R.string.api_send_handl_forward),
				Long.toString(dispatch.getId()), handler,
				new OnRequestListener() {

					@Override
					public void onSuccess() {
						dialog.dismiss();
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.success), Toast.LENGTH_LONG)
								.show();
						listChecked.removeAll(listChecked);
					}

					@Override
					public void onStart() {
						dialog.show();
					}

					@Override
					public void onFalse() {
						dialog.dismiss();
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.error_l), Toast.LENGTH_LONG)
								.show();
						listChecked.removeAll(listChecked);
					}

					@Override
					public void onFalse(String stFalse) {
						dialog.dismiss();
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.error_l), Toast.LENGTH_LONG)
								.show();
						listChecked.removeAll(listChecked);
					}
				});
	}
}
