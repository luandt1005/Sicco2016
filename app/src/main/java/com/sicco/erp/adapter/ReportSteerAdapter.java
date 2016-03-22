package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.ReportSteer;

public class ReportSteerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ReportSteer> data;

    public ReportSteerAdapter(Context context, ArrayList<ReportSteer> data) {
        this.context = context;
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<ReportSteer> getData() {
        return data;
    }

    public void setData(ArrayList<ReportSteer> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ReportSteer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ReportSteer reportSteer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_report_steer, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.handler = (TextView) convertView
                    .findViewById(R.id.txtHandler);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txtDate);
            viewHolder.contentReport = (TextView) convertView
                    .findViewById(R.id.txtContentReportSteer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(
                    R.color.white));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(
                    R.color.item_color));
        }

        String date = "<font><b>" + context.getResources().getString(R.string.time) + "</b></font>" + "  " + reportSteer.getDate();
        String handler = "<font><b>" + context.getResources().getString(R.string.dang_boi) + "</b></font>" + " " + "<font><b>" + reportSteer.getHandler() + "</b></font>";
        String contentReport =  "<font><b>" + context.getResources().getString(R.string.noi_dung) + "</b></font>" + "  " + reportSteer.getContent();
        viewHolder.handler.setText(Html.fromHtml(handler));
        viewHolder.date.setText(Html.fromHtml(date));
        viewHolder.contentReport.setText(Html.fromHtml(contentReport));
        return convertView;
    }

    private class ViewHolder {
        TextView handler, date, contentReport;
    }
}
