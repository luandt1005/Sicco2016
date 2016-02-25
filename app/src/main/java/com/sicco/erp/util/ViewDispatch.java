package com.sicco.erp.util;

import java.io.File;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.ViewDispatchActivity;
import com.sicco.erp.util.DownloadFile.OnDownloadListener;

public class ViewDispatch {

	Context context;
	String url;
	
	private ProgressDialog dialog;
	private DownloadFile downloadFile;
	private AlertDialog aDialog;

	public ViewDispatch(Context context, String url) {
		super();
		this.context = context;
		this.url = url;
		startDownload(url);
	}
	
	private void startDownload(String url) {
		dialog = new ProgressDialog(context);
		downloadFile = new DownloadFile(url);
		dialog.setMessage(context.getResources().getString(R.string.downloading) + downloadFile.getFileName());
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		if (downloadFile.isZero()) {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setIndeterminate(true);
		} else {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);
		}
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					showDialogConfirmCancel();
				}
				return false;
			}
		});
		dialog.show();

		downloadFile.setOnDownloadListener(new OnDownloadListener() {

			@Override
			public void onSuccess(String p) {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
				downloadFile = null;
				File pdfFile = new File(p);
				try {
					if (pdfFile.exists()) {
						Uri path = Uri.fromFile(pdfFile);
						Intent objIntent = new Intent(Intent.ACTION_VIEW);
						objIntent.setDataAndType(path, "application/pdf");
						objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(objIntent);
					} else {
						Toast.makeText(context, context.getResources().getString(R.string.file_notfound),
								Toast.LENGTH_SHORT).show();
					}
				} catch (ActivityNotFoundException e) {
					Intent intent = new Intent(context,
							ViewDispatchActivity.class);
					intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, p);
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFalse() {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
			}

			@Override
			public void onDownload(int progress) {
				dialog.setProgress(progress);
			}

			@Override
			public void onCancel() {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
			}
		});
	}

	private void showDialogConfirmCancel() {
		if (aDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					context);
			builder.setMessage(context.getResources().getString(R.string.exit_download));
			builder.setPositiveButton(context.getResources().getString(R.string.agree), new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					downloadFile.cancel();
					if (ViewDispatch.this.dialog.isShowing()) {
						ViewDispatch.this.dialog.dismiss();
					}
					aDialog.dismiss();
				}
			});
			builder.setNegativeButton(context.getResources().getString(R.string.no),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							aDialog.dismiss();
						}
					});
			aDialog = builder.create();
			aDialog.show();
		} else {
			if (!aDialog.isShowing()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				builder.setMessage(context.getResources().getString(R.string.exit_download));
				builder.setPositiveButton(context.getResources().getString(R.string.agree),
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								downloadFile.cancel();
								if (ViewDispatch.this.dialog.isShowing()) {
									ViewDispatch.this.dialog.dismiss();
								}
								aDialog.dismiss();
							}
						});
				builder.setNegativeButton(context.getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								aDialog.dismiss();
							}
						});
				aDialog = builder.create();
				aDialog.show();
			}
		}
	}
}
