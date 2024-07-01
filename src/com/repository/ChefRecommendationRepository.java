package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.ChefRecommendation;
import com.model.Menu;

public class ChefRecommendationRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T recommendedItem : entitiesToSave) {
			ChefRecommendation chefRecommendedItem = (ChefRecommendation)recommendedItem;
			 StringBuilder sql = new StringBuilder("INSERT INTO chefrecommendation (");
		     StringBuilder values = new StringBuilder("VALUES (");
		     boolean first = true;
		     if (chefRecommendedItem.getMenuId() > 0) {
		            if (!first) {
		                sql.append(", ");
		                values.append(", ");
		            }
		            sql.append("MenuId");
		            values.append("?");
		            first = false;
		        }
		        if (chefRecommendedItem.getVoteCount() > 0) {
		            if (!first) {
		                sql.append(", ");
		                values.append(", ");
		            }
		            sql.append("Vote");
		            values.append("?");
		            first = false;
		        }
		        sql.append(") ");
		        values.append(")");
		        sql.append(values);
		        System.out.println(sql.toString());
		        Connection databaseConnection =  databaseHelper.getConnection();
	        	final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
	        	 int paramIndex = 1;
	        	 if (chefRecommendedItem.getMenuId() > 0) statement.setInt(paramIndex++, chefRecommendedItem.getMenuId());
	             if (chefRecommendedItem.getVoteCount() > 0) statement.setInt(paramIndex++, chefRecommendedItem.getVoteCount());
//	             if (newItem.getAvailabilityStatus() != null && !newItem.getAvailabilityStatus().isEmpty()) statement.setString(paramIndex++, newItem.getAvailabilityStatus());
//	             if (newItem.getMealType() != null && !newItem.getMealType().isEmpty()) statement.setString(paramIndex++, newItem.getMealType());
//	             if (newItem.getScore() != 0.0f) statement.setFloat(paramIndex++, newItem.getScore()); 
//	             if (newItem.getMenuId() > 0) statement.setInt(paramIndex++, newItem.getMenuId());
	            //final Statement statement = databaseConnection.createStatement();
	             System.out.println(statement.toString());
				rowSaved =+ databaseHelper.write(statement);
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
		List<T> votedMenus = new ArrayList<>();
		ResultSet cursor;
		//String selectQuery = "SELECT * FROM Menu";
		cursor = databaseHelper.readAll(query);
		 while (cursor.next()) {
             ChefRecommendation votedMenu = new ChefRecommendation();
             votedMenu.setMenuId(cursor.getInt("MenuId"));
             votedMenu.setId(cursor.getInt("Id"));
             votedMenu.setVoteCount(cursor.getInt("VoteCount"));
             if(cursor.getString("MenuItem") != null)votedMenu.setMenuName(cursor.getString("MenuItem"));
             if(cursor.getString("Score") != null)votedMenu.setScore(cursor.getFloat("Score"));
             votedMenus.add((T)votedMenu);
         }
		return votedMenus;
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
