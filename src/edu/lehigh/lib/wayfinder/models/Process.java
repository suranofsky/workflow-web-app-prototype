package edu.lehigh.lib.wayfinder.models;

import java.sql.Timestamp;
import java.util.List;

public class Process {
	
	private int id;
	private int accountId;
	private String title;
	private String description;
	private Timestamp createDate;
	private Timestamp updateDate;
	
	//NOT SURE I WANT THIS HERE?
	private List<WipProcessData> wipProcessData;
	
	
	public List<WipProcessData> getWipProcessData() {
		return wipProcessData;
	}
	public void setWipProcessData(List<WipProcessData> wipProcessData) {
		this.wipProcessData = wipProcessData;
	}
	
	//
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
	

}
