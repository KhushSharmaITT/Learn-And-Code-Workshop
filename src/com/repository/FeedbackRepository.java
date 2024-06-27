package com.repository;

import java.sql.SQLException;
import java.util.List;

public class FeedbackRepository<T> implements Repository<T> {


	@Override
	public T find(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
    @Override
	public int save(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}

