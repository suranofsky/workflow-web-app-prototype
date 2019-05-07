package edu.lehigh.lib.wayfinder.models;

import java.sql.Timestamp;

public class Log {
	
	private int id;
	private String userid;
	private Timestamp updatedDate;
	private int wipId;
	private String oldStatus; //DO WE REALLY NEED OLD STATUS?
	private String newStatus;
	private int accountId;
	private String uuid;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getWipId() {
		return wipId;
	}
	public void setWipId(int wipId) {
		this.wipId = wipId;
	}
	public String getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	

}
