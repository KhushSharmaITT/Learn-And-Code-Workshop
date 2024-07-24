package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Menu;

public class MenuRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T menuItem : entitiesToSave) {
		Menu newItem = (Menu)menuItem;
		 StringBuilder sql = new StringBuilder("INSERT INTO Menu (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (newItem.getName() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("ItemName");
	            values.append("?");
	            first = false;
	        }
	        if (newItem.getPrice() >= 0.0f) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Price");
	            values.append("?");
	            first = false;
	        }
	        if (newItem.getAvailabilityStatus() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("AvailabilityStatus");
	            values.append("?");
	            first = false;
	        }
	        if (newItem.getMealType() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("MealType");
	            values.append("?");
	        }

	        if (newItem.getPreference() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("vegetarian_preference");
	            values.append("?");
	        }
	        
	        if (newItem.getSpiceLevel() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("spice_level");
	            values.append("?");
	        }
	        
	        if (newItem.getCuisinePreference() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("cuisine_preference");
	            values.append("?");
	        }
	        
	        if (newItem.getSweetTooth() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("sweet_tooth");
	            values.append("?");
	        }
	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        System.out.println(sql.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
        	final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
        	 int paramIndex = 1;
        	 if (newItem.getName() != null && !newItem.getName().isEmpty()) statement.setString(paramIndex++, newItem.getName());
             if (newItem.getPrice() != 0.0f) statement.setDouble(paramIndex++, newItem.getPrice());
             if (newItem.getAvailabilityStatus() != null && !newItem.getAvailabilityStatus().isEmpty()) statement.setString(paramIndex++, newItem.getAvailabilityStatus());
             if (newItem.getMealType() != null && !newItem.getMealType().isEmpty()) statement.setString(paramIndex++, newItem.getMealType());
             if (newItem.getPreference() != null && !newItem.getPreference().isEmpty()) statement.setString(paramIndex++, newItem.getPreference());
             if (newItem.getSpiceLevel() != null && !newItem.getSpiceLevel().isEmpty()) statement.setString(paramIndex++, newItem.getSpiceLevel());
             if (newItem.getCuisinePreference() != null && !newItem.getCuisinePreference().isEmpty()) statement.setString(paramIndex++, newItem.getCuisinePreference());
             if (newItem.getSweetTooth() != null && !newItem.getSweetTooth().isEmpty()) statement.setString(paramIndex++, newItem.getSweetTooth());
             if (newItem.getScore() != 0.0f) statement.setDouble(paramIndex++, newItem.getScore()); 
             if (newItem.getMenuId() > 0) statement.setInt(paramIndex++, newItem.getMenuId());
             System.out.println(statement.toString());
		 rowSaved = databaseHelper.write(statement);
		}
		return rowSaved;
	}

	@Override
	public T find(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll() throws SQLException {
		List<T> menus = new ArrayList<>();
		ResultSet cursor;
		String selectQuery = "SELECT * FROM Menu";
		cursor = databaseHelper.readAll(selectQuery);
		 while (cursor.next()) {
             Menu menu = new Menu();
             menu.setMenuId(cursor.getInt("MenuId"));
             menu.setPrice(cursor.getFloat("Price"));
             menu.setAvailabilityStatus(cursor.getString("AvailabilityStatus"));
             menu.setMealType(cursor.getString("MealType"));
             menu.setScore(cursor.getDouble("Score"));
             menu.setName(cursor.getString("ItemName"));
             menu.setPreference(cursor.getString("vegetarian_preference"));
             menu.setSpiceLevel(cursor.getString("spice_level"));
             menu.setCuisinePreference(cursor.getString("cuisine_preference"));
             menu.setSweetTooth(cursor.getString("sweet_tooth"));
             menus.add((T) menu);
         }
		return menus;
	}

	@Override
	public int delete(List<T> entitiesToDelete) throws SQLException {
		int rowDeleted = 0;
		for(T deletedItem : entitiesToDelete) {
			Menu deleteItem = (Menu)deletedItem;
			String sql = "DELETE FROM Menu WHERE MenuId = ?";
			rowDeleted = databaseHelper.delete(sql, String.valueOf(deleteItem.getMenuId()));
		}
		return rowDeleted;
	}
	
    @Override
	public int update(List<T> entitiesToUpdate) throws SQLException {
		int rowUpdated = 0;
		for(T updatedItem : entitiesToUpdate) {
			Menu updatedMenu = (Menu)updatedItem;
			StringBuilder updateQuery = new StringBuilder("UPDATE menu SET ");
			boolean first = true;
			if (updatedMenu.getName() != null && !updatedMenu.getName().isEmpty()) {
				updateQuery.append("ItemName = ?");
	            first = false;
	        }
	        if (updatedMenu.getPrice() != 0.0f) {
	            if (!first) updateQuery.append(", "); 
	            updateQuery.append("Price = ?");
	            first = false;
	        }
	        if (updatedMenu.getAvailabilityStatus() != null && !updatedMenu.getAvailabilityStatus().isEmpty()) {
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("AvailabilityStatus = ?");
	            first = false;
	        }
	        if (updatedMenu.getMealType() != null && !updatedMenu.getMealType().isEmpty()) {
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("MealType = ?");
	        }
	        if (updatedMenu.getScore() != 0.0f ){
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("Score = ?");
	        }
	        updateQuery.append(" WHERE MenuId = ?");

	        System.out.println(updateQuery.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
        	final PreparedStatement statement = databaseConnection.prepareStatement(updateQuery.toString());
        	 int paramIndex = 1;
        	 if (updatedMenu.getName() != null && !updatedMenu.getName().isEmpty()) statement.setString(paramIndex++, updatedMenu.getName());
             if (updatedMenu.getPrice() != 0.0f) statement.setDouble(paramIndex++, updatedMenu.getPrice());
             if (updatedMenu.getAvailabilityStatus() != null && !updatedMenu.getAvailabilityStatus().isEmpty()) statement.setString(paramIndex++, updatedMenu.getAvailabilityStatus());
             if (updatedMenu.getMealType() != null && !updatedMenu.getMealType().isEmpty()) statement.setString(paramIndex++, updatedMenu.getMealType());
             if (updatedMenu.getScore() != 0.0f) statement.setDouble(paramIndex++, updatedMenu.getScore()); 
             if (updatedMenu.getMenuId() > 0) statement.setInt(paramIndex++, updatedMenu.getMenuId());
             System.out.println(statement.toString());
	        rowUpdated =+ databaseHelper.write(statement);
		}
		
		return rowUpdated;
	}

	@Override
	public List<T> findRecords(String query) throws SQLException {
		List<T> menus = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
		 while (cursor.next()) {
             Menu menu = new Menu();
             menu.setMenuId(cursor.getInt("MenuId"));
             menu.setPrice(cursor.getFloat("Price"));
             menu.setAvailabilityStatus(cursor.getString("AvailabilityStatus"));
             menu.setMealType(cursor.getString("MealType"));
             menu.setScore(cursor.getDouble("Score"));
             menu.setName(cursor.getString("ItemName"));
             menus.add((T) menu);
         }
		return menus;
	}
}
