package com.database;

import java.sql.ResultSet;

public interface DatabaseHelper {
	public void write(String insertQueryText);
    public ResultSet read(String selectQueryText, String Id);
}
