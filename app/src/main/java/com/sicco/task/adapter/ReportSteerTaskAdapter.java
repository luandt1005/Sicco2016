package com.sicco.task.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.task.model.ReportSteerTask;

public class ReportSteerTaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ReportSteerTask> data;
	
	public ReportSteerTaskAdapter(Context context,
			ArrayList<ReportSteerTask> data) {
		this.context = context;
		this.data = data;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ArrayList<ReportSteerTask> getData() {
		return data;
	}

	public void setData(ArrayList<ReportSteerTask> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ReportSteerTask getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ReportSteerTask reportSteer = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_report_steer_task, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.handler = (TextView) convertView
					.findViewById(R.id.txtHandler);
			viewHolder.date = (TextView) convertView.findViewById(R.id.txtDate);
			viewHolder.contentReport = (TextView) convertView
					.findViewById(R.id.txtContentReportSteer);
			viewHolder.attachments = (TextView) convertView
					.findViewById(R.id.txtAttach);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (position % 2 == 0) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.item_color));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.white));
		}
		
		String handler = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.dang_boi)
				+ "</i></u></b></font>" + "  " + reportSteer.getHandler();
		String date = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.time)
				+ "</i></u></b></font>" + "  " + reportSteer.getDate();
		String contentReport = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.noi_dung)
				+ "</i></u></b></font>" + "  " + reportSteer.getContent();
		String attachments;
		if (!reportSteer.getFile().equals("")) {
			File file = new File(reportSteer.getFile());
			attachments = "<font weigth='bold'><b><u><i>"
					+ context.getResources().getString(R.string.attach)
					+ "</i></u></b></font>" + "  " + file.getName();
		} else {
			attachments = "<font weigth='bold'><b><u><i>"
					+ context.getResources().getString(R.string.attach)
					+ "</i></u></b></font>" + "  "
					+ context.getResources().getString(R.string.no_attach);
		}

		viewHolder.handler.setText(Html.fromHtml(handler));
		viewHolder.date.setText(Html.fromHtml(date));
		viewHolder.contentReport.setText(Html.fromHtml(contentReport));
		viewHolder.attachments.setText(Html.fromHtml(attachments));

		return convertView;
	}

	private class ViewHolder {
		private TextView handler;
		private TextView date;
		private TextView contentReport;
		private TextView attachments;
	}
	
}
