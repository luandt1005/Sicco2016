package com.sicco.erp.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.HomeActivity.NotifyBR;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.DispatchType;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.util.BadgeUtils;
import com.sicco.erp.util.Utils;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GetAllNotificationService extends Service {
    private String url_get_notification = "", url_get_congviec = "", url_get_binhluan = "", url_get_state = "";
    private Cursor cursor;
    private NotificationDBController db;
    // ArrayList and variable to getCount
    private ArrayList<NotificationModel> congVanXuLy_list;
    private ArrayList<NotificationModel> congVanCanPhe_list;

    private ArrayList<Task> taskData, stateData;
    private ArrayList<ReportSteerTask> reportData;
    private ArrayList<ReportSteer> reportCongVanData;
    // count
    static String CONGVIEC = "congviec", CONGVAN = "congvan", LICHBIEU = "lichbieu";
    //
    long id;
    int total;
    int action, CV_CHUA_XL;
    NotifyBR notifyBR;
    String username = "";
    boolean inserted = false;
    boolean insertedTask = false;
    String cv_id = "";

    // key
    public static String CVCP_KEY = "CONGVIECCANPHE_KEY";
    public static String CVXL_KEY = "CONGVIECXULY_KEY";
    public static String CL_KEY = "CACLOAI_KEY";
    public static String CV_KEY = "CONGVIEC_KEY";
    public static String BL_ID = "BL_ID";
    public static String TOTAL_KEY = "TOTAL_KEY";

    private Dispatch dispatch = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(notifyBR);
        } catch (Exception ex) {

        }
    }

    @SuppressWarnings("null")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // start Service
        insertedTask = false;
        inserted = false;
        Log.d("LuanDT", "-------------->>>>>onStartCommand");

        CongVanCanPheAsync();
        CongVanXuLyAsync();
        // CacLoaiAsync();
        StateAsync();
        BinhLuanAsync();
        BinhLuanCongVanAsync();
        CongViecAsync();

        if (intent != null) {
            action = intent.getIntExtra("ACTION", 1);
        } else {
            try {
                action = intent.getIntExtra("ACTION", 0);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        Utils.scheduleNext(getApplicationContext());

        return START_STICKY;
    }

    void CongVanCanPheAsync() {
        AsyncHttpClient handler = new AsyncHttpClient();
        url_get_notification = getResources().getString(R.string.api_get_dispatch_approval);

        congVanCanPhe_list = new ArrayList<NotificationModel>();

        RequestParams params = new RequestParams();
        String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
        String userId = Utils.getString(getApplicationContext(), SessionManager.KEY_USER_ID);
        params.add("username", username);
        params.add("userId", userId);

        handler.post(getApplicationContext(), url_get_notification, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String st = response.toString();

                try {
                    JSONObject json = new JSONObject(st);
                    total = json.getInt("total");
                    JSONArray rows = json.getJSONArray("row");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);
                        // get all data
                        long id = row.getInt("id");
                        String soHieuCongVan = row.getString("so_hieu");
                        String trichYeu = row.getString("trich_yeu");
                        String dinhKem = row.getString("content");
                        String ngayDenSicco = row.getString("ngay_den");
                        String trangThai = row.getString("status");
                        String pheduyet = row.getString("phe_duyet");

                        if (pheduyet.equals("0")) {
                            congVanCanPhe_list.add(new NotificationModel(id, 1, soHieuCongVan, trichYeu, dinhKem,
                                    ngayDenSicco, trangThai));
                        }
                    }
                    origanizeNoti(congVanCanPhe_list, congVanCanPhe_list.size(), 1);
                    saveInt(1, total);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });

    }

    void CongVanXuLyAsync() {
        AsyncHttpClient handler = new AsyncHttpClient();
        url_get_notification = getResources().getString(R.string.api_get_dispatch_handle);

        congVanXuLy_list = new ArrayList<NotificationModel>();

        RequestParams params = new RequestParams();
        String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
        String userId = Utils.getString(getApplicationContext(), SessionManager.KEY_USER_ID);
        params.add("username", username);
        params.add("userId", userId);

        handler.post(getApplicationContext(), url_get_notification, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String st = response.toString();

                try {
                    JSONObject json = new JSONObject(st);
                    total = json.getInt("total");
                    JSONArray rows = json.getJSONArray("row");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);
                        // get all data
                        long id = row.getInt("id");
                        String soHieuCongVan = row.getString("so_hieu");
                        String trichYeu = row.getString("trich_yeu");
                        String dinhKem = row.getString("content");
                        String ngayDenSicco = row.getString("ngay_den");
                        String trangThai = row.getString("status");

                        // if (trangThai.equals("2")) {
                        congVanXuLy_list.add(new NotificationModel(id, 2, soHieuCongVan, trichYeu, dinhKem,
                                ngayDenSicco, trangThai));
                        // }

                    }
                    origanizeNoti(congVanXuLy_list, congVanXuLy_list.size(), 2);
                    saveInt(2, total);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });

    }

    void CongViecAsync() {
        Log.d("LuanDT", "CongViecAsync");
        url_get_congviec = getResources().getString(R.string.api_get_cv_duoc_giao_noti);
        taskData = new ArrayList<Task>();
        taskData.clear();
        CV_CHUA_XL = 0;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", Utils.getString(getApplicationContext(), "user_id"));
        params.add("username", Utils.getString(getApplicationContext(), "name"));

        client.post(url_get_congviec, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String jsonRead = response.toString();
                if (!jsonRead.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(jsonRead);
                        total = object.getInt("total");
                        JSONArray rows = object.getJSONArray("row");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);
                            String id = row.getString("id");
                            String ten_cong_viec = row.getString("ten_cong_viec");
                            String nguoi_thuc_hien = row.getString("nguoi_thuc_hien");
                            String nguoi_xem = row.getString("nguoi_xem");
                            String mo_ta = row.getString("mo_ta");
                            String trang_thai = row.getString("trang_thai");
                            String isXuLy = row.getString("isXuLy");
                            String cbl = row.getString("co_binh_luan");
                            String is_nguoi_xem = row.getString("is_nguoi_xem");
                            boolean co_binh_luan = false;
                            if (cbl.equals("1")) {
                                co_binh_luan = true;
                            }
                            //neu la cv chua xu ly
                            if (is_nguoi_xem.equals("1")) {
                                //no thing
                            } else {
                                insertedTask = true;
                                //Số công việc chưa xử lý
                                CV_CHUA_XL++;
                            }
                            //data show noti
                            taskData.add(new Task(Long.parseLong(id), ten_cong_viec, nguoi_thuc_hien,
                                    nguoi_xem, mo_ta, is_nguoi_xem));
                        }

                        boolean firstRun = Utils.getBoolean(getApplicationContext(), "FIRSTRUN", true);
                        if (firstRun) {
                            Log.d("LuanDT", "firstRun");
                            Utils.saveBoolean(getApplicationContext(), "FIRSTRUN", false);
                            origanizeCongViecNoti(taskData, taskData.size());
                            saveCVInt(CV_CHUA_XL);
                        } else {
                            Log.d("LuanDT", "not firstRun");
                            if (insertedTask) {
                                Log.d("LuanDT", "insertedTask---->goi noti");
                                origanizeCongViecNoti(taskData, taskData.size());
                                saveCVInt(CV_CHUA_XL);
                                //CongViec(username);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    public void CongViec(String username) {
        int count = 0;
        db = NotificationDBController.getInstance(getApplicationContext());
        cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null, null, null, null, null, null);
        String sql = "Select * from " + NotificationDBController.TASK_TABLE_NAME + " where "
                + NotificationDBController.TRANGTHAI_COL + " = \"new\"" + " and " + NotificationDBController.TASK_STATE
                + " = \"active\"" + " and " + NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
        cursor = db.rawQuery(sql, null);
        count = cursor.getCount();
        // origanizeNoti(cacLoai_list, count);
        // origanizeCongViecNoti(taskData, taskData.size());
        saveCVInt(count);
    }

    void BinhLuanAsync() {
        Log.d("LuanDT", "BinhLuanAsync");
        db = NotificationDBController.getInstance(getApplicationContext());
        db.deleteReportData();

        url_get_binhluan = getResources().getString(R.string.api_tatcabinhluan);
        reportData = new ArrayList<ReportSteerTask>();
        RequestParams params = new RequestParams();
        params.add("user_id", Utils.getString(GetAllNotificationService.this, "user_id"));
        params.add("username", Utils.getString(GetAllNotificationService.this, "name"));
        // Log.d("LuanDT", "params: " + params);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url_get_binhluan, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String jsonRead = response.toString();
                // Log.d("LuanDT", "jsonRead: " + jsonRead);
                if (!jsonRead.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(jsonRead);
                        JSONArray rows = object.getJSONArray("row");

                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);

                            String id = row.getString("id");
                            String date = row.getString("thoi_gian");
                            String handler = row.getString("nguoi_binh_luan");
                            String content = row.getString("noi_dung");
                            String id_cv = row.getString("id_cv");

                            date = date.substring(0, 10);
                            // add to db

                            String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
                            String sql = "Select * from " + NotificationDBController.REPORT_TABLE_NAME + " where "
                                    + NotificationDBController.ID_COL + " = " + id + " and "
                                    + NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
                            cursor = db.rawQuery(sql, null);

                            if (cursor != null && cursor.getCount() > 0) {

                            } else {
                                ContentValues values = new ContentValues();
                                values.put(NotificationDBController.ID_COL, id);
                                values.put(NotificationDBController.USERNAME_COL, username);
                                values.put(NotificationDBController.REPORT_CONTENT, content);
                                values.put(NotificationDBController.REPORT_DATE, date);
                                values.put(NotificationDBController.REPORT_HANDLER, handler);

                                long insertResult = db.insert(NotificationDBController.REPORT_TABLE_NAME, null, values);
                                if (insertResult != -1) {
                                    inserted = true;
                                    Log.d("LuanDT", "inserted comment ---->show noti binh luan");
                                    reportData.add(new ReportSteerTask(Long.parseLong(id), handler, content, id_cv));
                                }
                            }

                        }
                        if (inserted) {
                            Log.d("LuanDT", "data comment: " + reportData.size());
                            origanizeBinhLuanNoti(reportData, reportData.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private ArrayList<DispatchType> dataDispatchType = new ArrayList<DispatchType>();

    void BinhLuanCongVanAsync() {
        Log.d("BinhLuanCongVanAsync", "BinhLuanCongVanAsync");

        db = NotificationDBController.getInstance(getApplicationContext());
        //db.deleteReportCongVanData();

        url_get_binhluan = getResources().getString(R.string.api_get_steer_congvan_report);
        reportCongVanData = new ArrayList<ReportSteer>();
        RequestParams params = new RequestParams();
        params.add("user_id", Utils.getString(GetAllNotificationService.this, "user_id"));
        params.add("username", Utils.getString(GetAllNotificationService.this, "name"));
        // Log.d("LuanDT", "params: " + params);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url_get_binhluan, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String jsonRead = response.toString();
                Log.d("BinhLuanCongVanAsync", "BinhLuanCongVanAsync jsonRead: " + jsonRead);
                if (!jsonRead.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(jsonRead);
                        JSONArray rows = object.getJSONArray("row");

                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);

                            String id = row.getString("id");
                            String date = row.getString("thoi_gian");
                            String handler = row.getString("nguoi_binh_luan");
                            String content = row.getString("noi_dung");
                            long id_cong_van = row.getLong("id_cong_van");

                            // congvan
                            String cv_numberDispatch = row.getString("so_hieu");
                            String cv_description = row.getString("trich_yeu");
                            String cv_content = row.getString("content");
                            String cv_date = row.getString("ngay_den");
                            String cv_status = row.getString("status");
                            String cv_coQuanBanHanh = row
                                    .getString("co_quan_ban_hanh");
                            String cv_handler = row.getString("handler");
                            String cv_idloaicv = row.getString("id_loai_cong_van");
                            String cv_nguoithaydoitrangthai = row
                                    .getString("nguoi_thay_doi_trang_thai");
                            String cv_pheduyet = row.getString("phe_duyet");
                            String cv_nguoi_phe_duyet = row.getString("nguoi_phe_duyet");

                            cv_content = content.replace(" ", "%20");
                            cv_date = date.substring(0, 10);

                            DispatchType dispatchType = new DispatchType();
                            dataDispatchType = dispatchType.getData(getApplicationContext().getResources()
                                    .getString(R.string.api_get_dispatch_type));

                            if (!dataDispatchType.isEmpty()) {
                                for (int j = 0; j < dataDispatchType.size(); j++) {
                                    DispatchType type = dataDispatchType.get(j);
                                    if (cv_idloaicv.equals(type.getId())) {
                                        dispatch = new Dispatch(id_cong_van,
                                                cv_numberDispatch, cv_description,
                                                cv_content, cv_date, cv_handler, cv_status,
                                                cv_coQuanBanHanh, type.getTitle(),
                                                cv_nguoithaydoitrangthai, cv_pheduyet, cv_nguoi_phe_duyet);
                                    } else {
                                        dispatch = new Dispatch(id_cong_van,
                                                cv_numberDispatch, cv_description,
                                                cv_content, cv_date, cv_handler, cv_status,
                                                cv_coQuanBanHanh, "",
                                                cv_nguoithaydoitrangthai, cv_pheduyet, cv_nguoi_phe_duyet);
                                    }
                                }
                            } else {
                                dispatch = new Dispatch(id_cong_van,
                                        cv_numberDispatch, cv_description,
                                        cv_content, cv_date, cv_handler, cv_status,
                                        cv_coQuanBanHanh, "",
                                        cv_nguoithaydoitrangthai, cv_pheduyet, cv_nguoi_phe_duyet);
                            }
                            // add to db

                            String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
                            String sql = "Select * from " + NotificationDBController.REPORT_CONGVAN_TABLE_NAME + " where "
                                    + NotificationDBController.ID_COL + " = " + id + " and "
                                    + NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
                            cursor = db.rawQuery(sql, null);

                            if (cursor != null && cursor.getCount() > 0) {

                            } else {
                                ContentValues values = new ContentValues();
                                values.put(NotificationDBController.ID_COL, id);
                                values.put(NotificationDBController.USERNAME_COL, username);
                                values.put(NotificationDBController.REPORT_CONTENT, content);
                                values.put(NotificationDBController.REPORT_DATE, date);
                                values.put(NotificationDBController.REPORT_HANDLER, handler);

                                long insertResult = db.insert(NotificationDBController.REPORT_CONGVAN_TABLE_NAME, null, values);
                                if (insertResult != -1) {
                                    inserted = true;
                                    Log.d("BinhLuanCongVanAsync", "BinhLuanCongVanAsync inserted comment ---->show noti binh luan");
                                    reportCongVanData.add(new ReportSteer(Long.parseLong(id), handler, date, content, id_cong_van));
                                }
                            }

                        }
                        if (inserted) {
                            Log.d("BinhLuanCongVanAsync", "data comment: " + reportCongVanData.size());
                            origanizeBinhLuanCongVanNoti(reportCongVanData, reportCongVanData.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    void StateAsync() {

        db = NotificationDBController.getInstance(getApplicationContext());
        db.deleteStateData();

        url_get_state = getResources().getString(R.string.api_get_state);
        stateData = new ArrayList<Task>();
        stateData.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", Utils.getString(getApplicationContext(), "user_id"));
        params.add("username", Utils.getString(getApplicationContext(), "name"));

        Log.d("MyDebug", "StateAsync");

        client.post(url_get_state, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String jsonRead = response.toString();
                Log.d("MyDebug", "json ======= : " + jsonRead);
                if (!jsonRead.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(jsonRead);
                        total = object.getInt("total");
                        JSONArray rows = object.getJSONArray("row");

                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);
                            String id = row.getString("id");
                            String ten_cong_viec = row.getString("ten_cong_viec");
                            String trang_thai = row.getString("trang_thai");
                            String nguoi_xem = row.getString("nguoi_xem");
                            String nguoi_thuc_hien = row.getString("nguoi_thuc_hien");
                            String mo_ta = row.getString("mo_ta");

                            String date = db.getCurrentDate();
                            // add to db
                            db = NotificationDBController.getInstance(getApplicationContext());
                            username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);

                            String sql = "Select * from " + NotificationDBController.TASK_TABLE_NAME + " where "
//									+ NotificationDBController.ID_COL + " = " + id + " and "
                                    + NotificationDBController.TASK_STATE + " != \"" + "active" + "\"";
                            cursor = db.rawQuery(sql, null);

                            if (cursor != null && cursor.getCount() > 0) {

                            } else {
                                ContentValues values = new ContentValues();
                                values.put(NotificationDBController.ID_COL, id);
                                values.put(NotificationDBController.USERNAME_COL, username);
                                values.put(NotificationDBController.TASK_TENCONGVIEC, ten_cong_viec);
                                values.put(NotificationDBController.REPORT_DATE, date);
                                values.put(NotificationDBController.TASK_STATE, trang_thai);
                                values.put(NotificationDBController.TRANGTHAI_COL,
                                        NotificationDBController.NOTIFICATION_STATE_NEW);

                                long resultInsert = db.insert(NotificationDBController.TASK_TABLE_NAME, null, values);
                                if (resultInsert != -1) {
                                    insertedTask = true;
                                    Log.d("LuanDT", "---->>>>>inserted table cv");

//									stateData.add(new Task(Long.parseLong(id), ten_cong_viec, trang_thai));
                                    stateData.add(new Task(Long.parseLong(id), ten_cong_viec, nguoi_thuc_hien,
                                            nguoi_xem, mo_ta, trang_thai));
                                }

                            }
                        }

                        boolean firstRun = Utils.getBoolean(getApplicationContext(), "FIRSTRUN", true);
                        if (firstRun) {
                            Utils.saveBoolean(getApplicationContext(), "FIRSTRUN", false);
                            origanizeCongViecStateNoti(stateData, stateData.size());
                        } else {
                            if (insertedTask) {
                                origanizeCongViecStateNoti(stateData, stateData.size());

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    void origanizeBinhLuanNoti(ArrayList<ReportSteerTask> data, int notification_count) {
        sereprateBinhLuanList(data, notification_count);
        Log.d("LuanDT", "origanizeBinhLuanNoti");
    }

    void origanizeBinhLuanCongVanNoti(ArrayList<ReportSteer> data, int notification_count) {
        sereprateBinhLuanCongVanList(data, notification_count);
        Log.d("BinhLuanCongVanAsync", "origanizeBinhLuanNoti");
    }

    void origanizeCongViecNoti(ArrayList<Task> data, int notification_count) {
        sereprateCongViecList(data, notification_count);
        notifyBR = new NotifyBR();
        IntentFilter intentFilter = new IntentFilter("acb");
        registerReceiver(notifyBR, intentFilter);
        Intent i = new Intent("acb");
        sendBroadcast(i);
        Log.d("ToanNM", "sereprateCongViecList");

        getTotalNotification();
    }

    void origanizeCongViecStateNoti(ArrayList<Task> data, int notification_count) {
        sereprateCongViecState(data, notification_count);
    }

    ArrayList<Dispatch> dData;

    void cacLoai(String username) {
        int count = 0;
        db = NotificationDBController.getInstance(getApplicationContext());
        cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null, null, null, null, null, null);
        String sql = "Select * from " + NotificationDBController.DISPATCH_TABLE_NAME + " where "
                + NotificationDBController.DSTATE_COL + " = \"new\"" + " and " + NotificationDBController.D_TYPE_COL
                + " = " + 1 + " and " + NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
        cursor = db.rawQuery(sql, null);
        count = cursor.getCount();
        // ================================================= \\
        // origanizeNoti(cacLoai_list, count);
        sereprateCacLoaiList(dData, count);
        saveInt(3, count);
    }

    void saveInt(int type, int size) {
        if (type == 1) {
            Utils.saveInt(getApplicationContext(), CVCP_KEY, size);
        } else if (type == 2) {
            Utils.saveInt(getApplicationContext(), CVXL_KEY, size);
        } else if (type == 3) {
            Utils.saveInt(getApplicationContext(), CL_KEY, size);
        }
        getTotalNotification();
    }

    public void saveCVInt(int size) {
        Utils.saveInt(getApplicationContext(), CV_KEY, size);
        getTotalNotification();
    }

    void getTotalNotification() {
        int cvcp_total = Utils.getInt(getApplicationContext(), CVCP_KEY, 0);
        int cvxl_total = Utils.getInt(getApplicationContext(), CVXL_KEY, 0);
        int cv_total = Utils.getInt(getApplicationContext(), CV_KEY, 0);
        int total = cvcp_total + cvxl_total + cv_total;
        Utils.saveInt(getApplicationContext(), TOTAL_KEY, total);
        BadgeUtils.setBadge(getApplicationContext(), total);

    }

    void origanizeNoti(ArrayList<NotificationModel> data, int notification_count, int type) {
        sereprateList(data, notification_count, type);
        notifyBR = new NotifyBR();
        IntentFilter intentFilter = new IntentFilter("acb");
        registerReceiver(notifyBR, intentFilter);
        Intent i = new Intent("acb");
        sendBroadcast(i);
    }

    void sereprateList(ArrayList<NotificationModel> data, int notification_count, int type) {
        int size = data.size();
        if (action == 1 && size != 0) {
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyType(getApplicationContext(), data, notification_count);
        }
        if (size == 0) {
            cancelNotification(getApplicationContext(), type);
            saveInt(type, size);
        }
    }

    void sereprateCacLoaiList(ArrayList<Dispatch> data, int notification_count) {
        int size = data.size();
        if (action == 1 && size != 0) {
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyCacLoai(getApplicationContext(), data, notification_count);
        }
        if (size == 0) {
            cancelNotification(getApplicationContext(), 3);
            saveInt(3, notification_count);
        }
    }

    void sereprateCongViecList(ArrayList<Task> data, int notification_count) {
        int size = data.size();
        if (action == 1 && size != 0) {
            Log.d("MyDebug", "sereprateCongViecList action : " + action);
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyCongViec(getApplicationContext(), data);
            Log.d("ToanNM", "CongViec Notification <=");
        }
    }

    void sereprateCongViecState(ArrayList<Task> data, int notification_count) {
        int size = data.size();
        if (action == 1 && size != 0) {
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyState(getApplicationContext(), data);
        }
    }

    void sereprateBinhLuanList(ArrayList<ReportSteerTask> data, int notification_count) {
        int size = data.size();
        if (action == 1 && size != 0) {
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyBinhLuan(getApplicationContext(), data);
            Log.d("LuanDT", "sereprateBinhLuanList: " + data.size());
        }
    }

    void sereprateBinhLuanCongVanList(ArrayList<ReportSteer> data, int notification_count) {
        int size = data.size();
        if (action == 1 && size != 0) {
            Log.d("BinhLuanCongVanAsync", "" + dispatch);
            MyNotificationManager myNotificationManager = new MyNotificationManager();
            myNotificationManager.notifyBinhLuanCongVan(getApplicationContext(), data, dispatch);
            Log.d("BinhLuanCongVanAsync", "sereprateBinhLuanList: " + data.size());
        }
    }

    void cancelNotification(Context context, int notification_id) {
        String notificationServiceStr = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(notificationServiceStr);
        mNotificationManager.cancel(notification_id);
    }

    // getCurrentDate
    String getCurrentDate() {
        String currentDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDateandTime = sdf.format(new Date());
            Date cdate = sdf.parse(currentDateandTime);
            Calendar now2 = Calendar.getInstance();
            now2.add(Calendar.DATE, 0);

            int d = now2.get(Calendar.DATE);
            int m = now2.get(Calendar.MONTH) + 1;
            String month = "", date = "";
            if (m < 10) {
                month = "0" + m;
            } else {
                month = "" + m;
            }
            if (d < 10) {
                date = "0" + d;
            } else {
                date = "" + d;
            }

            int year = now2.get(Calendar.YEAR);
            String beforedate = year + "/" + month + "/" + date;
            Date BeforeDate1 = sdf.parse(beforedate);
            cdate.compareTo(BeforeDate1);

            currentDate = beforedate.replace("/", "-");

            Log.d("MyDebug", "secondary List : " + currentDate);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return currentDate;
    }

}

// add to db
//							db = NotificationDBController.getInstance(getApplicationContext());
//							username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
//
//							if (!id.equals("")) {
//								String sql = "Select * from " + NotificationDBController.TASK_TABLE_NAME + " where "
//										+ NotificationDBController.TRANGTHAI_COL + " = \"new\"" + " and "
//										+ NotificationDBController.ID_COL + " = " + id + " and "
//										+ NotificationDBController.TASK_STATE + " = \"active\"" + " and "
//										+ NotificationDBController.USERNAME_COL + " = \"" + username + "\"";
//								cursor = db.rawQuery(sql, null);
//
//								if (cursor != null && cursor.getCount() > 0) {
//
//								} else {
//									ContentValues values = new ContentValues();
//									values.put(NotificationDBController.ID_COL, id);
//									values.put(NotificationDBController.USERNAME_COL, username);
//									values.put(NotificationDBController.TASK_TENCONGVIEC, ten_cong_viec);
//									values.put(NotificationDBController.TASK_NGUOIXEM, nguoi_xem);
//									values.put(NotificationDBController.TASK_NGUOITHUCHIEN, nguoi_thuc_hien);
//									values.put(NotificationDBController.TASK_STATE, trang_thai);
//									values.put(NotificationDBController.TRANGTHAI_COL,
//											NotificationDBController.NOTIFICATION_STATE_NEW);
//
//									long resultInsert = db.insert(NotificationDBController.TASK_TABLE_NAME, null,
//											values);
//									if (resultInsert != -1) {
//										insertedTask = true;
//										Log.d("LuanDT", "---->>>>>inserted table cv");
//
//										taskData.add(new Task(Long.parseLong(id), ten_cong_viec, nguoi_thuc_hien,
//												nguoi_xem, mo_ta));
//									}
//
//								}
//							}