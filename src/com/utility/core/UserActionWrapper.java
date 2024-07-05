package com.utility.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.utility.user.UserWrapper;

public class UserActionWrapper<T> {

	@Expose
	private List<T> actionData = new ArrayList<T>();
	@Expose
	private UserWrapper userWrapper;
	@Expose
	private String actionToPerform;
	
	public List<T> getActionData() {
		return actionData;
	}
	public void setActionData(List<T> actionData) {
		this.actionData = actionData;
	}
	public UserWrapper getUserWrapper() {
		return userWrapper;
	}
	public void setUserWrapper(UserWrapper userWrapper) {
		this.userWrapper = userWrapper;
	}
	public String getActionToPerform() {
		return actionToPerform;
	}
	public void setActionToPerform(String actionToPerform) {
		this.actionToPerform = actionToPerform;
	}
}
