package com.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.model.DiscardItem;
import com.model.UserLogger;
import com.repository.UserLoggerRepository;
import com.utility.ActionChoiceConstant;
import com.utility.core.UserActionWrapper;

public class UserLoggerService {
	UserLoggerRepository<UserLogger> repository;
	List<UserLogger> userLogsToInsert;
	public UserLoggerService() {
		repository = new UserLoggerRepository<>();
		userLogsToInsert = new ArrayList<>();
	}

	public boolean checkUserLogForDiscardProcess(UserActionWrapper<DiscardItem> userActionWrapper) throws SQLException {
		System.out.println("Usersloggerervice");
		List<UserLogger> userLogs = new ArrayList<>();
		String queryToFind = "SELECT * FROM user_logger WHERE UserAction = '"+ActionChoiceConstant.CHEF_DISCARD_ITEM +"' ORDER BY DATE(Date_Created) DESC LIMIT 1";
		userLogs = repository.findRecords(queryToFind);
		LocalDateTime currentDateTime = LocalDateTime.now();
		if(userLogs != null && !userLogs.isEmpty()) {
			UserLogger userLog = userLogs.getFirst();
			Timestamp dateCreated = userLog.getDateCreated();
			LocalDateTime userLogDateCreated = dateCreated.toLocalDateTime();
			LocalDateTime durationDate = userLogDateCreated.plusDays(30);
			
			if (currentDateTime.isAfter(durationDate)) {
				UserLogger newUserLog = new UserLogger();
				newUserLog.setUserAction(ActionChoiceConstant.CHEF_DISCARD_ITEM);
				newUserLog.setDateCreated(Timestamp.valueOf(currentDateTime));
				newUserLog.setUserId(userActionWrapper.getUserWrapper().getId());
				userLogsToInsert.add(newUserLog);
				createUserLogs();
				return false;
			}else {
				return true;
			}
		}
			else {
				UserLogger newUserLog = new UserLogger();
				newUserLog.setUserAction(ActionChoiceConstant.CHEF_DISCARD_ITEM);
				newUserLog.setDateCreated(Timestamp.valueOf(currentDateTime));
				newUserLog.setUserId(userActionWrapper.getUserWrapper().getId());
				userLogsToInsert.add(newUserLog);
				createUserLogs();
				return false;
			}
	
	}

	private void createUserLogs() throws SQLException {
		int rowSaved = repository.save(userLogsToInsert);
		System.out.println(rowSaved + " user logs saved successfully");
		//return String.valueOf(rowSaved);
		
	}

}
