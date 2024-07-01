package com.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserRepository<T> implements Repository<T>{

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
	public List<T> findRecords(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(List<T> entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<T> entitiesToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(List<T> entitiesToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
