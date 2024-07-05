package com.action;

import java.sql.SQLException;

import com.exception.DiscardMenuDurationException;
import com.exception.DuplicateDataException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;

public interface Action {
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, DuplicateDataException, DiscardMenuDurationException;
}
