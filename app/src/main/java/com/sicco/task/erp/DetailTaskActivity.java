package com.sicco.task.erp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.model.Status;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.util.BadgeUtils;
import com.sicco.erp.util.Utils;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.ReportSteerTaskAdapter;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogConfirmDeleteTask;
import com.sicco.task.ultil.DialogSetProcess;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class DetailTaskActivity extends Activity implements OnClickListener,
        OnItemClickListener, Serializable {
    private Context context;
    private TextView title, content, assigner, implementers, assigned_at,
            expired_at, completed_infact, process, emptyView, attach_file;
    private ImageView back, btnAction;
    private SlidingDrawer drawer;
    private ViewDispatch viewDispatch;

    private long id_task;
    private Task task;

    private LinearLayout connectError, connectError1;
    private ScrollView scroll;
    private ListView listReport;
    private ProgressBar loading, loading1;
    private Button retry, retry1, btnReceiveTask;

    private ArrayList<ReportSteerTask> arrReportSteers;
    private ReportSteerTaskAdapter adapter;
    private ReportSteerTask reportSteerTask;
    private String s_id_task;
    private int taskType = -1;

    private ArrayList<Status> listStatus;
    private ArrayList<Status> listProcess;

    //
    private boolean insertToDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_detail_task);

        context = DetailTaskActivity.this;
        Intent intent = getIntent();
        // get id notifi
        id_task = intent.getLongExtra("id_task", -1);
        taskType = intent.getIntExtra("TASK_TYPE", -1);
        insertToDB = intent.getBooleanExtra("com.sicco.erp.manager.insertdb", false);
        Log.d("Debug", "id_task: " + id_task);

        // set id cong viec
        if (id_task != -1) {
            s_id_task = "" + id_task;
            setDetailTask(s_id_task);

        } else {
            task = (Task) intent.getSerializableExtra("task");
            s_id_task = "" + task.getId();
            init();
            setListReportSteer(s_id_task);
        }

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

    private void init() {
        reportSteerTask = new ReportSteerTask(DetailTaskActivity.this);
        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        back = (ImageView) findViewById(R.id.back);
        btnAction = (ImageView) findViewById(R.id.action);

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.task_content);
        assigner = (TextView) findViewById(R.id.assigner);
        implementers = (TextView) findViewById(R.id.implementers);
        assigned_at = (TextView) findViewById(R.id.assigned_at);
        expired_at = (TextView) findViewById(R.id.expired_at);
        completed_infact = (TextView) findViewById(R.id.completed_infact);
        process = (TextView) findViewById(R.id.process);
        attach_file = (TextView) findViewById(R.id.attach_file);

        scroll = (ScrollView) findViewById(R.id.scroll);
        btnReceiveTask = (Button) findViewById(R.id.receive_task);

        loading = (ProgressBar) findViewById(R.id.loading);
        retry = (Button) findViewById(R.id.retry);
        connectError = (LinearLayout) findViewById(R.id.connect_error);

        loading1 = (ProgressBar) findViewById(R.id.loading1);
        retry1 = (Button) findViewById(R.id.retry1);
        connectError1 = (LinearLayout) findViewById(R.id.connect_error1);

        listReport = (ListView) findViewById(R.id.listReport);
        emptyView = (TextView) findViewById(R.id.empty_view);

        attach_file.setOnClickListener(this);
        back.setOnClickListener(this);
        btnAction.setOnClickListener(this);

        retry.setOnClickListener(this);
        listReport.setOnItemClickListener(this);

        drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

            }
        });
        drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

            }
        });

        // setData
        title.setText(Html.fromHtml("<font><b>" + task.getTen_cong_viec() + "</b></font>"));
        content.setText(Html.fromHtml(task.getMo_ta()));
        assigner.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_assigner) + "</b></font>" + " " + task.getNguoi_giao()));
        implementers.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.nguoi_thuc_hien_cv) + "</b></font>" + " " + task.getNguoi_thuc_hien()));
        assigned_at.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_assigned_at) + "</b></font>" + " " + task.getNgay_bat_dau().substring(0, 10)));
        expired_at.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_expired_at) + "</b></font>" + " " + task.getNgay_ket_thuc().substring(0, 10)));
        completed_infact.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_completed_infact) + "</b></font>" + " " + task.getNgay_httt()));
        process.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_process) + "</b></font>" + " " + task.getTien_do() + "%"));
        if (task.getDinh_kem().equals("")) {
            attach_file.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_attach_file) + "</b></font>" + " " + getResources().getString(R.string.no_attach)));
        } else {
            File file = new File(task.getDinh_kem());
            attach_file.setText(Html.fromHtml("<font><b>" + getResources().getString(R.string.task_attach_file) + "</b></font>" + " " + "<font color = '#358cd1'><u><i>" + file.getName() + "</i></u></font>"));
        }

        if (!task.getTrang_thai().equals("complete")) {
            completed_infact.setVisibility(View.GONE);
        }

        if (task.getTrang_thai().equals("complete")) {
            process.setVisibility(View.GONE);
        }

        checkVisibleReceiveTask();
        String username = Utils.getString(context, "name");
        String url = context.getString(R.string.api_da_doc);
        task.changeDaDoc(url, username, task.getId() + "", new Task.OnLoadListener() {

            @Override
            public void onSuccess() {
                Log.d("LuanDT", "dadooooooooooooooooooooooooc is succcceeeeeessss");
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFalse() {
            }
        });
    }

    private void checkVisibleReceiveTask(){
        //check view nhan cong viec
        boolean checkIsXuly = false;
        final String userName = Utils.getString(DetailTaskActivity.this, "name");
        String[] nguoixuly = task.getNguoi_thuc_hien().split(",");
        for (int i = 0; i < nguoixuly.length; i++){
            if(nguoixuly[i].equals(userName)){
                checkIsXuly = true;
                break;
            }
        }

        if ((taskType == 2 || insertToDB) && checkIsXuly && task.getIsXuLy().equals("0")) {
            btnReceiveTask.setVisibility(View.VISIBLE);
            btnReceiveTask.setOnClickListener(this);
        }
    }

    private void setListReportSteer(String id_task) {
        arrReportSteers = new ArrayList<ReportSteerTask>();
        arrReportSteers = reportSteerTask.getData(DetailTaskActivity.this,
                getResources().getString(R.string.api_get_steer_report_task),
                id_task, new ReportSteerTask.OnLoadListener() {
                    @Override
                    public void onSuccess() {
                        loading.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                        if (adapter.getCount() <= 0) {
                            listReport.setEmptyView(emptyView);
                        }

                    }

                    @Override
                    public void onStart() {
                        loading.setVisibility(View.VISIBLE);
                        connectError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure() {
                        loading.setVisibility(View.GONE);
                        connectError.setVisibility(View.VISIBLE);
                    }
                });

        adapter = new ReportSteerTaskAdapter(DetailTaskActivity.this,
                arrReportSteers);
        listReport.setAdapter(adapter);
    }

    private void setDetailTask(String id_task) {
        task = new Task(DetailTaskActivity.this);
        task = task
                .getTaskById(DetailTaskActivity.this, getResources()
                                .getString(R.string.api_get_infor_task_by_id),
                        id_task, new Task.OnLoadListener() {

                            @Override
                            public void onSuccess() {
                                loading1.setVisibility(View.GONE);
                                init();
                                scroll.setVisibility(View.VISIBLE);
                                drawer.setVisibility(View.VISIBLE);
                                setListReportSteer(s_id_task);

                            }

                            @Override
                            public void onStart() {
                                drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
                                back = (ImageView) findViewById(R.id.back);
                                scroll = (ScrollView) findViewById(R.id.scroll);
                                loading1 = (ProgressBar) findViewById(R.id.loading1);
                                retry1 = (Button) findViewById(R.id.retry1);
                                connectError1 = (LinearLayout) findViewById(R.id.connect_error1);
                                retry1.setOnClickListener(DetailTaskActivity.this);

                                loading1.setVisibility(View.VISIBLE);
                                connectError1.setVisibility(View.GONE);
                                scroll.setVisibility(View.GONE);
                                drawer.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFalse() {
                                loading1.setVisibility(View.GONE);
                                connectError1.setVisibility(View.VISIBLE);

                            }
                        });

    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.attach_file:
                // open ViewDispatch
                if (!task.getDinh_kem().equals("")) {
                    viewDispatch = new ViewDispatch(DetailTaskActivity.this, task.getDinh_kem());
                } else

                    Toast.makeText(
                            getApplicationContext(), getResources().getString(
                                    R.string.no_attach), Toast.LENGTH_SHORT).show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.action:
                PopupMenu popupMenu = new PopupMenu(context, arg0);
                if (taskType == 1) {
                    popupMenu.getMenuInflater().inflate(R.menu.assigned_task2_2,
                            popupMenu.getMenu());
                } else if(taskType == 4) {
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.assigned_task2_2,
                                    popupMenu.getMenu());
                }else{
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.assigned_task1_2,
                                    popupMenu.getMenu());
                }

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
                                        if(!insertToDB)
                                            //intent.putExtra("task", task);
                                            intent.putExtra("id_task", task.getId());
                                        else
                                            intent.putExtra("id_task", id_task);
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
                break;
            case R.id.retry:
                setListReportSteer("" + id_task);
                break;
            case R.id.retry1:
                setDetailTask(s_id_task);
                break;
            case R.id.receive_task:
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(getResources().getString(R.string.waiting));
                ReportSteerTask reportSteerTask = new ReportSteerTask(context);
                try {
                    reportSteerTask.sendReport(context, task.getId(), context.getResources().getString(R.string.received_task), null, new ReportSteerTask.OnLoadListener() {
                        @Override
                        public void onStart() {
                            progressDialog.show();
                        }

                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            finish();
                            btnReceiveTask.setVisibility(View.GONE);
                            Toast.makeText(context, getResources().getString(R.string.received_task_success), Toast.LENGTH_SHORT).show();
                            //cập nhật số cv chưa xử lý
                            saveCVInt();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

    private void saveCVInt() {
        int cv_total = Utils.getInt(getApplicationContext(),
                GetAllNotificationService.CV_KEY, 0);
        if(cv_total > 0) {
            Utils.saveInt(getApplicationContext(),
                    GetAllNotificationService.CV_KEY, cv_total - 1);
        } else {
            Utils.saveInt(getApplicationContext(),
                    GetAllNotificationService.CV_KEY, 0);
        }
        getTotalNotification();
    }

    private void getTotalNotification() {
        int cvcp_total = Utils.getInt(getApplicationContext(),
                GetAllNotificationService.CVCP_KEY, 0);
        int cvxl_total = Utils.getInt(getApplicationContext(),
                GetAllNotificationService.CVXL_KEY, 0);
        int cv_total = Utils.getInt(getApplicationContext(),
                GetAllNotificationService.CV_KEY, 0);
        int total = cvcp_total + cvxl_total + cv_total;
        Utils.saveInt(getApplicationContext(),
                GetAllNotificationService.TOTAL_KEY, total);
        BadgeUtils.setBadge(getApplicationContext(), total);

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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ReportSteerTask reportSteerTask = (ReportSteerTask) arg0.getAdapter()
                .getItem(arg2);
        if (!reportSteerTask.getFile().equals("")) {
            viewDispatch = new ViewDispatch(DetailTaskActivity.this,
                    reportSteerTask.getFile());
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.no_attach),
                    Toast.LENGTH_SHORT).show();
        }
    }
}