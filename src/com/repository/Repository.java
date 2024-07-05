package com.repository;

import java.sql.SQLException;
import java.util.List;

import com.database.DatabaseHelper;
import com.database.SQLDatabaseHelper;

public interface Repository<T> {
	public DatabaseHelper databaseHelper = new SQLDatabaseHelper();
    public int save(List<T> entity) throws SQLException;
    public T find(String id) throws SQLException;
    public List<T> findAll() throws SQLException;
    public List<T> findRecords(String query) throws SQLException;
    public int delete(List<T> entitiesToDelete) throws SQLException;
	public int update(List<T> entitiesToUpdate) throws SQLException;
}
