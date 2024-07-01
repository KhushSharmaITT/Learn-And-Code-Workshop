package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.payload.UserPayload;
import com.repository.UserRepository;
import com.utility.ErrorMessageConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.user.UserWrapper;

public class UserService {

	private JsonWrapper<UserPayload> jsonWrapper;
	public UserService() {
		jsonWrapper = new JsonWrapper<>(UserPayload.class);
        jsonWrapper.setPrettyFormat(true);
	}

	public UserPayload prepareUserPayload(RequestWrapper requestWrapper) throws InvalidDataException {
		//final User userToAuthenticate;
		UserPayload userPayload = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		//To be implemented more nicely
		final UserWrapper userWrapper = userPayload.getUserWrapperDetails();
			 if(userWrapper.getEmailId()== null || userWrapper.getPassword() == null) {
				 throw new InvalidDataException(ErrorMessageConstant.INVALID_USER_DATA);
			 }
		return userPayload; 
	}

	public UserWrapper getUserWithRole(UserWrapper userToAuthenticate) throws SQLException, InvalidDataException, UserNotFoundException {
		//String userRole = "";
		final UserRepository<ResultSet> repository = new UserRepository<>();
		ResultSet cursor = repository.find(userToAuthenticate.getEmailId());
		if (cursor != null && cursor.next()) {
			userToAuthenticate.setRole(cursor.getString("Role"));
			userToAuthenticate.setId(cursor.getInt("Id"));
			}else {
			throw new UserNotFoundException(ErrorMessageConstant.NO_USER_FOUND);
		}
		return userToAuthenticate;
	}
}
