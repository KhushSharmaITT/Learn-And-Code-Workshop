package com.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.model.UserProfile;
import com.repository.UserProfileRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class UserProfileService {
	private Type type;
	private JsonWrapper<UserActionWrapper<UserProfile>> jsonWrapper;
	private UserProfileRepository<UserProfile> repository;

	public UserProfileService() {
		type = new TypeToken<UserActionWrapper<UserProfile>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		repository = new UserProfileRepository<>(); 
	}

	public UserActionWrapper<UserProfile> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<UserProfile> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}

	public String upsertProfile(List<UserProfile> userProfiles) throws SQLException {
		List<UserProfile> userProfilesRetrieved;
		List<UserProfile> userProfilesToUpdate = new ArrayList<>();
		List<UserProfile> userProfilesToInsert = new ArrayList<>();
		for(UserProfile userProfile : userProfiles ) {
			String queryTofind = "SELECT * FROM user_profiles WHERE user_id = "+userProfile.getUserId();
			userProfilesRetrieved = repository.findRecords(queryTofind);
			if(!userProfilesRetrieved.isEmpty()) {
				userProfilesToUpdate.add(userProfile);
			}
			else {
				userProfilesToInsert.add(userProfile);
			}
		}
		if(!userProfilesToUpdate.isEmpty()) {
			int rowsUpdated = repository.update(userProfilesToUpdate);
			return String.valueOf(rowsUpdated);
		}
		if(!userProfilesToInsert.isEmpty()) {
			int rowsInserted = repository.save(userProfilesToInsert);
			return String.valueOf(rowsInserted);
		}
		return null;
	}
}
