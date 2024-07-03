package com.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Notification;
import com.repository.NotificationRepository;

public class NotificationService {
	private NotificationRepository<Notification> repository;
	public NotificationService() {
		repository = new NotificationRepository<>();
	}

	public String viewNotifications() throws SQLException {
		System.out.println("In view notification");
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
}
