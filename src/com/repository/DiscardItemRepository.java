package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.model.DiscardItem;
import com.model.Menu;

public class DiscardItemRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T itemToDiscard : entitiesToSave) {
		 DiscardItem discardItem = (DiscardItem)itemToDiscard;
		 StringBuilder sql = new StringBuilder("INSERT INTO DiscardItemTable (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (discardItem.getMenuId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("MenuId");
	            values.append("?");
	            first = false;
	        }
	        if (discardItem.getItemName() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("ItemName");
	            values.append("?");
	            first = false;
	        }
	        if (discardItem.getAverageRating() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("AverageRating");
	            values.append("?");
	            first = false;
	        }
	        if (discardItem.getChefQuestions() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("ChefQuestions");
	            values.append("?");
	        }

	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        System.out.println(sql.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
	        final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
	        int paramIndex = 1;
	        if (discardItem.getMenuId() > 0) statement.setInt(paramIndex++, discardItem.getMenuId());
	        if (discardItem.getItemName() != null && !discardItem.getItemName().isEmpty()) statement.setString(paramIndex++, discardItem.getItemName());
	        if (discardItem.getAverageRating() > 0) statement.setDouble(paramIndex++, discardItem.getAverageRating());
	        if (discardItem.getChefQuestions() != null && !discardItem.getChefQuestions().isEmpty()) statement.setString(paramIndex++, discardItem.getChefQuestions());
	        System.out.println(statement.toString());
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
		return null;
	}

	@Override
	public int delete(List<T> entitiesToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(List<T> entitiesToUpdate) throws SQLException {
		int rowUpdated = 0;
		for(T updatedItem : entitiesToUpdate) {
			DiscardItem updatedDiscardItem = (DiscardItem)updatedItem;
			StringBuilder updateQuery = new StringBuilder("UPDATE DiscardItemTable SET ");
			boolean first = true;
			if (updatedDiscardItem.getChefQuestions() != null && !updatedDiscardItem.getChefQuestions().isEmpty()) {
				updateQuery.append("ChefQuestions = ?");
	            first = false;
	        }
//	        if (updatedMenu.getPrice() != 0.0f) {
//	            if (!first) updateQuery.append(", "); 
//	            updateQuery.append("Price = ?");
//	            first = false;
//	        }
//	        if (updatedMenu.getAvailabilityStatus() != null && !updatedMenu.getAvailabilityStatus().isEmpty()) {
//	            if (!first) updateQuery.append(", ");
//	            updateQuery.append("AvailabilityStatus = ?");
//	            first = false;
//	        }
//	        if (updatedMenu.getMealType() != null && !updatedMenu.getMealType().isEmpty()) {
//	            if (!first) updateQuery.append(", ");
//	            updateQuery.append("MealType = ?");
//	        }
//	        if (updatedMenu.getScore() != 0.0f ){
//	            if (!first) updateQuery.append(", ");
//	            updateQuery.append("Score = ?");
//	        }
	        updateQuery.append(" WHERE MenuId = ?");

	        System.out.println(updateQuery.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
	        final PreparedStatement statement = databaseConnection.prepareStatement(updateQuery.toString());
	        int paramIndex = 1;
	        if (updatedDiscardItem.getChefQuestions()!= null && !updatedDiscardItem.getChefQuestions().isEmpty()) statement.setString(paramIndex++, updatedDiscardItem.getChefQuestions());
            if (updatedDiscardItem.getMenuId() > 0) statement.setInt(paramIndex++, updatedDiscardItem.getMenuId());
            System.out.println(statement.toString());
	        rowUpdated += databaseHelper.write(statement);
		}
		return rowUpdated;
	}

}
