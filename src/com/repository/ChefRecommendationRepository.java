package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.ChefRecommendation;

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
		List<T> retrievedRecords = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
		ResultSetMetaData rsmd = null;
        rsmd = cursor.getMetaData();
		 while (cursor.next()) {
             ChefRecommendation retrievedRecord = new ChefRecommendation();
             retrievedRecord.setMenuId(cursor.getInt("MenuId"));
             retrievedRecord.setMenuName(cursor.getString("MenuName"));
             retrievedRecord.setScore(cursor.getFloat("Score"));
             if(isColumnPresent(rsmd, "VoteCount"))retrievedRecord.setVoteCount(cursor.getInt("VoteCount"));
             if(isColumnPresent(rsmd, "vegetarian_preference") && cursor.getString("Preference")!= null)retrievedRecord.setPreference(cursor.getString("Preference"));
             if(isColumnPresent(rsmd, "spice_level"))retrievedRecord.setSpiceLevel(cursor.getString("SpiceLevel"));
             if(isColumnPresent(rsmd, "cuisine_preference"))retrievedRecord.setCuisinePreference(cursor.getString("CuisinePreference"));
             if(isColumnPresent(rsmd, "sweet_tooth"))retrievedRecord.setSweetTooth(cursor.getString("SweetTooth"));
             if(isColumnPresent(rsmd, "MealType"))retrievedRecord.setMealType(cursor.getString("MealType"));
             retrievedRecords.add((T)retrievedRecord);
         }
		return retrievedRecords;
	}
	private boolean isColumnPresent(ResultSetMetaData rsmd, String columnName) throws SQLException {
		int columnCount = rsmd.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            if (rsmd.getColumnName(index).equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
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
