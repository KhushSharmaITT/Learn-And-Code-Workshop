package com.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.exception.DuplicateDataException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.ChefRecommendation;
import com.model.Feedback;
import com.model.Notification;
import com.model.UserLogger;
import com.model.UserProfile;
import com.model.VotedItem;
import com.payload.ChefRecommendPayloadHelper;
import com.service.ChefRecommendationService;
import com.service.FeedbackService;
import com.service.NotificationService;
import com.service.UserLoggerService;
import com.service.UserProfileService;
import com.service.VotedItemService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class ManageEmployeeAction implements Action {

	private ChefRecommendationService chefRecommendationService;
	private JsonWrapper<RequestWrapper> jsonWrapper;
	private VotedItemService votedItemService;
	private FeedbackService feedbackService;
	private Hashtable<String, Object> outputToProcess;
	private String dataToProcess;
	private UserLoggerService userLoggerService;
	private List<UserLogger> userLoggersToInsert;

	public ManageEmployeeAction() {
		chefRecommendationService = new ChefRecommendationService();
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
		this.jsonWrapper.setPrettyFormat(true);
		votedItemService = new VotedItemService();
		feedbackService = new FeedbackService();
		this.userLoggerService = new UserLoggerService();
		this.userLoggersToInsert = new ArrayList<>();
	}

	@Override
	public String handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, DuplicateDataException {
		String actionToPerform = data.split("=")[1].trim();

		if (actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION)) {
			return viewChefsRecommendation(data);
		} else if (actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS)) {
			return voteItems(data);
		} else if (actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_FEEDBACK)) {
			return prepareFeedbackToInsert(data);
		} else if (actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION)) {
			return viewNotification(data);
		} else if (actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_UPDATE_PROFILE)) {
			return updateProfile(data);
		}
		return null;
	}

	private String viewChefsRecommendation(String data) throws SQLException {
		outputToProcess = new Hashtable<>();
		dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<ChefRecommendation> userActionWrapper = chefRecommendationService
				.prepareUserActionWrapper(requestWrapper);

		chefRecommendationService.prepareItemsForRecommendation(userActionWrapper.getUserWrapper());
		List<ChefRecommendation> rowsRetrieved = chefRecommendationService.getChefsRecommendation();

		UserLogger userLogger = userLoggerService.prepareUserLogger(userActionWrapper.getUserWrapper().getId(),
				userActionWrapper.getActionToPerform());
		userLoggersToInsert.add(userLogger);
		userLoggerService.createUserLogs(userLoggersToInsert);

		UserActionWrapper<ChefRecommendation> userActionResponseWrapper = new UserActionWrapper<>();
		userActionResponseWrapper.setActionData(rowsRetrieved);
		userActionResponseWrapper.setActionToPerform(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION_RESPONSE);

		outputToProcess.put(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION_RESPONSE, userActionResponseWrapper);
		final ChefRecommendPayloadHelper<ResponseWrapper> chefRecommendPayloadHelper = new ChefRecommendPayloadHelper<>(
				outputToProcess);
		final ResponseWrapper responseWrapper = chefRecommendPayloadHelper.getResponsePayload();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		final String jsonString = jsonResponseWrapper.convertIntoJson(responseWrapper);
		return jsonString + "=" + "Successfully retrieved the records.";
	}

	private String voteItems(String data) throws SQLException, DuplicateDataException {
		dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<VotedItem> userActionWrapper = votedItemService.prepareUserActionWrapper(requestWrapper);
		String rowSaved = votedItemService.saveVotedItem(userActionWrapper.getActionData());
		UserLogger userLogger = userLoggerService.prepareUserLogger(userActionWrapper.getUserWrapper().getId(),
				userActionWrapper.getActionToPerform());
		userLoggersToInsert.add(userLogger);
		userLoggerService.createUserLogs(userLoggersToInsert);
		return rowSaved + " " + "Record successfuly saved." + "=" + "Record successfuly saved.";
	}

	private String prepareFeedbackToInsert(String data) throws SQLException, DuplicateDataException {
		dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<Feedback> userActionWrapper = feedbackService.prepareUserActionWrapper(requestWrapper);
		List<Feedback> processedFeedbacks = feedbackService
				.processFeedbacksForSentiments(userActionWrapper.getActionData());
		String rowSaved = feedbackService.saveFeedbacks(processedFeedbacks);
		UserLogger userLogger = userLoggerService.prepareUserLogger(userActionWrapper.getUserWrapper().getId(),
				userActionWrapper.getActionToPerform());
		userLoggersToInsert.add(userLogger);
		userLoggerService.createUserLogs(userLoggersToInsert);
		return rowSaved + " " + "Record successfuly saved." + "=" + "Record successfuly saved.";
	}

	private String viewNotification(String data) throws SQLException {
		dataToProcess = data.split("=")[0].trim();
		NotificationService notificationService = new NotificationService();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<Notification> userActionWrapper = notificationService
				.prepareUserActionWrapper(requestWrapper);
		String rowsRetrieved = notificationService.viewNotifications();

		UserLogger userLogger = userLoggerService.prepareUserLogger(userActionWrapper.getUserWrapper().getId(),
				userActionWrapper.getActionToPerform());
		userLoggersToInsert.add(userLogger);
		userLoggerService.createUserLogs(userLoggersToInsert);

		return rowsRetrieved+ "=" + "Successfully retrieved the records.";
	}

	private String updateProfile(String data) throws SQLException {
		final String dataToProcess = data.split("=")[0].trim();
		final UserProfileService userProfileService = new UserProfileService();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<UserProfile> userActionWrapper = userProfileService.prepareUserActionWrapper(requestWrapper);
		String rowSaved = userProfileService.upsertProfile(userActionWrapper.getActionData());
		UserLogger userLogger = userLoggerService.prepareUserLogger(userActionWrapper.getUserWrapper().getId(),
				userActionWrapper.getActionToPerform());
		userLoggersToInsert.add(userLogger);
		userLoggerService.createUserLogs(userLoggersToInsert);
		return rowSaved+" Successfully Save the record." + "=" + "Successfully Save the record.";
	}
}
