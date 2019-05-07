package edu.lehigh.lib.wayfinder.models;

import java.sql.Timestamp;
import java.util.List;

public class ProcessData {
	
	private int id;
	private int accountId;
	private int processId;
	private List<String> keys;
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
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	
	

	
	

}
