package com.sicco.task.erp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.LogRecord;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Project;
import com.sicco.erp.model.User;
import com.sicco.erp.util.ChooseFileActivity;
import com.sicco.erp.util.DialogChooseDepartment;
import com.sicco.erp.util.DialogChooseHandler;
import com.sicco.erp.util.DialogChooseProject;
import com.sicco.erp.util.DialogChooseUser;
import com.sicco.erp.util.Utils;
import com.sicco.task.adapter.SpinnerPriorityAdapter;
import com.sicco.task.model.Priority;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;
import com.sicco.task.ultil.DialogConfirmTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class EditTaskActivity extends ChooseFileActivity implements
        OnClickListener {
    private Context activityContext;
    private static int FLAG_DATE_PICKER = 1;
    private ImageView back;
    private ScrollView scrollView;
    private LinearLayout lnDateHandle, lnHandler, lnViewer, lnDepartment,
            lnDateFinish, lnProject, lnTitle, lnDescription, lnPriority;

    private TextView txtDateHandle;
    private TextView txtDateFinish, txtPriority;
    private Spinner spnPriority;
    public static TextView txtDepartment, txtHandler, txtViewer, txtProject;
    private EditText edtDes;
    private EditText edtTitle;
    private Button btnAssign;

    private ArrayList<Department> listDep;
    private ArrayList<Department> listDepUser;
    private ArrayList<Project> listProject;
    private ArrayList<User> allUser;
    private ArrayList<User> listChecked, listCheckedHandler;
    private Task task;

    static final int DATE_DIALOG_ID = 111;
    private int date;
    private int months;
    private int years_now;

    private StringBuilder toDate;
    private Department department;
    private Project project;
    private User user;
    private File file;
    private String path;

    private SpinnerPriorityAdapter spinnerPriorityAdapter;
    private ArrayList<Priority> listPriority;
    private String keyPriority;

    private Task taskIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_edit_task);
        activityContext = EditTaskActivity.this;

        try {
            taskIntent = (Task) getIntent().getSerializableExtra("TASK");
            if (taskIntent == null) {
                finish();
                Toast.makeText(activityContext, "Task null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            finish();
            Toast.makeText(activityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        DialogChooseHandler.VIEW_CURRENT = 3;
        DialogChooseProject.VIEW_CURRENT = 3;
        DialogChooseDepartment.VIEW_CURRENT = 3;

        init();
    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);

        scrollView = (ScrollView) findViewById(R.id.scrMain);
        lnTitle = (LinearLayout) findViewById(R.id.lnTitle);
        lnDescription = (LinearLayout) findViewById(R.id.lndes);
        lnDateHandle = (LinearLayout) findViewById(R.id.lnDateHandle);
        lnDateFinish = (LinearLayout) findViewById(R.id.lnDateFinish);
        lnHandler = (LinearLayout) findViewById(R.id.lnHandler);
        lnViewer = (LinearLayout) findViewById(R.id.lnViewer);
        lnDepartment = (LinearLayout) findViewById(R.id.lnDepartment);
        lnProject = (LinearLayout) findViewById(R.id.lnProject);
        lnPriority = (LinearLayout) findViewById(R.id.lnPriority);

        txtDateHandle = (TextView) findViewById(R.id.txtDateHandle);
        txtDateFinish = (TextView) findViewById(R.id.txtDateFinish);
//		txtPriority   	= (TextView) findViewById(R.id.txtPriority);
        txtHandler = (TextView) findViewById(R.id.txtHandler);
        txtViewer = (TextView) findViewById(R.id.txtViewer);
        txtDepartment = (TextView) findViewById(R.id.txtDepartment);
        txtProject = (TextView) findViewById(R.id.txtProject);

        //----------------------spinner Priority -----------------------------///
        spnPriority = (Spinner) findViewById(R.id.spnPriority);
        listPriority = new ArrayList<Priority>();

        listPriority.add(new Priority(getResources().getString(R.string.low), "0"));
        listPriority.add(new Priority(getResources().getString(R.string.medium), "1"));
        listPriority.add(new Priority(getResources().getString(R.string.hight), "2"));
        spinnerPriorityAdapter = new SpinnerPriorityAdapter(getApplicationContext(), listPriority);

        spnPriority.setAdapter(spinnerPriorityAdapter);
        spnPriority.setSelection(1);
        spnPriority.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Priority priority = (Priority) parent.getAdapter().getItem(position);

                keyPriority = priority.getKeyPriority();
//				Status status = (Status) parent.getAdapter().getItem(position);
//				Log.d("NgaDV", "priority.getKey(): " + pr.getKey());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        //----------------------spinner Priority -----------------------------///
        edtDes = (EditText) findViewById(R.id.edtDes);
        edtTitle = (EditText) findViewById(R.id.edtTitle);

        btnAssign = (Button) findViewById(R.id.btnAssign);

        // click
        back.setOnClickListener(this);

        lnDateHandle.setOnClickListener(this);
        lnDateFinish.setOnClickListener(this);
        lnPriority.setOnClickListener(this);
        lnHandler.setOnClickListener(this);
        lnViewer.setOnClickListener(this);
        lnDepartment.setOnClickListener(this);
        lnProject.setOnClickListener(this);
//		spnPriority.setOnClickListener(l)
        btnAssign.setOnClickListener(this);

        listChecked = new ArrayList<User>();
        listCheckedHandler = new ArrayList<User>();

        department = new Department();
        project = new Project();
        user = new User();
        listDep = new ArrayList<Department>();
        listDepUser = new ArrayList<Department>();
        listProject = new ArrayList<Project>();
        allUser = new ArrayList<User>();

        listDep = department.getData(getResources().getString(
                R.string.api_get_deparment));
        listDepUser = department.getData(getResources().getString(
                R.string.api_get_deparment_users));
        listProject = project.getData(getResources().getString(
                R.string.api_get_project));
        allUser = user.getData(getResources().getString(
                R.string.api_get_all_user));

        final String[] handlerNames = taskIntent.getNguoi_thuc_hien().split(",");
        final String[] viewerNames = taskIntent.getNguoi_xem().split(",");
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (allUser != null && allUser.size() > 0) {
                    for (int i = 0; i < handlerNames.length; i++) {
                        User user = findUserByName(handlerNames[i]);
                        if(user!=null)
                            listCheckedHandler.add(user);
                    }
                    String idUsersHandl = "";
                    for(int i = 0; i<listCheckedHandler.size();i++){
                        if(i < listCheckedHandler.size()-1){
                            idUsersHandl += ""+listCheckedHandler.get(i).getId()+",";
                        }else{
                            idUsersHandl += ""+listCheckedHandler.get(i).getId();
                        }
                    }
                    DialogChooseHandler.idUsersHandl = idUsersHandl;
                    for (int i = 0; i < viewerNames.length; i++) {
                        User user = findUserByName(viewerNames[i]);
                        if(user!=null)
                            listChecked.add(user);
                    }

                    String idUsersView = "";
                    for(int i = 0; i<listChecked.size();i++){
                        if(i < listChecked.size()-1){
                            idUsersView += ""+listChecked.get(i).getId()+",";
                        }else{
                            idUsersView += ""+listChecked.get(i).getId();
                        }
                    }
                    DialogChooseUser.idUsersView = idUsersView;
                } else {
                    handler.postDelayed(this, 50);
                }

            }
        };
        handler.post(runnable);
        // set data
        final Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DATE);
        months = c.get(Calendar.MONTH);
        years_now = c.get(Calendar.YEAR);

        toDate = new StringBuilder().append(padding_str(years_now)).append("-")
                .append(padding_str(months + 1)).append("-")
                .append(padding_str(date));

        /////////////
//        listCheckedHandler.add
//        String[] handlerNames = taskIntent.getNguoi_thuc_hien().split(",");
//        for (int i = 0; i < handlerNames.length; i++) {
//            listCheckedHandler.add(new User("", handlerNames[i], ""));
//        }
//        String[] viewerNames = taskIntent.getNguoi_xem().split(",");
//        for (int i = 0; i < viewerNames.length; i++) {
//            listChecked.add(new User("", viewerNames[i], ""));
//        }

        edtTitle.setText(taskIntent.getTen_cong_viec());
        edtDes.setText(taskIntent.getMo_ta());
        String[] stNgayBatDau = taskIntent.getNgay_bat_dau().split("/");
        txtDateHandle.setText(stNgayBatDau[2]+"-"+stNgayBatDau[1]+"-"+stNgayBatDau[0]);
        String[] stNgayKetThuc = taskIntent.getNgay_ket_thuc().split("/");
        txtDateFinish.setText(stNgayKetThuc[2]+"-"+stNgayKetThuc[1]+"-"+stNgayKetThuc[0]);
        txtHandler.setText(taskIntent.getNguoi_thuc_hien());
        txtViewer.setText(taskIntent.getNguoi_xem());
        txtDepartment.setText(taskIntent.getPhong_ban());
        txtProject.setText(taskIntent.getDu_an());
        spnPriority.setSelection(Integer.parseInt(taskIntent.getMuc_uu_tien()));
        DialogChooseDepartment.idDepSelected = Long.parseLong(taskIntent.getId_phong_ban());
        DialogChooseProject.idProjectSelected = Long.parseLong(taskIntent.getId_du_an());
        keyPriority = taskIntent.getMuc_uu_tien();
        Log.d("TuNT", "task id: " + taskIntent.getId());
    }

    // ------------choose date------------------//

    private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = dayOfMonth;
            months = monthOfYear;
            years_now = year;

            if (FLAG_DATE_PICKER == 1) {
                txtDateHandle.setTextColor(Color.parseColor(getResources()
                        .getString(R.color.actionbar_color)));
                txtDateHandle.setText(new StringBuilder()
                        .append(padding_str(years_now)).append("-")
                        .append(padding_str(months + 1)).append("-")
                        .append(padding_str(date)));
            } else {
                txtDateFinish.setTextColor(Color.parseColor(getResources()
                        .getString(R.color.actionbar_color)));
                txtDateFinish.setText(new StringBuilder()
                        .append(padding_str(years_now)).append("-")
                        .append(padding_str(months + 1)).append("-")
                        .append(padding_str(date)));
            }

        }

    };

    @Deprecated
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                datePickerListener, years_now, months, date);
        datePickerDialog.getDatePicker().setSpinnersShown(false);
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        return datePickerDialog;
    }

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                // clear data
                listChecked.clear();
                listCheckedHandler.clear();
                DialogChooseHandler.strUsersHandl = getResources().getString(
                        R.string.handler1);
                DialogChooseHandler.idUsersHandl = "";
                DialogChooseUser.strUsersView = getResources().getString(
                        R.string.viewer);
                DialogChooseUser.idUsersView = "";
                finish();
                break;
            case R.id.lnDateHandle:
                FLAG_DATE_PICKER = 1;
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.lnDateFinish:
                FLAG_DATE_PICKER = 0;
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.lnHandler:
                new DialogChooseHandler(activityContext, listDepUser, allUser,
                        listCheckedHandler);
                break;
            case R.id.lnViewer:
                ActionAdapter.flag = "chooseViewer";
                new DialogChooseUser(activityContext, listDepUser, allUser,
                        listChecked);
                break;
            case R.id.lnDepartment:
                new DialogChooseDepartment(activityContext, listDep);
                break;
            case R.id.lnProject:
                new DialogChooseProject(activityContext, listProject);
                break;
            case R.id.btnAssign:
                Log.d("TuNT","DialogChooseHandler.idUsersHandl: "+ DialogChooseHandler.idUsersHandl.toString());
                if (validatFromAssignTask()) {
                    DialogConfirmTask dialogConfirmTask = new DialogConfirmTask(activityContext, task);
                    dialogConfirmTask.setTitle(getResources().getString(R.string.app_name));
                    dialogConfirmTask.setMessage(getResources().getString(R.string.content_dialog_task_update_task));
                    dialogConfirmTask.setOnButtonDialogConfirmListener(getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), new DialogConfirmTask.OnButtonDialogConfirmListener() {
                        @Override
                        public void onClickBtnOk(View view) {
                            final ProgressDialog progressDialog = new ProgressDialog(
                                    activityContext);
                            progressDialog.setMessage(getResources().getString(
                                    R.string.waiting));

                            AsyncHttpClient client = new AsyncHttpClient();

                            RequestParams params = new RequestParams();
                            params.put("id_cong_viec", "" + taskIntent.getId());
                            params.put("ten_cong_viec", edtTitle.getText().toString().trim());
                            params.put("mo_ta", edtDes.getText().toString().trim());
                            params.put("ngay_bat_dau", txtDateHandle.getText().toString().trim());
                            params.put("ngay_ket_thuc", txtDateFinish.getText().toString().trim());
                            params.put("id_nguoi_thuc_hien", DialogChooseHandler.idUsersHandl);
                            params.put("nguoi_thuc_hien", txtHandler.getText().toString().trim());
                            params.put("id_nguoi_xem", DialogChooseUser.idUsersView);
                            params.put("nguoi_xem", txtViewer.getText().toString().trim());
                            params.put("phong_ban_pt", DialogChooseDepartment.idDepSelected);
                            params.put("id_du_an", DialogChooseProject.idProjectSelected);
                            params.put("uu_tien", keyPriority.trim());
                            client.post(EditTaskActivity.this, getResources().getString(R.string.api_edit_task), params, new JsonHttpResponseHandler() {
                                @Override
                                public void onStart() {
                                    progressDialog.show();
                                    super.onStart();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        if (response.getInt("success") == 1) {
                                            listChecked.clear();
                                            listCheckedHandler.clear();
                                            DialogChooseHandler.strUsersHandl = getResources()
                                                    .getString(R.string.handler1);
                                            DialogChooseHandler.idUsersHandl = "";
                                            DialogChooseUser.strUsersView = getResources()
                                                    .getString(R.string.viewer);
                                            DialogChooseUser.idUsersView = "";
                                            progressDialog.dismiss();
                                            Toast.makeText(activityContext,
                                                    getResources().getString(
                                                            R.string.success),
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            if (response.getInt("error") == 1)
                                                Toast.makeText(activityContext, response.getString("error_msg"),
                                                        Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    super.onSuccess(statusCode, headers, response);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activityContext, "Failure", Toast.LENGTH_LONG).show();
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activityContext, "Failure", Toast.LENGTH_LONG).show();
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }
                            });
                        }
                    });
                    dialogConfirmTask.showDialog();
                }
                break;
        }
    }

    private boolean validatFromAssignTask() {
        boolean validate = true;
        String title = edtTitle.getText().toString().trim();
        String description = edtDes.getText().toString().trim();
        String date_handle = txtDateHandle.getText().toString().trim();
        String date_finish = txtDateFinish.getText().toString().trim();
        String handler = txtHandler.getText().toString().trim();
        String viewer = txtViewer.getText().toString().trim();
        String department = txtDepartment.getText().toString().trim();

        if (title.equals("")) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.title_null),
                    Toast.LENGTH_SHORT).show();
            edtTitle.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            edtTitle.requestFocus();
            scrollView.setScrollY((int) lnTitle.getY());
            validate = false;

        } else if (description.equals("")) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.description_null),
                    Toast.LENGTH_SHORT).show();
            edtDes.requestFocus();
            edtDes.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnDescription.getY());
            validate = false;

        } else if (date_handle.equals(getResources().getString(
                R.string.date_handle))) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.date_handle_null),
                    Toast.LENGTH_SHORT).show();
            lnDateHandle.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnDateHandle.getY());
            validate = false;

        } else if (date_finish.equals(getResources().getString(
                R.string.date_finish))) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.date_finish_null),
                    Toast.LENGTH_SHORT).show();
            lnDateFinish.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnDateFinish.getY());
            validate = false;

        } else if (handler.equals(getResources().getString(R.string.handler1))) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.handler_null),
                    Toast.LENGTH_SHORT).show();
            lnHandler.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnHandler.getY());
            validate = false;

        } else if (viewer.equals(getResources().getString(R.string.viewer))) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.viewer_null),
                    Toast.LENGTH_SHORT).show();
            lnViewer.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnViewer.getY());
            validate = false;

        } else if (department.equals(getResources().getString(
                R.string.department))) {

            Toast.makeText(activityContext,
                    getResources().getString(R.string.department_null),
                    Toast.LENGTH_SHORT).show();
            lnDepartment.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.shake));
            scrollView.setScrollY((int) lnDepartment.getY());
            validate = false;

        } else {

            validate = true;

        }

        return validate;
    }

    private User findUserByName(String name) {
        for (int i = 0; i < allUser.size(); i++) {
            if (name.equals(allUser.get(i).getUsername()))
                return allUser.get(i);
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        DialogChooseHandler.VIEW_CURRENT = 0;
        super.onDestroy();
    }
}