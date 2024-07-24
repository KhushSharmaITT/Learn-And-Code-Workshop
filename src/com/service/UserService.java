package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.console.ConsoleService;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.UserProfile;
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
		UserPayload userPayload = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		final UserWrapper userWrapper = userPayload.getUserWrapperDetails();
			 if(userWrapper.getEmailId()== null || userWrapper.getPassword() == null) {
				 throw new InvalidDataException(ErrorMessageConstant.INVALID_USER_DATA);
			 }
		return userPayload; 
	}

	public UserWrapper getUserWithRole(UserWrapper userToAuthenticate) throws SQLException, InvalidDataException, UserNotFoundException {
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

	public UserProfile getUserProfile() {
		UserProfile userProfile = new UserProfile();
		userProfile.setPreference(ConsoleService.getUserInput("Enter your preference(Vegetarian/Non Vegetarian/Eggetarian) : ").toLowerCase());
		userProfile.setSpiceLevel(ConsoleService.getUserInput("Enter your spice level(High/Medium/Low) : ").toLowerCase());
		userProfile.setCuisinePreference(ConsoleService.getUserInput("What do you prefer most(North Indian/South Indian/Other) : ").toLowerCase());
		userProfile.setSweetTooth(ConsoleService.getUserInput("Do you have a sweet tooth(Yes/No) : ").toLowerCase());
		return userProfile;
	}

}
