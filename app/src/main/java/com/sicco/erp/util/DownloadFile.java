package com.sicco.erp.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Environment;

public class DownloadFile extends AsyncTask<Void, Integer, Boolean> {
	private String urlDownload;
	private String fileName;
	private String filePath;
	private int thisProgress = -1;
	private int size = 0;
	private boolean isZero = false;
	private boolean cancel = false;

	public DownloadFile(String urlDownload, String filePath, String fileName) {
		this.urlDownload = urlDownload;
		this.fileName = fileName;
		this.filePath = filePath;
		execute();
	}

	public DownloadFile(String urlDownload, String fileName) {
		this.urlDownload = urlDownload;
		this.fileName = fileName;
		this.filePath = Environment.getExternalStorageDirectory() + "/Download";
		execute();
	}

	public DownloadFile(String urlDownload) {
		this.urlDownload = urlDownload;
		String fn = urlDownload.substring(urlDownload.lastIndexOf('/') + 1);
		if (!fn.isEmpty())
			this.fileName = fn;
		else
			this.fileName = "unknow";
		this.filePath = Environment.getExternalStorageDirectory() + "/Download";
		execute();
	}

	@Override
	protected void onPreExecute() {
		File projDir = new File(filePath);
		if (!projDir.exists())
			projDir.mkdirs();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... f_url) {
		int count;
		long total = 0;
		try {
			URL url = new URL(urlDownload);
			URLConnection connection = url.openConnection();
			connection.connect();
			int fileLength = connection.getContentLength();
			size = fileLength;
			if (fileLength == -1) {
				isZero = true;
			}
			InputStream input = new BufferedInputStream(url.openStream(), 8192);

			OutputStream output = new FileOutputStream(filePath + "/"
					+ fileName);
			byte data[] = new byte[1024];
			while ((count = input.read(data)) != -1) {
				if (cancel) {
					if (onDownloadListener != null)
						onDownloadListener.onCancel();
					break;
				}
				total += count;
				output.write(data, 0, count);
				if (fileLength > 0) // only if total length is known
					publishProgress((int) (total * 100 / fileLength));
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			if (onDownloadListener != null)
				onDownloadListener.onFalse();
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (thisProgress != values[0]) {
			thisProgress = values[0];
			if (onDownloadListener != null) {
				onDownloadListener.onDownload(values[0]);
				if (thisProgress == 100) {
					onDownloadListener.onSuccess(this.filePath + "/"
							+ this.fileName);
				}
			}
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			if (size == -1)
				onDownloadListener.onSuccess(this.filePath + "/"
						+ this.fileName);
		}
		super.onPostExecute(result);
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isZero() {
		return isZero;
	}

	public void cancel() {
		this.cancel = true;
	}

	private OnDownloadListener onDownloadListener;

	public interface OnDownloadListener {
		void onDownload(int progress);

		void onFalse();

		void onSuccess(String path);

		void onCancel();
	}

	public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
		this.onDownloadListener = onDownloadListener;
	}
}