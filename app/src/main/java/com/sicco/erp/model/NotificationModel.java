package com.sicco.erp.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	int notify_type;
	String soHieuCongVan;
	String trichYeu;
	String dinhKem;
	String ngayDenSicco;
	String trangThai;
	String state;

	public NotificationModel(long id, int notify_type, String soHieuCongVan,
			String trichYeu, String dinhKem, String ngayDenSicco,
			String trangThai) {
		this.id = id;
		this.notify_type = notify_type;
		this.soHieuCongVan = soHieuCongVan;
		this.trichYeu = trichYeu;
		this.dinhKem = dinhKem;
		this.ngayDenSicco = ngayDenSicco;
		this.trangThai = trangThai;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(int notify_type) {
		this.notify_type = notify_type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSoHieuCongVan() {
		return soHieuCongVan;
	}

	public void setSoHieuCongVan(String soHieuCongVan) {
		this.soHieuCongVan = soHieuCongVan;
	}

	public String getTrichYeu() {
		return trichYeu;
	}

	public void setTrichYeu(String trichYeu) {
		this.trichYeu = trichYeu;
	}

	public String getDinhKem() {
		return dinhKem;
	}

	public void setDinhKem(String dinhKem) {
		this.dinhKem = dinhKem;
	}

	public String getNgayDenSicco() {
		return ngayDenSicco;
	}

	public void setNgayDenSicco(String ngayDenSicco) {
		this.ngayDenSicco = ngayDenSicco;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

}
