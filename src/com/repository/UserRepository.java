package com.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserRepository<T> implements Repository<T>{

	@Override
	public int save(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T find(String id) throws SQLException {
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
	public int delete(T entity) {
		return 0;
		// TODO Auto-generated method stub
		
	}

	@Override
	public int update(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
