package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Utils;
import com.sicco.task.erp.DetailTaskActivity;
import com.sicco.task.erp.EditTaskActivity;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogConfirmDeleteTask;
import com.sicco.task.ultil.DialogSetProcess;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> data;
    private int type;
    NotificationDBController db;
    Cursor cursor;
    private ArrayList<Status> listStatus;
    private ArrayList<Status> listProcess;

    public TaskAdapter(Context context, ArrayList<Task> data, int type) {
        super();
        this.context = context;
        this.data = data;
        this.type = type;

        listStatus = new ArrayList<Status>();

        listStatus.add(new Status(0, "active", context.getResources()
                .getString(R.string.active)));
        listStatus.add(new Status(1, "inactive", context.getResources()
                .getString(R.string.inactive)));
        listStatus.add(new Status(2, "complete", context.getResources()
                .getString(R.string.complete)));
        listStatus.add(new Status(3, "cancel", context.getResources()
                .getString(R.string.cancel)));

        listProcess = new ArrayList<Status>();

        String[] key = context.getResources().getStringArray(
                R.array.process_key);
        String[] value = context.getResources().getStringArray(
                R.array.process_value);
        int max = key.length;
        for (int i = 0; i < max; i++) {
            listProcess.add(new Status(i + 1, key[i], value[i]));
        }
    }

    public ArrayList<Task> getData() {
        return data;
    }

    public void setData(ArrayList<Task> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Task getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return data.get(arg0).getId();
    }

    @Override
    public View getView(int arg0, View view, ViewGroup arg2) {
        final ViewHolder holder;
        final Task task = getItem(arg0);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_task,
                    arg2, false);
            holder = new ViewHolder();
            holder.taskName = (TextView) view.findViewById(R.id.taskName);
            holder.date_handle = (TextView) view.findViewById(R.id.date_handle);
            holder.action = (TextView) view.findViewById(R.id.action);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (type == 2) {
            long id = task.getId();
            String state = querryFromDB(context, id);
            if (state
                    .equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)) {
                view.setBackgroundColor(context.getResources().getColor(
                        R.color.item_color));
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
        }

//        String date_handle = "<font weigth='bold'><b>" + context.getResources().getString(R.string.ngaygiao)
//                + "</b></font>" + ":  " + task.getNgay_bat_dau();

        holder.taskName.setText(task.getTen_cong_viec());
        //holder.date_handle.setText(Html.fromHtml(date_handle));
        holder.date_handle.setText("" + task.getNgay_bat_dau());

        String colorAction = context.getResources().getString(R.color.actionbar_color);
        //mh danh sach viec
        if (type == 3) {
            //if (!task.isCo_binh_luan()) {
            //	colorAction = "#aa0000";
            //}
            //if (!task.isCo_binh_luan() && task.getMuc_uu_tien().equals("2")) {
            //	colorAction = "#ff0000";
            //}
            //if (task.isCo_binh_luan()) {
            //	colorAction = "#5E7AF8";
            //}
            //if (task.isDa_qua_han() && task.isCo_binh_luan()) {
            //	colorAction = "#DF06D2";
            //}
            if (task.getTrang_thai().equals("complete")) {
                colorAction = "#01C853";
            }
            if (task.getTrang_thai().equals("inactive")) {
                colorAction = "#868B86";
            }
            if (task.getTrang_thai().equals("cancel")) {
                colorAction = "#A26637";
            }
        }

        //mh duoc giao
        if (type == 2) {
            if (task.getDaxuly().equals("0")) {
                colorAction = "#aa0000";
            } else {
                colorAction = colorAction;
            }
        }

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, 0});
        drawable.setColor(Color.parseColor(colorAction));
        drawable.setCornerRadius(context.getResources().getDimension(R.dimen.item_size));
        holder.action.setBackgroundDrawable(drawable);
        holder.action.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(onActionClickLisstener!=null){
                    onActionClickLisstener.onClick(v, listStatus, listProcess, type, task, isUpdateStatusAndRate(task.getNguoi_thuc_hien()));
                }else {
                    PopupMenu popupMenu = new PopupMenu(context, holder.action);
                    if (type == 1) {
                        popupMenu.getMenuInflater().inflate(R.menu.assigned_task,
                                popupMenu.getMenu());
                    } else {
                        popupMenu.getMenuInflater()
                                .inflate(R.menu.assigned_task1,
                                        popupMenu.getMenu());
                    }

                    popupMenu.show();
                    popupMenu
                            .setOnMenuItemClickListener(new OnMenuItemClickListener() {

                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Intent intent = new Intent();
                                    switch (item.getItemId()) {
                                        case R.id.action_report:
                                            intent.setClass(context,
                                                    SteerReportTaskActivity.class);
                                            intent.putExtra("task", task);
                                            context.startActivity(intent);
                                            break;
                                        case R.id.action_detail:
                                            intent.setClass(context,
                                                    DetailTaskActivity.class);
                                            intent.putExtra("task", task);
                                            context.startActivity(intent);
                                            break;
                                        case R.id.action_edit:
                                            intent = new Intent(context, EditTaskActivity.class);
                                            intent.putExtra("TASK", task);
                                            context.startActivity(intent);
                                            break;
                                        case R.id.action_delete:
                                            new DialogConfirmDeleteTask(context, task);
                                            break;
                                        case R.id.action_change_status:
                                            if (task.getTrang_thai().equals("complete")) {
                                                Toast.makeText(context,
                                                        context.getResources().getString(R.string.not_update_status),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (isUpdateStatusAndRate(task.getNguoi_thuc_hien())) {
                                                    new DialogChangeStatusTask(context,
                                                            listStatus, task);
                                                } else {
                                                    Toast.makeText(
                                                            context,
                                                            context.getResources().getString(
                                                                    R.string.info_update_status),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            break;
                                        case R.id.action_update_rate:
                                            if (task.getTrang_thai().equals("complete")) {
                                                Toast.makeText(context,
                                                        context.getResources().getString(R.string.not_update_rate),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (isUpdateStatusAndRate(task.getNguoi_thuc_hien())) {
                                                    DialogSetProcess dialog = new DialogSetProcess(
                                                            context, listProcess, task);
                                                    dialog.showDialog();
                                                } else {
                                                    Toast.makeText(
                                                            context,
                                                            context.getResources().getString(R.string.info_update_rate),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            break;

                                    }
                                    return false;
                                }
                            });
                }
            }
        });

        return view;
    }

    private boolean isUpdateStatusAndRate(String stNguoiThucHien) {
        String username = Utils.getString(context,
                "name");
        String[] nguoithuchien = stNguoiThucHien.split(",");
        for (int i = 0; i < nguoithuchien.length; i++) {
            if (username.equals(nguoithuchien[i])) {
                return true;
            }
        }
        return false;
    }

    private class ViewHolder {
        private TextView taskName;
        private TextView handler;
        private TextView date_handle;
        private TextView date_finish;
        private TextView action;
    }

    String querryFromDB(Context context, long position) {
        String state = "";
        db = NotificationDBController.getInstance(context);
        cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null,
                null, null, null, null, null);
        String sql = "Select * from "
                + NotificationDBController.TASK_TABLE_NAME + " where "
                + NotificationDBController.ID_COL + " = " + position;
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                state = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(NotificationDBController.TRANGTHAI_COL));
            } while (cursor.moveToNext());
        }
        return state;
    }

    public interface OnActionClickLisstener{
        void onClick(View view,ArrayList<Status> listStatus, ArrayList<Status> listProcess, int type, Task task, boolean isUpdateStatusAndRate);
    }

    private OnActionClickLisstener onActionClickLisstener;
    public void setOnActionClickLisstener(OnActionClickLisstener onActionClickLisstener){
        this.onActionClickLisstener = onActionClickLisstener;
    }
}
