package com.repository;

import java.sql.ResultSet;
import java.util.List;

public class UserRepository<T> implements Repository<T>{

	@Override
	public void save(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T find(String id) {
		ResultSet cursor;
		String findQuery = "SELECT * FROM User WHERE EmailId=? ";
		cursor = databaseHelper.read(findQuery, id);
		return (T) cursor;
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}
	

}
