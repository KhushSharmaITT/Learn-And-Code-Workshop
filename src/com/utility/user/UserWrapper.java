package com.utility.user;

import com.google.gson.annotations.Expose;

public class UserWrapper {
	@Expose
	private int id;
	@Expose
	private String emailId;
	@Expose
	private String password;
	@Expose
	private String role; 
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return this.role;
	}
}
