package com.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Feedback;
import com.model.Menu;
import com.model.VotedItem;

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
		String selectQuery = "SELECT * FROM feedback WHERE Is_Processed = 0";
		cursor = databaseHelper.readAll(selectQuery);
		 while (cursor.next()) {
             Feedback feedback = new Feedback();
             feedback.setId(cursor.getInt("Id"));
             feedback.setUserId(cursor.getInt("UserId"));
             feedback.setMenuId(cursor.getInt("MenuId"));
             feedback.setComment(cursor.getString("Comment"));
             feedback.setRating(cursor.getFloat("Rating"));
             feedback.setSentiments(cursor.getString("Sentiments"));
             feedback.setSentimentScore(cursor.getDouble("SentimentScore"));
             feedback.setCreatedDate(cursor.getTimestamp("Date_Created"));
             feedbacks.add((T) feedback);
         }
		return feedbacks;
	}
   
	@Override
	public List<T> findRecords(String query) throws SQLException {
		List<T> retreivedResponses = new ArrayList<>();
		ResultSet cursor;
		cursor = databaseHelper.readAll(query);
		ResultSetMetaData rsmd = null;
        rsmd = cursor.getMetaData();
		while (cursor.next()) {
			Feedback feedback = new Feedback();
			feedback.setMenuId(cursor.getInt("MenuId"));
			if(isColumnPresent(rsmd, "UserId"))feedback.setUserId(cursor.getInt("UserId"));
			if(cursor.getString("ItemName")!= null)feedback.setItemName(cursor.getString("ItemName"));
			if(cursor.getFloat("Avg_Rating")>0)feedback.setRating(cursor.getFloat("Avg_Rating"));
			if(cursor.getDouble("Avg_SentimentScore")>0)feedback.setSentimentScore(cursor.getDouble("Avg_SentimentScore"));
			retreivedResponses.add((T)feedback);
		}
		return retreivedResponses;
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
	        
	        if (newFeedback.getSentimentScore() > 0.0) {
	            if (!first) {
	                sql.append(", ");
	                values.append(", ");
	            }
	            sql.append("SentimentScore");
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
	        if (newFeedback.getSentimentScore() > 0.0f) statement.setDouble(paramIndex++, newFeedback.getSentimentScore());
	        //if (newFeedback.getId() > 0) statement.setInt(paramIndex++, newItem.getMenuId());
	        System.out.println(statement.toString());
	        rowSaved += databaseHelper.write(statement);
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
		int rowUpdated = 0;
		for(T updatedItem : entitiesToUpdate) {
			Feedback updatedFeedback = (Feedback)updatedItem;
			StringBuilder updateQuery = new StringBuilder("UPDATE feedback SET ");
			boolean first = true;
			if (updatedFeedback.getIsDiscardProcessDone() != 0) {
				updateQuery.append("Is_Discard_Process_Done = ?");
	            first = false;
	        }
	        if (updatedFeedback.getIsProcessed() != 0) {
	            if (!first) updateQuery.append(", ");
	            updateQuery.append("Is_Processed = ?");
	            first = false;
	        }
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
	        if (updatedFeedback.getIsDiscardProcessDone() > 0) statement.setInt(paramIndex++, updatedFeedback.getIsDiscardProcessDone());
            if (updatedFeedback.getIsProcessed() > 0) statement.setInt(paramIndex++, updatedFeedback.getIsProcessed());
            if (updatedFeedback.getMenuId() > 0 ) statement.setInt(paramIndex++, updatedFeedback.getMenuId());
            System.out.println(statement.toString());
            rowUpdated += databaseHelper.write(statement);
	    }
		return rowUpdated;
	}
}

