package com.model;


public class UserBuilder {
	private User userModel;

	public UserBuilder() {
		this.userModel = new User();
	}


	public UserBuilder setEmail(String email) {
		this.userModel.emailId = email;
		return this;
	}

	public UserBuilder setPassword(String userPassword) {
		this.userModel.password = userPassword;
		return this;
	}

	public User build(boolean doInsert) {
		if(doInsert) {
			return this.userModel;
		}
		return null;
	}

}
