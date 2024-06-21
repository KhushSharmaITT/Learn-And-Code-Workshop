package com.repository;

import java.util.List;

import com.database.DatabaseHelper;
import com.database.SQLDatabaseHelper;

public interface Repository<T> {
	public DatabaseHelper databaseHelper = new SQLDatabaseHelper();
    public void save(T entity);
    public T find(String id);
    public List<T> findAll();
    public void delete(T entity);

}
