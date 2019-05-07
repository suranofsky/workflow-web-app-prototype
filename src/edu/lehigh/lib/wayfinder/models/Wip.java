package edu.lehigh.lib.wayfinder.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wip {
	
	
	private int id;
	private int accountId;
	private int processId;
	private int currentTaskId;
	private List<WipProcessData> data;
	private Timestamp createDate;
	private Timestamp updateDate;
	
	//EXPERIMENTAL
	private Map<String,String> keyValues;
	
	


	public Map<String, String> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(Map<String, String> keyValues) {
		this.keyValues = keyValues;
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

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getCurrentTaskId() {
		return currentTaskId;
	}

	public void setCurrentTaskId(int currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public List<WipProcessData> getData() {
		return data;
	}

	public void setData(List<WipProcessData> data) {
		this.data = data;
	}
	
	

}
