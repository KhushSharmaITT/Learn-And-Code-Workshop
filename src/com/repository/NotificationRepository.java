package com.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Menu;
import com.model.Notification;

public class NotificationRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T find(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findRecords(String query) throws SQLException {
		List<T> notifications = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
		 while (cursor.next()) {
             Notification notification = new Notification();
             notification.setMenuId(cursor.getInt("MenuId"));
             notification.setMessage(cursor.getString("Message"));
             notifications.add((T) notification);
         }
		return notifications;
	}

	@Override
	public int delete(List<T> entitiesToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(List<T> entitiesToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
