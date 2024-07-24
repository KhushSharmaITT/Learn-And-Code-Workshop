package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.UserLogger;

public class UserLoggerRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T userLog : entitiesToSave) {
		UserLogger newUserLog = (UserLogger)userLog;
		 StringBuilder sql = new StringBuilder("INSERT INTO user_logger (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (newUserLog.getUserId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("UserId");
	            values.append("?");
	            first = false;
	        }
	        if (newUserLog.getUserAction() != null ) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("UserAction");
	            values.append("?");
	            first = false;
	        }
	        if (newUserLog.getDateCreated() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Date_Created");
	            values.append("?");
	            first = false;
	        }	        
	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        Connection databaseConnection =  databaseHelper.getConnection();
	        final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
	        int paramIndex = 1;
	        if (newUserLog.getUserId() > 0) statement.setInt(paramIndex++, newUserLog.getUserId());
	        if (newUserLog.getUserAction() != null && !newUserLog.getUserAction().isEmpty()) statement.setString(paramIndex++, newUserLog.getUserAction());
	        if (newUserLog.getDateCreated()!= null) statement.setTimestamp(paramIndex++, newUserLog.getDateCreated());
	        System.out.println(sql.toString());
	        rowSaved += databaseHelper.write(statement);
		}
		return rowSaved;
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
		List<T> retreivedResponses = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
        while (cursor.next()&& cursor != null) {
        	UserLogger userLog = new UserLogger();
        	userLog.setId(cursor.getInt("Id"));
        	userLog.setUserId(cursor.getInt("UserId"));
        	userLog.setUserAction(cursor.getString("UserAction"));
        	userLog.setDateCreated(cursor.getTimestamp("Date_Created"));
        	retreivedResponses.add((T)userLog);
        }
		return retreivedResponses;
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
