package com.sicco.task.model;

public class Priority {
	private String priority;
	private String keyPriority;
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getKeyPriority() {
		return keyPriority;
	}
	public void setKeyPriority(String keyPriority) {
		this.keyPriority = keyPriority;
	}
	public Priority(String priority, String keyPriority) {
		super();
		this.priority = priority;
		this.keyPriority = keyPriority;
	}
	public Priority() {
		super();
	}
	
	
}
