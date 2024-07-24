package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.VotedItem;

public class VotedItemRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T votedItem : entitiesToSave) {
		 VotedItem newVotedItem = (VotedItem)votedItem;
		 StringBuilder sql = new StringBuilder("INSERT INTO VotedItem (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (newVotedItem.getMenuId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("MenuId");
	            values.append("?");
	            first = false;
	        }
	        if (newVotedItem.getUserId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("UserId");
	            values.append("?");
	            first = false;
	        }
	        sql.append(", ");
            values.append(", ");
            sql.append("Vote");
            values.append("?");
            
	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        Connection databaseConnection =  databaseHelper.getConnection();
	        final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
	        int paramIndex = 1;
	        if(newVotedItem.getMenuId() > 0) statement.setInt(paramIndex++, newVotedItem.getMenuId());
	        if(newVotedItem.getUserId() > 0) statement.setInt(paramIndex++, newVotedItem.getUserId());
	        statement.setInt(paramIndex++, 1);
	        rowSaved = databaseHelper.write(statement);
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
		// TODO Auto-generated method stub
		List<T> duplicateResponses = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
		while (cursor.next()) {
			VotedItem votedItem = new VotedItem();
			votedItem.setMenuId(cursor.getInt("MenuId"));
			votedItem.setUserId(cursor.getInt("UserId"));
			duplicateResponses.add((T)votedItem);
		}
		return duplicateResponses;
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
