package com.sicco.erp.model;

public class Status {
	private String status;
	private String sKey;
	private long key;

	public Status() {
		// TODO Auto-generated constructor stub
	}

	public Status(String status, long key) {
		super();
		this.status = status;
		this.key = key;
	}

	public Status(long key, String sKey, String status) {
		super();
		this.status = status;
		this.sKey = sKey;
		this.key = key;
	}

	public String getStatus() {
		return status;
	}

	public String getsKey() {
		return sKey;
	}

	public void setsKey(String sKey) {
		this.sKey = sKey;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

}
