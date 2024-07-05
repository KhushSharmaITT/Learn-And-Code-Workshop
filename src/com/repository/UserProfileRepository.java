package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.ChefRecommendation;
import com.model.Feedback;
import com.model.Menu;
import com.model.UserProfile;

public class UserProfileRepository<T> implements Repository<T> {

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T profile : entitiesToSave) {
		UserProfile userProfile = (UserProfile)profile;
		 StringBuilder sql = new StringBuilder("INSERT INTO user_profiles (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (userProfile.getUserId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("user_id");
	            values.append("?");
	            first = false;
	        }
	        if (userProfile.getPreference() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("preference");
	            values.append("?");
	            first = false;
	        }
	        if (userProfile.getSpiceLevel() != null && !userProfile.getSpiceLevel().isEmpty()) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("spice_level");
	            values.append("?");
	            first = false;
	        }
	        if (userProfile.getCuisinePreference() != null) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("cuisine_preference");
	            values.append("?");
	            first = false;
	        }
	        if (userProfile.getSweetTooth() != null && !userProfile.getSweetTooth().isEmpty()) {
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
	        if (userProfile.getUserId() > 0) statement.setInt(paramIndex++, userProfile.getUserId());
	        if (userProfile.getPreference() != null) statement.setString(paramIndex++, userProfile.getPreference());
	        if (userProfile.getSpiceLevel() != null && !userProfile.getSpiceLevel().isEmpty()) statement.setString(paramIndex++, userProfile.getSpiceLevel());
	        if (userProfile.getCuisinePreference() != null) statement.setString(paramIndex++, userProfile.getCuisinePreference());
	        if (userProfile.getSweetTooth() != null && !userProfile.getSweetTooth().isEmpty()) statement.setString(paramIndex++, userProfile.getSweetTooth());
	        System.out.println(statement.toString());
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
		while (cursor.next()) {
            UserProfile userProfile = new UserProfile();
            userProfile.setProfileId(cursor.getInt("profile_id"));
            userProfile.setUserId(cursor.getInt("user_id"));
            userProfile.setPreference(cursor.getString("preference"));
            userProfile.setSpiceLevel(cursor.getString("spice_level"));
            userProfile.setCuisinePreference(cursor.getString("cuisine_preference"));
            userProfile.setSweetTooth(cursor.getString("sweet_tooth"));
            retreivedResponses.add((T)userProfile);
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
		int rowUpdated = 0;
		for(T profile : entitiesToUpdate) {
			UserProfile updatedProfile = (UserProfile)profile;
			StringBuilder updateQuery = new StringBuilder("UPDATE user_profiles SET ");
			boolean first = true;
			if (updatedProfile.getPreference() != null && !updatedProfile.getPreference().isEmpty()) {
				updateQuery.append("preference = ?");
	            first = false;
	        }
	        if (updatedProfile.getSpiceLevel() != null && !updatedProfile.getSpiceLevel().isEmpty()) {
	            if (!first) updateQuery.append(", "); 
	            updateQuery.append("spice_level = ?");
	            first = false;
	        }
	        if (updatedProfile.getCuisinePreference() != null && !updatedProfile.getCuisinePreference().isEmpty()) {
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("cuisine_preference = ?");
	            first = false;
	        }
	        if (updatedProfile.getSweetTooth() != null && !updatedProfile.getSweetTooth().isEmpty()) {
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("sweet_tooth = ?");
	        }
	        updateQuery.append(" WHERE user_id = ?");

	        System.out.println(updateQuery.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
        	final PreparedStatement statement = databaseConnection.prepareStatement(updateQuery.toString());
        	int paramIndex = 1;
        	if (updatedProfile.getPreference() != null && !updatedProfile.getPreference().isEmpty()) statement.setString(paramIndex++, updatedProfile.getPreference());
        	if (updatedProfile.getSpiceLevel() != null && !updatedProfile.getSpiceLevel().isEmpty()) statement.setString(paramIndex++, updatedProfile.getSpiceLevel());
        	if (updatedProfile.getCuisinePreference() != null && !updatedProfile.getCuisinePreference().isEmpty()) statement.setString(paramIndex++, updatedProfile.getCuisinePreference());
        	if (updatedProfile.getSweetTooth() != null && !updatedProfile.getSweetTooth().isEmpty()) statement.setString(paramIndex++, updatedProfile.getSweetTooth());
            if (updatedProfile.getUserId() > 0) statement.setInt(paramIndex++, updatedProfile.getUserId()); 
            System.out.println(statement.toString());
            rowUpdated += databaseHelper.write(statement);
		}
		return rowUpdated;
	}

}
