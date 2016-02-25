package com.sicco.task.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.util.AccentRemover;
import com.sicco.erp.util.Utils;

public class Task implements Serializable {
	private long id;
	private String ten_cong_viec;
	private String nguoi_thuc_hien;
	private String nguoi_xem;
	private String du_an;
	private String tien_do;
	private String ngay_bat_dau;
	private String ngay_ket_thuc;
	private String nguoi_giao;
	private String dinh_kem;
	private String phong_ban;
	private String mo_ta;
	private String code;
	private String id_du_an;
	private String id_phong_ban;
	private String trang_thai;
	private boolean co_binh_luan;
	private boolean da_qua_han;
	private String muc_uu_tien;
	private String ngay_httt;
	private String daxuly;
	private Context context;

	private ArrayList<Task> data;

	public Task(Context context) {
		this.context = context;
	}
	
	public Task(long id, String ten_cong_viec, String nguoi_thuc_hien,
			String nguoi_xem, String mo_ta) {
		super();
		this.id = id;
		this.ten_cong_viec = ten_cong_viec;
		this.nguoi_thuc_hien = nguoi_thuc_hien;
		this.nguoi_xem = nguoi_xem;
		this.mo_ta = mo_ta;
	}

	public Task(long id, String ten_cong_viec, String nguoi_thuc_hien,
			String nguoi_xem, String mo_ta, String trang_thai) {
		super();
		this.id = id;
		this.ten_cong_viec = ten_cong_viec;
		this.nguoi_thuc_hien = nguoi_thuc_hien;
		this.nguoi_xem = nguoi_xem;
		this.mo_ta = mo_ta;
		this.trang_thai = trang_thai;
	}

	public Task(long id, String ten_cong_viec, String nguoi_thuc_hien,
			String nguoi_xem, String du_an, String tien_do,
			String ngay_bat_dau, String ngay_ket_thuc, String nguoi_giao,
			String dinh_kem, String phong_ban, String mo_ta, String code,
			String id_du_an, String id_phong_ban, String trang_thai) {
		super();
		this.id = id;
		this.ten_cong_viec = ten_cong_viec;
		this.nguoi_thuc_hien = nguoi_thuc_hien;
		this.nguoi_xem = nguoi_xem;
		this.du_an = du_an;
		this.tien_do = tien_do;
		this.ngay_bat_dau = ngay_bat_dau;
		this.ngay_ket_thuc = ngay_ket_thuc;
		this.nguoi_giao = nguoi_giao;
		this.dinh_kem = dinh_kem;
		this.phong_ban = phong_ban;
		this.mo_ta = mo_ta;
		this.code = code;
		this.id_du_an = id_du_an;
		this.id_phong_ban = id_phong_ban;
		this.trang_thai = trang_thai;
	}

	public Task(long id, String ten_cong_viec, String nguoi_thuc_hien,
			String nguoi_xem, String du_an, String tien_do,
			String ngay_bat_dau, String ngay_ket_thuc, String nguoi_giao,
			String dinh_kem, String phong_ban, String mo_ta, String code,
			String id_du_an, String id_phong_ban, String trang_thai,
			boolean co_binh_luan, boolean da_qua_han, String muc_uu_tien,
			String ngay_httt, String daxuly) {
		super();
		this.id = id;
		this.ten_cong_viec = ten_cong_viec;
		this.nguoi_thuc_hien = nguoi_thuc_hien;
		this.nguoi_xem = nguoi_xem;
		this.du_an = du_an;
		this.tien_do = tien_do;
		this.ngay_bat_dau = ngay_bat_dau;
		this.ngay_ket_thuc = ngay_ket_thuc;
		this.nguoi_giao = nguoi_giao;
		this.dinh_kem = dinh_kem;
		this.phong_ban = phong_ban;
		this.mo_ta = mo_ta;
		this.code = code;
		this.id_du_an = id_du_an;
		this.id_phong_ban = id_phong_ban;
		this.trang_thai = trang_thai;
		this.co_binh_luan = co_binh_luan;
		this.da_qua_han = da_qua_han;
		this.muc_uu_tien = muc_uu_tien;
		this.ngay_httt = ngay_httt;
		this.daxuly = daxuly;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTen_cong_viec() {
		return ten_cong_viec;
	}

	public void setTen_cong_viec(String ten_cong_viec) {
		this.ten_cong_viec = ten_cong_viec;
	}

	public String getNguoi_thuc_hien() {
		return nguoi_thuc_hien;
	}

	public void setNguoi_thuc_hien(String nguoi_thuc_hien) {
		this.nguoi_thuc_hien = nguoi_thuc_hien;
	}

	public String getNguoi_xem() {
		return nguoi_xem;
	}

	public void setNguoi_xem(String nguoi_xem) {
		this.nguoi_xem = nguoi_xem;
	}

	public String getDu_an() {
		return du_an;
	}

	public void setDu_an(String du_an) {
		this.du_an = du_an;
	}

	public String getTien_do() {
		return tien_do;
	}

	public void setTien_do(String tien_do) {
		this.tien_do = tien_do;
	}

	public String getNgay_bat_dau() {
		return ngay_bat_dau;
	}

	public void setNgay_bat_dau(String ngay_bat_dau) {
		this.ngay_bat_dau = ngay_bat_dau;
	}

	public String getNgay_ket_thuc() {
		return ngay_ket_thuc;
	}

	public void setNgay_ket_thuc(String ngay_ket_thuc) {
		this.ngay_ket_thuc = ngay_ket_thuc;
	}

	public String getNguoi_giao() {
		return nguoi_giao;
	}

	public void setNguoi_giao(String nguoi_giao) {
		this.nguoi_giao = nguoi_giao;
	}

	public String getDinh_kem() {
		return dinh_kem;
	}

	public void setDinh_kem(String dinh_kem) {
		this.dinh_kem = dinh_kem;
	}

	public String getPhong_ban() {
		return phong_ban;
	}

	public void setPhong_ban(String phong_ban) {
		this.phong_ban = phong_ban;
	}

	public String getMo_ta() {
		return mo_ta;
	}

	public void setMo_ta(String mo_ta) {
		this.mo_ta = mo_ta;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Task> getData() {
		return data;
	}

	public void setData(ArrayList<Task> data) {
		this.data = data;
	}

	public String getId_du_an() {
		return id_du_an;
	}

	public void setId_du_an(String id_du_an) {
		this.id_du_an = id_du_an;
	}

	public String getId_phong_ban() {
		return id_phong_ban;
	}

	public void setId_phong_ban(String id_phong_ban) {
		this.id_phong_ban = id_phong_ban;
	}

	public String getTrang_thai() {
		return trang_thai;
	}

	public void setTrang_thai(String trang_thai) {
		this.trang_thai = trang_thai;
	}

	public boolean isCo_binh_luan() {
		return co_binh_luan;
	}

	public void setCo_binh_luan(boolean co_binh_luan) {
		this.co_binh_luan = co_binh_luan;
	}

	public boolean isDa_qua_han() {
		return da_qua_han;
	}

	public void setDa_qua_han(boolean da_qua_han) {
		this.da_qua_han = da_qua_han;
	}

	public String getMuc_uu_tien() {
		return muc_uu_tien;
	}

	public void setMuc_uu_tien(String muc_uu_tien) {
		this.muc_uu_tien = muc_uu_tien;
	}

	public String getNgay_httt() {
		return ngay_httt;
	}

	public void setNgay_httt(String ngay_httt) {
		this.ngay_httt = ngay_httt;
	}

	public String getDaxuly() {
		return daxuly;
	}

	public void setDaxuly(String daxuly) {
		this.daxuly = daxuly;
	}

	// get data
	public ArrayList<Task> getData(final Context context, String url,
			OnLoadListener OnLoadListener) {

		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<Task>();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("user_id", Utils.getString(context, "user_id"));
		params.add("username", Utils.getString(context, "name"));

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							String id = row.getString("id");
							String ten_cong_viec = row
									.getString("ten_cong_viec");
							String nguoi_thuc_hien = row
									.getString("nguoi_thuc_hien");
							String nguoi_xem = row.getString("nguoi_xem");
							String du_an = row.getString("du_an");
							String tien_do = row.getString("tien_do");
							String ngay_bat_dau = row.getString("ngay_bat_dau");
							String ngay_ket_thuc = row
									.getString("ngay_ket_thuc");
							String nguoi_giao = row.getString("nguoi_giao");
							String dinh_kem = row.getString("dinh_kem");
							String phong_ban = row.getString("phong_ban");
							String mo_ta = row.getString("mo_ta");
							String code = row.getString("code");
							String id_du_an = row.getString("id_du_an");
							String id_phong_ban = row.getString("id_phong_ban");
							String trang_thai = row.getString("trang_thai");
							String cbl = row.getString("co_binh_luan");
							boolean co_binh_luan = false;
							if (cbl.equals("1"))
								co_binh_luan = true;
							String dqh = row.getString("da_qua_han");
							boolean da_qua_han = false;
							if (dqh.equals("1"))
								da_qua_han = true;
							String muc_uu_tien = row.getString("muc_uu_tien");
							String ngay_httt = row.getString("ngay_httt");
							String daxuly = row.getString("daxuly");

							dinh_kem = dinh_kem.replace(" ", "%20");

							data.add(new Task(Long.parseLong(id),
									ten_cong_viec, nguoi_thuc_hien, nguoi_xem,
									du_an, tien_do, ngay_bat_dau,
									ngay_ket_thuc, nguoi_giao, dinh_kem,
									phong_ban, mo_ta, code, id_du_an,
									id_phong_ban, trang_thai, co_binh_luan,
									da_qua_han, muc_uu_tien, ngay_httt, daxuly));
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

	// get task by id
	public static Task getTaskById(final Context context, String url, String id) {

		final Task task = new Task(context);
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("task_id", id);

		Log.d("LuanDT", "params: " + params);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();
				Log.d("LuanDT", "jsonRead: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							String id = row.getString("id");
							String ten_cong_viec = row
									.getString("ten_cong_viec");
							String nguoi_thuc_hien = row
									.getString("nguoi_thuc_hien");
							String nguoi_xem = row.getString("nguoi_xem");
							String du_an = row.getString("du_an");
							String tien_do = row.getString("tien_do");
							String ngay_bat_dau = row.getString("ngay_bat_dau");
							String ngay_ket_thuc = row
									.getString("ngay_ket_thuc");
							String nguoi_giao = row.getString("nguoi_giao");
							String dinh_kem = row.getString("dinh_kem");
							String phong_ban = row.getString("phong_ban");
							String mo_ta = row.getString("mo_ta");
							String code = row.getString("code");
							String id_du_an = row.getString("id_du_an");
							String id_phong_ban = row.getString("id_phong_ban");
							String trang_thai = row.getString("trang_thai");
							String cbl = row.getString("co_binh_luan");
							boolean co_binh_luan = false;
							if (cbl.equals("1"))
								co_binh_luan = true;
							String dqh = row.getString("da_qua_han");
							boolean da_qua_han = false;
							if (dqh.equals("1"))
								da_qua_han = true;
							String muc_uu_tien = row.getString("muc_uu_tien");
							String ngay_httt = row.getString("ngay_httt");

							dinh_kem = dinh_kem.replace(" ", "%20");

							task.setId(Long.parseLong(id));
							task.setTen_cong_viec(ten_cong_viec);
							task.setNguoi_thuc_hien(nguoi_thuc_hien);
							task.setNguoi_xem(nguoi_xem);
							task.setDu_an(du_an);
							task.setTien_do(tien_do);
							task.setNgay_bat_dau(ngay_bat_dau);
							task.setNgay_ket_thuc(ngay_ket_thuc);
							task.setNguoi_giao(nguoi_giao);
							task.setDinh_kem(dinh_kem);
							task.setPhong_ban(phong_ban);
							task.setMo_ta(mo_ta);
							task.setCode(code);
							task.setId_du_an(id_du_an);
							task.setId_phong_ban(id_phong_ban);
							task.setTrang_thai(trang_thai);
							task.setCo_binh_luan(co_binh_luan);
							task.setDa_qua_han(da_qua_han);
							task.setMuc_uu_tien(muc_uu_tien);
							task.setNgay_httt(ngay_httt);
							
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
			}
		});
		return task;
	}
	
	// get task by id interface
		public Task getTaskById(final Context context, String url, String id, OnLoadListener OnLoadListener) {

			this.onLoadListener = OnLoadListener;
			onLoadListener.onStart();
			final Task task = new Task(context);
			
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.add("task_id", id);

			Log.d("LuanDT", "params: " + params);
			client.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					String jsonRead = response.toString();
					Log.d("LuanDT", "jsonRead: " + jsonRead);
					if (!jsonRead.isEmpty()) {
						try {
							JSONObject object = new JSONObject(jsonRead);
							JSONArray rows = object.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								String id = row.getString("id");
								String ten_cong_viec = row
										.getString("ten_cong_viec");
								String nguoi_thuc_hien = row
										.getString("nguoi_thuc_hien");
								String nguoi_xem = row.getString("nguoi_xem");
								String du_an = row.getString("du_an");
								String tien_do = row.getString("tien_do");
								String ngay_bat_dau = row.getString("ngay_bat_dau");
								String ngay_ket_thuc = row
										.getString("ngay_ket_thuc");
								String nguoi_giao = row.getString("nguoi_giao");
								String dinh_kem = row.getString("dinh_kem");
								String phong_ban = row.getString("phong_ban");
								String mo_ta = row.getString("mo_ta");
								String code = row.getString("code");
								String id_du_an = row.getString("id_du_an");
								String id_phong_ban = row.getString("id_phong_ban");
								String trang_thai = row.getString("trang_thai");
								String cbl = row.getString("co_binh_luan");
								boolean co_binh_luan = false;
								if (cbl.equals("1"))
									co_binh_luan = true;
								String dqh = row.getString("da_qua_han");
								boolean da_qua_han = false;
								if (dqh.equals("1"))
									da_qua_han = true;
								String muc_uu_tien = row.getString("muc_uu_tien");
								String ngay_httt = row.getString("ngay_httt");

								dinh_kem = dinh_kem.replace(" ", "%20");

								task.setId(Long.parseLong(id));
								task.setTen_cong_viec(ten_cong_viec);
								task.setNguoi_thuc_hien(nguoi_thuc_hien);
								task.setNguoi_xem(nguoi_xem);
								task.setDu_an(du_an);
								task.setTien_do(tien_do);
								task.setNgay_bat_dau(ngay_bat_dau);
								task.setNgay_ket_thuc(ngay_ket_thuc);
								task.setNguoi_giao(nguoi_giao);
								task.setDinh_kem(dinh_kem);
								task.setPhong_ban(phong_ban);
								task.setMo_ta(mo_ta);
								task.setCode(code);
								task.setId_du_an(id_du_an);
								task.setId_phong_ban(id_phong_ban);
								task.setTrang_thai(trang_thai);
								task.setCo_binh_luan(co_binh_luan);
								task.setDa_qua_han(da_qua_han);
								task.setMuc_uu_tien(muc_uu_tien);
								task.setNgay_httt(ngay_httt);
								
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
					onLoadListener.onFalse();
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
			});
			return task;
		}

	// changeProcess
	public void changeProcess(String url, String id_cv, String process,
			OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();

		RequestParams params = new RequestParams();
		params.add("id_cv", id_cv);
		params.add("rate", process);

		Log.d("LuanDT", "params: " + params);
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

				Log.d("LuanDT", "jsonRead: " + jsonRead);
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

	// search
	public ArrayList<Task> search(String k, ArrayList<Task> data) {
		ArrayList<Task> result = new ArrayList<Task>();
		String vReplace = AccentRemover.getInstance(context).removeAccent(k);
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Task task : data) {

					String iReplace = AccentRemover.getInstance(context)
							.removeAccent(task.getTen_cong_viec());

					if (iReplace.toLowerCase().contains(vReplace.toLowerCase())) {
						result.add(task);
					}
				}
			}
		}
		return result;
	}

	// cap nhat trang thai
	public void changeStatusTask(Context context, long id_cv, String status,
			OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();

		RequestParams params = new RequestParams();
		params.put("id_cv", id_cv);
		params.put("status", status);

		Log.d("LuanDT", "params: " + params);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(context.getResources()
				.getString(R.string.api_update_status), params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						onLoadListener.onFalse();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String jsonRead = response.toString();
						Log.d("LuanDT", "json: " + jsonRead);
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

	// assign Task
	public void assignTask(String url, long user_id, String name_task,
			String des_task, String date_handle, String date_finish,
			String str_id_handler, String str_name_handler,
			String str_id_viewer, String str_name_viewer, String tich_hop,
			String id_department, String id_project, String pathFile,
			String keyPriority, OnLoadListener OnLoadListener)
			throws FileNotFoundException {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		RequestParams params = new RequestParams();

		params.add("user_id", Long.toString(user_id));
		params.add("ten_cong_viec", name_task);
		params.add("mo_ta", des_task);
		params.add("ngay_bat_dau", date_handle);
		params.add("ngay_ket_thuc", date_finish);
		params.add("id_nguoi_thuc_hien", str_id_handler);
		params.add("nguoi_thuc_hien", str_name_handler);
		params.add("id_nguoi_xem", str_id_viewer);
		params.add("nguoi_xem", str_name_viewer);
		params.add("tich_hop", tich_hop);
		params.add("phong_ban_pt", id_department);
		if (pathFile != null) {
			File file = new File(pathFile);
			params.put("dinh_kem", file);
		} else {
			params.put("dinh_kem", "");
		}
		params.add("id_du_an", id_project);
		params.add("uu_tien", keyPriority);

		Log.d("NgaDV", "params: " + params);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				Log.d("NgaDV", "json: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						int success = object.getInt("success");

						if (success == 1) {
							onLoadListener.onSuccess();
						} else {
							String error_msg = object.getString("error_msg");
							onLoadListener.onFalse();
							Toast.makeText(context, error_msg,
									Toast.LENGTH_SHORT).show();
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

				onLoadListener.onFalse();

				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	// delete Task
	public void deleteTaskById(String url, long task_id,
			OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		RequestParams params = new RequestParams();
		params.put("task_id", task_id);
		Log.d("NgaDV", "" + params);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				Log.d("NgaDV", "json: " + jsonRead);
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

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {

				onLoadListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	public static long FILTER_ALL_TYPE = 0;
	public static long FILTER_CXL_TYPE = 1;
	public static long FILTER_DXLTH_TYPE = 2;
	public static long FILTER_DXLQH_TYPE = 3;
	public static long FILTER_CVHT_TYPE = 4;
	public static long FILTER_CVTD_TYPE = 5;
	public static long FILTER_CVDH_TYPE = 6;

	// filter
	public ArrayList<Task> filter(ArrayList<Task> data, long type) {
		ArrayList<Task> dataFilter = new ArrayList<Task>();
		if (data != null) {
			if (type == FILTER_ALL_TYPE) {
				dataFilter = data;
			} else if (type == FILTER_CXL_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
//					if (!task.isCo_binh_luan())
					if(task.daxuly.equals("0"))
						dataFilter.add(task);
				}
			} else if (type == FILTER_DXLTH_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
//					if (task.isCo_binh_luan() && !task.isDa_qua_han())
					if(task.daxuly.equals("1") && !task.isDa_qua_han())
						dataFilter.add(task);
				}
			} else if (type == FILTER_DXLQH_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
//					if (task.isCo_binh_luan() && task.isDa_qua_han())
					if(task.daxuly.equals("1")  && task.isDa_qua_han())
						dataFilter.add(task);
				}
			}
			// filter cong viec
			else if (type == FILTER_CVHT_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
					if (task.getTrang_thai().equals("complete"))
						dataFilter.add(task);
				}
			} else if (type == FILTER_CVTD_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
					if (task.getTrang_thai().equals("inactive"))
						dataFilter.add(task);
				}
			} else if (type == FILTER_CVDH_TYPE) {
				for (int i = 0; i < data.size(); i++) {
					Task task = data.get(i);
					Log.d("TuNT", "type: " + task.getTrang_thai());
					if (task.getTrang_thai().equals("cancel"))
						dataFilter.add(task);
				}
			}
		}
		return dataFilter;
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
