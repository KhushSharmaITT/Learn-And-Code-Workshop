package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.Notification;

public class NotificationPayload {
	@Expose
	private List<Notification> notificationWrappers = new ArrayList<>();
	
	public void setNotificationWrapperDetails(List<Notification> notificationWrapperList) {
		this.notificationWrappers.addAll(notificationWrapperList);
	}

	public List<Notification> getMenuWrapperDetails() { 
		return this.notificationWrappers;
	}
}
