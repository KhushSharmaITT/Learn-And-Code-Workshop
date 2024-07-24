package com.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.model.Notification;
import com.repository.NotificationRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class NotificationService {
	private Type type;
	private JsonWrapper<UserActionWrapper<Notification>> jsonWrapper;
	private NotificationRepository<Notification> repository;
	public NotificationService() {
		type = new TypeToken<UserActionWrapper<Notification>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		repository = new NotificationRepository<>();
	}

	public String viewNotifications() throws SQLException {
		List<Notification> notificationList = new ArrayList<>();
		String queryToFind = "SELECT Id, MenuId, Message FROM notification WHERE DATE(Date_Created) = CURDATE() OR DATE(Date_Created) = CURDATE()-1";
		notificationList = repository.findRecords(queryToFind);
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s%n", "Menu ID", "Message"));
		result.append("---------------------------------------\n");
		for (Notification notification : notificationList) {
	        result.append(String.format("%-10d %-20s%n",
	        		notification.getMenuId(),
	        		notification.getMessage()
	        ));
	    }
		System.out.println(result.toString());
		return result.toString();
	}

	public UserActionWrapper<Notification> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<Notification> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}
}
