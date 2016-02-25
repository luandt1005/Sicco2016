package com.sicco.erp.database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.NotificationModel;
import com.sicco.task.model.Task;

public class NotificationDBController extends SQLiteOpenHelper {
	
	public static String NOTIFICATION_STATE_NEW = "new";
	public static String NOTIFICATION_STATE_CHECKED = "checked";

	private static String DB_NAME = "sicco.db";
	private static int DB_VERSION = 1;
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.sicco.erp/databases/";
	
	private SQLiteDatabase mDatabase;
	private static NotificationDBController sInstance;
	private static Context mContext;
	public NotificationDBController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mDatabase = getWritableDatabase();
//		mContext = context;
	}
	public static NotificationDBController getInstance(Context context){
		mContext = context;
		if(sInstance == null) {
			sInstance = new NotificationDBController(context);
		}
		
		return sInstance;
	}
	
	public static String TABLE_NAME = "notification_tbl";
	public static String DISPATCH_TABLE_NAME = "dispatch_tbl";
	public static String TASK_TABLE_NAME = "task_tbl";
	public static String REPORT_TABLE_NAME = "report_tbl";
	
	public static String ID_COL = "_id";
	public static String DISPATCH_COL = "did";
	public static String SOHIEUCONGVAN_COL = "soHieuCongVan";
	public static String TRICHYEU_COL = "trichYeu";
	public static String DINHKEM_COL = "dinhKem";
	public static String NGAYDENSICCO_COL = "ngayDenSicco";
	public static String TRANGTHAI_COL = "trangThai";
	public static String USERNAME_COL = "username";
	public static String NOTIFI_TYPE_COL = "notifi_type";
	public static String DSTATE_COL = "dstate";
	
	public static String D_NUMBER_DISPATCH_COL = "d_number_dispatch";
	public static String D_TYPE_COL = "d_type";
	public static String D_DESCRIPTION_COL = "d_description";
	public static String D_CONTENT_COL = "d_content";
	public static String D_DATE_COL = "d_date";
	public static String D_STATUS_COL = "d_status";
	public static String D_HANDLER_COL = "d_handler";
	
	public static String TASK_NGUOIXEM = "task_nguoixem";
	public static String TASK_NGUOITHUCHIEN = "task_nguoithuchien";
	public static String TASK_TENCONGVIEC = "task_tencongviec";
	public static String TASK_CONTENT = "task_content";
	public static String TASK_STATE = "trang_thai";
	
	public static String REPORT_CONTENT = "noi_dung";
	public static String REPORT_HANDLER = "nguoi_xu_ly";
	public static String REPORT_DATE = "date";
	
	
	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ ID_COL + " integer primary key autoincrement,"
			+ NOTIFI_TYPE_COL + " integer,"
			+ USERNAME_COL + " text,"
			+ SOHIEUCONGVAN_COL + " text,"
			+ TRICHYEU_COL + " text,"
			+ DINHKEM_COL + " text,"
			+ NGAYDENSICCO_COL + " text,"
			+ TRANGTHAI_COL + " text);";
	
	private static String CREATE_DISPATCH_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ DISPATCH_TABLE_NAME + "("
			+ DISPATCH_COL + " integer primary key autoincrement,"
			+ USERNAME_COL + " text,"
			+ D_TYPE_COL + " text,"
			+ D_NUMBER_DISPATCH_COL + " text,"
			+ D_DESCRIPTION_COL + " text,"
			+ D_CONTENT_COL + " text,"
			+ D_DATE_COL + " text,"
			+ D_STATUS_COL + " text,"
			+ D_HANDLER_COL + " text,"
			+ DSTATE_COL + " text);";
	
	private static String CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TASK_TABLE_NAME + "("
			+ ID_COL + " integer primary key autoincrement,"
			+ USERNAME_COL + " text,"
			+ TASK_TENCONGVIEC + " text,"
			+ TASK_NGUOIXEM + " text,"
			+ TASK_NGUOITHUCHIEN + " text,"
			+ TASK_CONTENT + " text,"
			+ TASK_STATE + " text,"
			+ REPORT_DATE + " text,"
			+ TRANGTHAI_COL + " text);";
	
	private static String CREATE_REPORT_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ REPORT_TABLE_NAME + "("
			+ ID_COL + " integer primary key autoincrement,"
			+ USERNAME_COL + " text,"
			+ REPORT_HANDLER + " text,"
			+ REPORT_DATE + " text,"
			+ REPORT_CONTENT + " text);";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_DISPATCH_TABLE);
		db.execSQL(CREATE_TASK_TABLE);
		db.execSQL(CREATE_REPORT_TABLE);
//		db.execSQL(CREATE_STATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + DISPATCH_TABLE_NAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + REPORT_TABLE_NAME);
		onCreate(arg0);
	}
	
	public void openDB() throws SQLException{
		String myPath = DB_PATH+ DB_NAME;
		try {
			mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
		}
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy){
		if (mDatabase == null) openDB();
		
		Cursor cursor = mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	
	public Cursor rawQuery(String stSql, String[] data) {
		if (mDatabase == null)
			openDB();

		Cursor cursor = mDatabase.rawQuery(stSql, data);
		return cursor;
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values){
		if (mDatabase == null) 
			openDB();
		
		long insertRet = mDatabase.insert(table, nullColumnHack, values);
		return insertRet;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
		if( mDatabase == null) openDB();
		
		int ret = mDatabase.update(table, values, whereClause, whereArgs);
		
		return ret;
	}
	
	
	public void checkedNotification(NotificationModel item, int id){
		ContentValues values = new ContentValues();
		values.put(TRANGTHAI_COL, NOTIFICATION_STATE_CHECKED);
		
		String where = NotificationDBController.ID_COL + " = " + id;
		update(TABLE_NAME, values, where, null);
	}
	
	public void checkedDisPatch(Dispatch item, long id){
		ContentValues values = new ContentValues();
		values.put(DSTATE_COL, NOTIFICATION_STATE_CHECKED);
		
		String where = NotificationDBController.DISPATCH_COL + " = " + id;
		update(DISPATCH_TABLE_NAME, values, where, null);
	}
	
	public void checkedTask(Task item, long id){
		ContentValues values = new ContentValues();
		values.put(TRANGTHAI_COL, NOTIFICATION_STATE_CHECKED);
		
		String where = NotificationDBController.ID_COL + " = " + id;
		update(TASK_TABLE_NAME, values, where, null);
	}
	
	public void changeStateDisPatch(Dispatch item, long id, int state, String sstate){
		ContentValues values = new ContentValues();
		values.put(D_TYPE_COL, state);
		values.put(DSTATE_COL, sstate);
		
		String where = NotificationDBController.DISPATCH_COL + " = " + id;
		update(DISPATCH_TABLE_NAME, values, where, null);
	}
	
	public void changeTaskState(Task item, long id, String state){
		ContentValues values = new ContentValues();
		values.put(TASK_STATE, state);
		
		String where = NotificationDBController.ID_COL + " = " + id;
		update(TASK_TABLE_NAME, values, where, null);
	}
	
	public void deleteAllData()
	{
	    SQLiteDatabase sdb= this.getWritableDatabase();
	    sdb.delete(TABLE_NAME, null, null);
	    sdb.delete(DISPATCH_TABLE_NAME, null, null);
	    sdb.delete(TASK_TABLE_NAME, null, null);
	    deleteReportData();
	}
	
	public void deleteReportData(){
		SQLiteDatabase sdb= this.getWritableDatabase();
		String currentDate = getCurrentDate();
//	    
//	    String sql = "Delete from " + REPORT_TABLE_NAME + " where "
//				+ NotificationDBController.REPORT_DATE
//				+ " = \"" + currentDate + "\"";
//	    Log.d("MyDebug", "deleteReportData sql : " + sql);
//	    sdb.rawQuery(sql, null);
		
		sdb.delete(REPORT_TABLE_NAME, NotificationDBController.REPORT_DATE
				+ " != \"" + currentDate + "\"", null);
	}
	
	public void deleteStateData(){
		SQLiteDatabase sdb= this.getWritableDatabase();
		String currentDate = getCurrentDate();
		
		sdb.delete(REPORT_TABLE_NAME, NotificationDBController.REPORT_DATE
				+ " != \"" + currentDate + "\"", null);
	}
	
	void cancelNotification(Context context, int id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(id);
	}
	
	public String getCurrentDate(){
		String currentDate = "";
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String currentDateandTime = sdf.format(new Date());
			Date cdate=sdf.parse(currentDateandTime);
			Calendar now2= Calendar.getInstance();
			now2.add(Calendar.DATE, 0);
			
			int d = now2.get(Calendar.DATE);
			int m = now2.get(Calendar.MONTH) + 1;
			String month = "", date = "";
			if(m < 10){
				month = "0" + m;
			} else {
				month = "" + m;
			}
			if(d < 10){
				date = "0" + d;
			} else {
				date = "" + d;
			}
			
			int year = now2.get(Calendar.YEAR);
			String beforedate = year +"/" + month + "/" + date;
			Date BeforeDate1=sdf.parse(beforedate);
			cdate.compareTo(BeforeDate1);
			
			currentDate = beforedate.replace("/", "-");
			
			Log.d("MyDebug", "secondary List : " + currentDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return currentDate;
	}
}