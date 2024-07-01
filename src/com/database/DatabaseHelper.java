package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseHelper<T> {
	public Connection getConnection();
	public int write(PreparedStatement statement) throws SQLException;
    public ResultSet read(String selectQueryText, String Id) throws SQLException;
	ResultSet readAll(String selectQueryText) throws SQLException;
	public int delete(String deleteQuery, String Id) throws SQLException;
}
