package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Feedback;
import com.model.Menu;

public class FeedbackRepository<T> implements Repository<T> {


	@Override
	public T find(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll() throws SQLException {
		List<T> feedbacks = new ArrayList<>();
		ResultSet cursor;
		String selectQuery = "SELECT * FROM feedback";
		cursor = databaseHelper.readAll(selectQuery);
		 while (cursor.next()) {
             Feedback feedback = new Feedback();
             feedback.setId(cursor.getInt("Id"));
             feedback.setUserId(cursor.getInt("UserId"));
             feedback.setMenuId(cursor.getInt("MenuId"));
             feedback.setComment(cursor.getString("Comment"));
             feedback.setRating(cursor.getFloat("Rating"));
             feedback.setSentiments(cursor.getString("Sentiments"));
             feedback.setCreatedDate(cursor.getTimestamp("Date_Created"));
             feedbacks.add((T) feedback);
         }
		return feedbacks;
	}
   
	@Override
	public List<T> findRecords(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(List<T> entitiesToSave) throws SQLException {
		int rowSaved = 0;
		for(T feedback : entitiesToSave) {
		Feedback newFeedback = (Feedback)feedback;
		 StringBuilder sql = new StringBuilder("INSERT INTO feedback (");
	     StringBuilder values = new StringBuilder("VALUES (");
	     boolean first = true;
	     if (newFeedback.getUserId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("UserId");
	            values.append("?");
	            first = false;
	        }
	        if (newFeedback.getMenuId() > 0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("MenuId");
	            values.append("?");
	            first = false;
	        }
	        if (newFeedback.getComment() != null && !newFeedback.getComment().isEmpty()) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Comment");
	            values.append("?");
	            first = false;
	        }
	        if (newFeedback.getRating() > 0.0f) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Rating");
	            values.append("?");
	            first = false;
	        }
	        if (newFeedback.getSentiments() != null && !newFeedback.getSentiments().isEmpty()) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("Sentiments");
	            values.append("?");
	        }

	        sql.append(") ");
	        values.append(")");
	        sql.append(values);
	        System.out.println(sql.toString());
	        Connection databaseConnection =  databaseHelper.getConnection();
	        final PreparedStatement statement = databaseConnection.prepareStatement(sql.toString());
	        int paramIndex = 1;
	        if (newFeedback.getUserId() > 0) statement.setInt(paramIndex++, newFeedback.getUserId());
	        if (newFeedback.getMenuId() > 0) statement.setInt(paramIndex++, newFeedback.getMenuId());
	        if (newFeedback.getComment() != null && !newFeedback.getComment().isEmpty()) statement.setString(paramIndex++, newFeedback.getComment());
	        if (newFeedback.getRating() > 0.0f) statement.setFloat(paramIndex++, newFeedback.getRating());
	        if (newFeedback.getSentiments() != null && !newFeedback.getSentiments().isEmpty()) statement.setString(paramIndex++, newFeedback.getSentiments());
	        //if (newFeedback.getId() > 0) statement.setInt(paramIndex++, newItem.getMenuId());
	        System.out.println(statement.toString());
	        rowSaved =+ databaseHelper.write(statement);
		}
	        return rowSaved;
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

