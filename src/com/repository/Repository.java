package com.repository;

import java.sql.SQLException;
import java.util.List;

import com.database.DatabaseHelper;
import com.database.SQLDatabaseHelper;

public interface Repository<T> {
	public DatabaseHelper<Object> databaseHelper = new SQLDatabaseHelper<>();
    public int save(T entity) throws SQLException;
    public T find(String id) throws SQLException;
    public List<T> findAll() throws SQLException;
    public int delete(T entity) throws SQLException;
	public int update(T entity) throws SQLException;

}
