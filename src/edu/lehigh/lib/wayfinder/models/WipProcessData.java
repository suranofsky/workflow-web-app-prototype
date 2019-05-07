package edu.lehigh.lib.wayfinder.models;

import java.sql.Timestamp;

public class WipProcessData {
	
	private int id;
	private Timestamp updateDate;
	private int wipId;
	private String key;
	private String value;
	
	
	
	public int getWipId() {
		return wipId;
	}
	public void setWipId(int wipId) {
		this.wipId = wipId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
	

}
