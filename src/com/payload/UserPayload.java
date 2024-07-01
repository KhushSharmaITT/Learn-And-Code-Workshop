package com.payload;

import com.google.gson.annotations.Expose;
import com.utility.user.UserWrapper;

public class UserPayload {
    @Expose
	private UserWrapper userWrapper;

	public void setUserWrapperDetails(UserWrapper userWrapper) {
		this.userWrapper = userWrapper;
	}

	public UserWrapper getUserWrapperDetails() {
		return this.userWrapper; 
	}
}
