package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.model.Menu;

public class SQLDatabaseHelper<T> implements DatabaseHelper<T>{

	private Connection databaseConnection;

    public SQLDatabaseHelper() {
        databaseConnection = getConnection();
    }

    private Connection getConnection() {
        try {
			databaseConnection = DriverManager.getConnection(
                DatabaseCredentials.HOST_DATABASE_URL+ "/" +DatabaseCredentials.DATABASE_NAME,
				DatabaseCredentials.USERNAME, DatabaseCredentials.PASSWORD
            );
			if(!databaseConnection.isClosed()){
				return databaseConnection;
			} else {
				return null;
			}
		} catch (SQLException issue){
			System.out.println("Failed to create connection with DB : "+issue.getMessage());
			return null;
		}
    }

    @Override
    public int write(String insertQueryText, T entity) throws SQLException{
    	Menu newItem = (Menu)entity;
    	int rowSaved = 0;
       // try {
        	final PreparedStatement statement = databaseConnection.prepareStatement(insertQueryText);
        	 int paramIndex = 1;
        	 if (newItem.getName() != null && !newItem.getName().isEmpty()) statement.setString(paramIndex++, newItem.getName());
             if (newItem.getPrice() != 0.0f) statement.setDouble(paramIndex++, newItem.getPrice());
             if (newItem.getAvailabilityStatus() != null && !newItem.getAvailabilityStatus().isEmpty()) statement.setString(paramIndex++, newItem.getAvailabilityStatus());
             if (newItem.getMealType() != null && !newItem.getMealType().isEmpty()) statement.setString(paramIndex++, newItem.getMealType());
             if (newItem.getMenuId() > 0) statement.setInt(paramIndex++, newItem.getMenuId());
            //final Statement statement = databaseConnection.createStatement();
             System.out.println(statement.toString());
            rowSaved = statement.executeUpdate();
        //} catch (SQLException issue) {
           // System.out.println("Failed to write into DB : "+issue.getLocalizedMessage());
        //}
    	return rowSaved;
    }

    @Override
    public ResultSet read(String selectQueryText, String Id) throws SQLException {
        ResultSet cursor = null;
        //try {
            final PreparedStatement statement = databaseConnection.prepareStatement(selectQueryText);
            statement.setString(1, Id);
            cursor = statement.executeQuery();
       // } catch (SQLException issue) {
        //    System.out.println("Failed to retrieve from DB : "+issue.getLocalizedMessage());
       // }
        return cursor;
    }
    
    @Override
    public ResultSet readAll(String selectQueryText) throws SQLException {
        ResultSet cursor = null;
        //try {
            final PreparedStatement statement = databaseConnection.prepareStatement(selectQueryText);
            cursor = statement.executeQuery();
        //} catch (SQLException issue) {
        //    System.out.println("Failed to retrieve from DB : "+issue.getLocalizedMessage());
        //}
        return cursor;
    }

	@Override
	public int delete(String deleteQuery, String menuId) throws SQLException{
		int rowDeleted = 0;
		//try {
        	final PreparedStatement statement = databaseConnection.prepareStatement(deleteQuery);
        	statement.setString(1, menuId);
        	System.out.println(statement.toString());
        	rowDeleted = statement.executeUpdate();
        //} catch (SQLException issue) {
        //    System.out.println("Failed to write into DB : "+issue.getLocalizedMessage());
        //}
		return rowDeleted;
	}

}
