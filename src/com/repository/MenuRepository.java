package com.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Menu;

public class MenuRepository<T> implements Repository<T> {

	@Override
	public int save(T entity) throws SQLException {
		int rowSaved;
		Menu newItem = (Menu)entity;
		 StringBuilder sql = new StringBuilder("INSERT INTO Menu (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (newItem.getName() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Name");
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
	        
	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        System.out.println(sql.toString());
		//String insertQuery = "Insert into Menu(Price,AvailabilityStatus,MealType,Score,Name) VALUES(?,?,?,?,?)";
		//StringBuilder updateQuery = new StringBuilder("UPDATE menu SET ");
		rowSaved = databaseHelper.write(sql.toString(),newItem);
		// TODO Auto-generated method stub
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
             menu.setScore(cursor.getFloat("Score"));
             menu.setName(cursor.getString("Name"));
             menus.add((T) menu);
         }
		return menus;
	}

	@Override
	public int delete(T entity) throws SQLException {
		int rowDeleted;
		Menu deleteItem = (Menu)entity;
		String sql = "DELETE FROM Menu WHERE MenuId = ?";
		rowDeleted = databaseHelper.delete(sql, String.valueOf(deleteItem.getMenuId()));
		return rowDeleted;
	}
	@Override
	public int update(T entity) throws SQLException {
		int rowSaved;
		Menu updatedMenu = (Menu)entity;
		StringBuilder updateQuery = new StringBuilder("UPDATE menu SET ");
		boolean first = true;
		if (updatedMenu.getName() != null && !updatedMenu.getName().isEmpty()) {
			updateQuery.append("Name = ?");
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
        updateQuery.append(" WHERE MenuId = ?");
        
        System.out.println(updateQuery.toString());
        rowSaved = databaseHelper.write(updateQuery.toString(),updatedMenu);
		return rowSaved;
		
	}

}
