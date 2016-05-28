package com.sicco.task.erp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.Utils;
import com.sicco.task.adapter.TaskAdapter;
import com.sicco.task.callback.OnSuccess;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogConfirmDeleteTask;
import com.sicco.task.ultil.DialogSetProcess;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Thanh Tu on 26/05/2016.
 */
public class ViecDuocXemActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Context context;
    private ImageView ivBack, ivClose, ivSearch, ivEmptySearch;
    private ListView lvCongViec;
    private LinearLayout llErrorConnect, llSearch;
    private TextView tvEmpty;
    private Button btnRetry;
    private ProgressBar pbLoading;
    private EditText edtSearch;

    private ArrayList<Task> arrTask;
    private Task task;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viec_duoc_xem);

        context = ViecDuocXemActivity.this;

        initView();
        arrTask = new ArrayList<>();
        task = new Task(context);
        adapter = new TaskAdapter(context, arrTask, 4);
        adapter.setOnActionClickLisstener(new TaskAdapter.OnActionClickLisstener() {
            @Override
            public void onClick(View view, final ArrayList<Status> listStatus, final ArrayList<Status> listProcess, int type, final Task task, final boolean isUpdateStatusAndRate) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.assigned_task2,
                                    popupMenu.getMenu());

                popupMenu.show();
                popupMenu
                        .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

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
                                        intent.putExtra("TASK_TYPE", 4);
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
                                            if (isUpdateStatusAndRate) {
                                                DialogChangeStatusTask dialogChangeStatusTask = new DialogChangeStatusTask(context,
                                                        listStatus, task);
                                                dialogChangeStatusTask.setOnSuccess(new OnSuccess() {
                                                    @Override
                                                    public void onSuccess() {
                                                        onResume();
                                                    }
                                                });
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
                                            if (isUpdateStatusAndRate) {
                                                DialogSetProcess dialog = new DialogSetProcess(
                                                        context, listProcess, task);
                                                dialog.setOnSuccess(new OnSuccess() {
                                                    @Override
                                                    public void onSuccess() {
                                                        onResume();
                                                    }
                                                });
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
        });
        lvCongViec.setAdapter(adapter);
        loadTask();
    }

    private void initView(){
        ivBack = (ImageView) findViewById(R.id.back);
        ivClose = (ImageView) findViewById(R.id.close);
        lvCongViec = (ListView) findViewById(R.id.listTask);
        llErrorConnect = (LinearLayout) findViewById(R.id.connect_error);
        tvEmpty = (TextView) findViewById(R.id.empty_view);
        btnRetry = (Button) findViewById(R.id.retry);
        pbLoading = (ProgressBar) findViewById(R.id.loading);
        ivSearch = (ImageView) findViewById(R.id.search);
        llSearch = (LinearLayout) findViewById(R.id.searchview);
        edtSearch = (EditText) findViewById(R.id.edit_search);
        ivEmptySearch = (ImageView) findViewById(R.id.empty);

        ivBack.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivEmptySearch.setOnClickListener(this);
        lvCongViec.setOnItemClickListener(this);

        pbLoading.setVisibility(View.GONE);
        llErrorConnect.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        lvCongViec.setVisibility(View.GONE);

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if (arg0.toString().trim().length() > 0) {
                    ivEmptySearch.setVisibility(View.VISIBLE);
                } else {
                    ivEmptySearch.setVisibility(View.GONE);
                }
                ArrayList<Task> searchData = task.search(
                        arg0.toString().trim(), arrTask);
                if(searchData.size() > 0){
                    lvCongViec.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                }else{
                    lvCongViec.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
                adapter.setData(searchData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void loadTask(){
        arrTask.clear();
        arrTask = task.getData(ViecDuocXemActivity.this, context.getString(R.string.api_get_cv_duoc_xem), new Task.OnLoadListener() {
            @Override
            public void onStart() {
                arrTask.clear();
                pbLoading.setVisibility(View.VISIBLE);
                llErrorConnect.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.GONE);
                lvCongViec.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess() {
                arrTask = task.getData();
                pbLoading.setVisibility(View.GONE);
                if(arrTask.size() > 0){
                    lvCongViec.setVisibility(View.VISIBLE);
                }else{
                    tvEmpty.setVisibility(View.VISIBLE);
                }
                adapter.setData(arrTask);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFalse() {
                pbLoading.setVisibility(View.GONE);
                llErrorConnect.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Task task = (Task) arg0.getAdapter().getItem(arg2);
        Intent intent = new Intent(this, DetailTaskActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("TASK_TYPE", 4);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.back:
                finish();
                break;
            case R.id.retry:
                loadTask();
                break;
            case R.id.search:
                edtSearch.requestFocus();
                Keyboard.showKeyboard(context, edtSearch);
                llSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.close:
                Keyboard.hideKeyboard(context, llSearch);
                llSearch.setVisibility(View.GONE);
                break;
            case R.id.empty:
                edtSearch.setText("");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(llSearch.getVisibility() == View.VISIBLE){
            llSearch.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
