package com.sicco.erp.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.util.AccentRemover;
import com.sicco.erp.util.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class Dispatch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	private long id;
	private String numberDispatch, description, content;
	private String date, handler;
	private String status;
	private String coQuanBanHanh;
	private String loaicongvan;
	private String idLoaicongvan;
	private String nguoithaydoitrangthai;
	private String pheduyet;
	private String nguoi_phe_duyet;
	private String nguoiXem;
	public String da_xu_ly;

	private ArrayList<Dispatch> data;
	private ArrayList<DispatchType> dataDispatchType;

	private Cursor cursor;
	private NotificationDBController db;

	public Dispatch(Context context) {
		this.context = context;
	}

	public Dispatch(long id, String numberDispatch, String description,
			String content, String date, String handler, String status,
			String coQuanBanHanh, String loaicongvan, String idLoaicongvan,
			String nguoithaydoitrangthai, String pheduyet, String nguoi_phe_duyet, String nguoiXem, String da_xu_ly) {
		super();
		this.id = id;
		this.numberDispatch = numberDispatch;
		this.description = description;
		this.content = content;
		this.date = date;
		this.handler = handler;
		this.status = status;
		this.coQuanBanHanh = coQuanBanHanh;
		this.loaicongvan = loaicongvan;
		this.idLoaicongvan = idLoaicongvan;
		this.nguoithaydoitrangthai = nguoithaydoitrangthai;
		this.pheduyet = pheduyet;
		this.nguoi_phe_duyet =  nguoi_phe_duyet;
		this.nguoiXem =  nguoiXem;
		this.da_xu_ly = da_xu_ly;
	}
	public Dispatch(long id, String numberDispatch, String description,
			String content, String date, String handler, String status,
			String coQuanBanHanh, String loaicongvan,
			String nguoithaydoitrangthai, String pheduyet, String nguoi_phe_duyet) {
		super();
		this.id = id;
		this.numberDispatch = numberDispatch;
		this.description = description;
		this.content = content;
		this.date = date;
		this.handler = handler;
		this.status = status;
		this.coQuanBanHanh = coQuanBanHanh;
		this.loaicongvan = loaicongvan;
		this.nguoithaydoitrangthai = nguoithaydoitrangthai;
		this.pheduyet = pheduyet;
		this.nguoi_phe_duyet =  nguoi_phe_duyet;
	}

	public Dispatch(long id, String numberDispatch, String description,
			String content, String date, String handler, String status) {
		super();
		this.id = id;
		this.numberDispatch = numberDispatch;
		this.description = description;
		this.content = content;
		this.date = date;
		this.handler = handler;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public String getNguoithaydoitrangthai() {
		return nguoithaydoitrangthai;
	}

	public void setNguoithaydoitrangthai(String nguoithaydoitrangthai) {
		this.nguoithaydoitrangthai = nguoithaydoitrangthai;
	}

	public String getPheduyet() {
		return pheduyet;
	}

	public void setPheduyet(String pheduyet) {
		this.pheduyet = pheduyet;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public String getIdLoaicongvan() {
		return idLoaicongvan;
	}

	public void setIdLoaicongvan(String idLoaicongvan) {
		this.idLoaicongvan = idLoaicongvan;
	}

	public String getNumberDispatch() {
		return numberDispatch;
	}

	public void setNumberDispatch(String numberDispatch) {
		this.numberDispatch = numberDispatch;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNguoiXem() {
		return nguoiXem;
	}

	public void setNguoiXem(String nguoiXem) {
		this.nguoiXem = nguoiXem;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Dispatch> getData() {
		return data;
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	public String getLoaicongvan() {
		return loaicongvan;
	}

	public void setLoaicongvan(String loaicongvan) {
		this.loaicongvan = loaicongvan;
	}

	public String getCoQuanBanHanh() {
		return coQuanBanHanh;
	}

	public void setCoQuanBanHanh(String coQuanBanHanh) {
		this.coQuanBanHanh = coQuanBanHanh;
	}
	
	public String getNguoi_phe_duyet() {
		return nguoi_phe_duyet;
	}

	public void setNguoi_phe_duyet(String nguoi_phe_duyet) {
		this.nguoi_phe_duyet = nguoi_phe_duyet;
	}

	public void changeStatusDispatch(String url, String id_dispatch,
			String status, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();

		RequestParams params = new RequestParams();
		params.add("id_dispatch", id_dispatch);
		params.add("status", status);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				onLoadListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						int success = object.getInt("success");
						if (success == 1) {
							onLoadListener.onSuccess();
						} else {
							onLoadListener.onFalse();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				super.onSuccess(statusCode, headers, response);
			}

		});
	}

	// ToanNM
	public ArrayList<Dispatch> getData(final Context context, String url,
			OnLoadListener OnLoadListener, final int type) {

		DispatchType dispatchType = new DispatchType();
		dataDispatchType = dispatchType.getData(context.getResources()
				.getString(R.string.api_get_dispatch_type));

		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<Dispatch>();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("username", Utils.getString(context, "name"));
		params.add("userId", Utils.getString(context, "user_id"));

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				 Log.d("LuanDT", "DISPATCH =>>> json: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							long id = row.getInt("id");
							String numberDispatch = row.getString("so_hieu");
							String description = row.getString("trich_yeu");
							String content = row.getString("content");
							String date = row.getString("ngay_den");
							String status = row.getString("status");
							String coQuanBanHanh = row
									.getString("co_quan_ban_hanh");
							String handler = row.getString("handler");
							String idloaicv = row.getString("id_loai_cong_van");
							String nguoithaydoitrangthai = row
									.getString("nguoi_thay_doi_trang_thai");
							String pheduyet = row.getString("phe_duyet");
							String nguoi_phe_duyet = row.getString("nguoi_phe_duyet");
							String nguoiXem = row.getString("nguoi_xem");
							//
							String da_xu_ly = row.getString("da_xu_ly");
							Log.d("ToanNM", "da_xu_ly =>> " + da_xu_ly);

							content = content.replace(" ", "%20");

							if (!dataDispatchType.isEmpty()) {
								for (int j = 0; j < dataDispatchType.size(); j++) {
									DispatchType type = dataDispatchType.get(j);
									if (idloaicv.equals(type.getId())) {
										data.add(new Dispatch(id,
												numberDispatch, description,
												content, date, handler, status,
												coQuanBanHanh, type.getTitle(), idloaicv,
												nguoithaydoitrangthai, pheduyet, nguoi_phe_duyet, nguoiXem, da_xu_ly));
										// Log.d("LuanDT",
										// "----->>>loai cong van: "
										// + type.getTitle());
									}
								}
							} else {
								data.add(new Dispatch(id, numberDispatch,
										description, content, date, handler,
										status, coQuanBanHanh, "", idloaicv,
										nguoithaydoitrangthai, pheduyet, nguoi_phe_duyet, nguoiXem, da_xu_ly));
							}

							addToDB(context, type, id, numberDispatch, content,
									date, status, handler, "");

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				onLoadListener.onSuccess();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				onLoadListener.onFalse();
			}
		});
		return data;
	}

	void addToDB(Context context, int type, long id, String numberDispatch,
			String content, String date, String status, String handler, String receive) {
		// // add to db
		String username = Utils.getString(context, SessionManager.KEY_NAME);
		db = NotificationDBController.getInstance(context);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DISPATCH_COL + " = " + id + " and "
				+ NotificationDBController.D_TYPE_COL + " = " + 1 + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username
				+ "\"";
		cursor = db.rawQuery(sql, null);
		if (type == 1) {
			if (cursor != null && cursor.getCount() > 0) {
				// querryFromDB(context, Utils.getString(context, "name"));
			} else {
				ContentValues values = new ContentValues();

				values.put(NotificationDBController.USERNAME_COL, username);
				values.put(NotificationDBController.DISPATCH_COL, id);
				values.put(NotificationDBController.D_TYPE_COL, type);
				values.put(NotificationDBController.D_DESCRIPTION_COL,
						description);
				values.put(NotificationDBController.D_CONTENT_COL, content);
				values.put(NotificationDBController.D_DATE_COL, date);
				values.put(NotificationDBController.D_STATUS_COL, status);
				values.put(NotificationDBController.D_HANDLER_COL, handler);
				values.put(NotificationDBController.D_NUMBER_DISPATCH_COL,
						numberDispatch);
				values.put(NotificationDBController.DSTATE_COL,
						NotificationDBController.NOTIFICATION_STATE_NEW);
				values.put(NotificationDBController.D_RECEIVED_COL, receive);
				long rowInserted = db.insert(
						NotificationDBController.DISPATCH_TABLE_NAME, null,
						values);
			}
		}
	}

	void querryFromDB(Context context, String username) {
		db = NotificationDBController.getInstance(context);
		data = new ArrayList<Dispatch>();
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DSTATE_COL + " = \"new\"" + " and "
				+ NotificationDBController.D_TYPE_COL + " = " + 1 + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username
				+ "\"";
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int did = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(NotificationDBController.DISPATCH_COL));
				// String state = cursor
				// .getString(cursor
				// .getColumnIndexOrThrow(NotificationDBController.DSTATE_COL));
				String numberDispatch = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_NUMBER_DISPATCH_COL));
				String description = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_DESCRIPTION_COL));
				String content = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_CONTENT_COL));
				String date = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_DATE_COL));
				String status = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_STATUS_COL));
				String handler = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.D_HANDLER_COL));

				data.add(new Dispatch(did, numberDispatch, description,
						content, date, handler, status));
			} while (cursor.moveToNext());
		}
	}

	// end of ToanNM
	public ArrayList<Dispatch> search(String k, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		this.onLoadListener.onStart();
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {
					if (dispatch.getNumberDispatch().contains(k)) {
						result.add(dispatch);
					}
				}
			}
		}
		this.onLoadListener.onSuccess();
		return result;
	}

	public ArrayList<Dispatch> search(String k, ArrayList<Dispatch> data) {
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		String vReplace = AccentRemover.getInstance(context).removeAccent(k);
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {

					String iReplace = AccentRemover.getInstance(context)
							.removeAccent(dispatch.getNumberDispatch());
					String replace = AccentRemover.getInstance(context)
							.removeAccent(dispatch.getDescription());
					String replaceCoQuanBanHanh = AccentRemover.getInstance(
							context).removeAccent(dispatch.getCoQuanBanHanh());
					String replaceLoaiCongVan = AccentRemover.getInstance(
							context).removeAccent(dispatch.getLoaicongvan());
					if (iReplace.toLowerCase().contains(vReplace.toLowerCase())
							|| replace.toLowerCase().contains(
									vReplace.toLowerCase())
							|| replaceCoQuanBanHanh.toLowerCase().contains(
									vReplace.toLowerCase())
							|| replaceLoaiCongVan.toLowerCase().contains(
									vReplace.toLowerCase())) {
						result.add(dispatch);
					}
				}
			}
		}
		return result;
	}

	public ArrayList<Dispatch> filterDispatch(String type,
			ArrayList<Dispatch> data) {
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		Log.d("LuanDT", "typeId: " + type);
		if (!data.isEmpty()) {
			for (Dispatch dispatch : data) {
				if (dispatch.getIdLoaicongvan().equals(type)) {
					result.add(dispatch);
				}else if(type.equals("-1")) {
					return data;
				}
			}

		}
		return result;
	}

	public void guiXuLy(String url, String cvId, String nguoiXuLy,
			OnRequestListener onRequestListener) {
		Dispatch.this.onRequestListener = onRequestListener;
		Dispatch.this.onRequestListener.onStart();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("id_cv", cvId);
		params.add("nguoi_xu_ly", nguoiXuLy);

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject json = new JSONObject(jsonRead);
						int error = json.getInt("error");
						if (error == 1) {
							Dispatch.this.onRequestListener.onFalse(json
									.getString("error_msg"));
						} else {
							Dispatch.this.onRequestListener.onSuccess();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Dispatch.this.onRequestListener.onFalse();
			}
		});
	}

	public void approvalDispatch(String url, String userId, String cvId,
			String noiDung, String nguoiXuLy,
			OnRequestListener onRequestListener) {
		Dispatch.this.onRequestListener = onRequestListener;
		Dispatch.this.onRequestListener.onStart();
		RequestParams params = new RequestParams();
		params.add("id", userId);
		params.add("id_cv", cvId);
		params.add("noi_dung", noiDung);
		params.add("nguoi_xu_ly", nguoiXuLy);

		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Dispatch.this.onRequestListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String json = response.toString();

				if (!json.isEmpty()) {
					try {
						JSONObject object = new JSONObject(json);
						int success = object.getInt("success");
						if (success == 1) {
							Dispatch.this.onRequestListener.onSuccess();
						} else {
							Dispatch.this.onRequestListener
									.onFalse(context.getResources().getString(
											R.string.error_l));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

		});
	}

	// /////////////////
	public void convertDispatch(
			String url, 
			String userId, 
			String tenCongViec,
			String moTa,
			String tuNgay, 
			String denNgay, 
			String idNguoiXuLy, 
			String nguoiXuLy,
			String idNguoiXem,
			String nguoiXem, 
			String phongBan, 
			String idDuAn,
			String uuTien,  
			String file, 
			OnRequestListener onRequestListener) {
		Dispatch.this.onRequestListener = onRequestListener;
		Dispatch.this.onRequestListener.onStart();
		RequestParams params = new RequestParams();
		params.add("id", userId);
		params.add("ten_cv", tenCongViec);
		params.add("mo_ta", moTa);
		params.add("tu_ngay", tuNgay);
		params.add("den_ngay", denNgay);
		params.add("id_nguoi_xu_ly", idNguoiXuLy);
		params.add("nguoi_xu_ly", nguoiXuLy);
		params.add("id_nguoi_xem", idNguoiXem);
		params.add("nguoi_xem", nguoiXem);
		params.add("phong_ban", phongBan);
		params.add("id_du_an", idDuAn);
		params.add("uu_tien", uuTien);
		params.add("file", file);

		Log.d("NgaDV", "convertDispatch: " + params);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Dispatch.this.onRequestListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String json = response.toString();
				Log.d("LuanDT", "json: " + json);
				if (!json.isEmpty()) {
					try {
						JSONObject object = new JSONObject(json);
						int success = object.getInt("success");
						if (success == 1) {
							Dispatch.this.onRequestListener.onSuccess();
						} else {
							Dispatch.this.onRequestListener
									.onFalse(context.getResources().getString(
											R.string.error_l));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

		});
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	public interface OnRequestListener {
		void onStart();

		void onFalse(String stFalse);

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
	private OnRequestListener onRequestListener;
}