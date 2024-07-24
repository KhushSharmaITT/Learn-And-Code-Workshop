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
	LocalDateTime currentDateTime;
	public UserLoggerService() {
		repository = new UserLoggerRepository<>();
		currentDateTime = LocalDateTime.now();
		}

	public boolean checkUserLogForDiscardProcess(UserActionWrapper<DiscardItem> userActionWrapper) throws SQLException {
		List<UserLogger> userLogsToInsert = new ArrayList<>();
		List<UserLogger> userLogs = new ArrayList<>();
		String queryToFind = "SELECT * FROM user_logger WHERE UserAction = '"+ActionChoiceConstant.CHEF_DISCARD_ITEM +"' ORDER BY DATE(Date_Created) DESC LIMIT 1";
		userLogs = repository.findRecords(queryToFind);
		if(userLogs != null && !userLogs.isEmpty()) {
			UserLogger userLog = userLogs.getFirst();
			Timestamp dateCreated = userLog.getDateCreated();
			LocalDateTime userLogDateCreated = dateCreated.toLocalDateTime();
			LocalDateTime durationDate = userLogDateCreated.plusDays(30);
			
			if (currentDateTime.isAfter(durationDate)) {
				UserLogger newUserLog = prepareUserLogger(userActionWrapper.getUserWrapper().getId(),userActionWrapper.getActionToPerform());
				userLogsToInsert.add(newUserLog);
				createUserLogs(userLogsToInsert);
				return false;
			}else {
				return true;
			}
		}
			else {
				UserLogger newUserLog = prepareUserLogger(userActionWrapper.getUserWrapper().getId(),userActionWrapper.getActionToPerform());
				userLogsToInsert.add(newUserLog);
				createUserLogs(userLogsToInsert);
				return false;
			}
	}

	public void createUserLogs(List<UserLogger> userLogsToInsert) throws SQLException {
		int rowSaved = repository.save(userLogsToInsert);
		System.out.println(rowSaved + " user logs saved successfully");		
	}

	public UserLogger prepareUserLogger(int userId, String userAction) {
		UserLogger newUserLog = new UserLogger();
		newUserLog.setUserAction(userAction);
		newUserLog.setDateCreated(Timestamp.valueOf(currentDateTime));
		newUserLog.setUserId(userId);
		return newUserLog;
		
	}
}
