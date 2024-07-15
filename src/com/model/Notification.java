package com.model;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

public class Notification {
	
	@Expose
	private int id;
	@Expose
	private int menuId;
	@Expose
	private String message;
	@Expose
    private Timestamp createdDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
}
