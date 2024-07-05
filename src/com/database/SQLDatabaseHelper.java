package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.Menu;

public class SQLDatabaseHelper implements DatabaseHelper{

	private Connection databaseConnection;

    public SQLDatabaseHelper() {
    	if(this.databaseConnection == null) {
    		databaseConnection = initializeConnection();
    	}
    }
    
    @Override
    public Connection getConnection() {
    	return this.databaseConnection;
    }

    private Connection initializeConnection() {
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
    public int write(PreparedStatement statement) throws SQLException{
    	int rowSaved = 0;
        rowSaved = statement.executeUpdate();
    	return rowSaved;
    }

    @Override
    public ResultSet read(String selectQueryText, String Id) throws SQLException {
        ResultSet cursor = null;
            final PreparedStatement statement = databaseConnection.prepareStatement(selectQueryText);
            statement.setString(1, Id);
            cursor = statement.executeQuery();
        return cursor;
    }

    @Override
    public ResultSet readAll(String selectQueryText) throws SQLException {
        ResultSet cursor = null;
            final PreparedStatement statement = databaseConnection.prepareStatement(selectQueryText);
            cursor = statement.executeQuery();
        return cursor;
    }

	@Override
	public int delete(String deleteQuery, String menuId) throws SQLException{
		int rowDeleted = 0;
        	final PreparedStatement statement = databaseConnection.prepareStatement(deleteQuery);
        	statement.setString(1, menuId);
        	System.out.println(statement.toString());
        	rowDeleted = statement.executeUpdate();
		return rowDeleted;
	}

}
