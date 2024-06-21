package com.controller;

import java.sql.SQLException;

import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;

public interface Controller {
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException;
}
