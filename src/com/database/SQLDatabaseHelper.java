package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDatabaseHelper implements DatabaseHelper{

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
    public void write(String insertQueryText) {
//        try {
//            //final Statement statement = databaseConnection.createStatement();
//            //statement.execute(insertQueryText);
//        } catch (SQLException issue) {
//            System.out.println("Failed to write into DB : "+issue.getLocalizedMessage());
//        }
    }

    @Override
    public ResultSet read(String selectQueryText, String Id) {
        ResultSet cursor = null;
        try {
            final PreparedStatement statement = databaseConnection.prepareStatement(selectQueryText);
            statement.setString(1, Id);
            cursor = statement.executeQuery();
        } catch (SQLException issue) {
            System.out.println("Failed to retrieve from DB : "+issue.getLocalizedMessage());
        }
        return cursor;
    }

}
