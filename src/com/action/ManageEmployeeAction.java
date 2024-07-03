package com.action;

import java.sql.SQLException;
import java.util.List;

import com.exception.DuplicateDataException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.Feedback;
import com.payload.FeedbackPayload;
import com.payload.VotedItemPayload;
import com.service.ChefRecommendationService;
import com.service.FeedbackService;
import com.service.NotificationService;
import com.service.VotedItemService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;

public class ManageEmployeeAction implements Action{

	private ChefRecommendationService chefRecommendationService;
	private JsonWrapper<RequestWrapper> jsonWrapper;
	private VotedItemService votedItemService;
	private FeedbackService feedbackService;

	public ManageEmployeeAction() {
		chefRecommendationService = new ChefRecommendationService();
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        votedItemService = new VotedItemService();
        feedbackService = new FeedbackService();
	}

	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, DuplicateDataException {
		// TODO Auto-generated method stub
		System.out.println("yeahhhhhh finally in employee menu");
		String actionToPerform = data.split("=")[1].trim();
		final String dataToProcess = data.split("=")[0].trim();

		if(actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION)) {
			System.out.println("In admin view");
			String rowsRetrieved = chefRecommendationService.viewChefsRecommendation();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS)) {
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			VotedItemPayload votedItemPayload = votedItemService.getVotedItemPayload(requestWrapper);
			String rowSaved = votedItemService.saveVotedItem(votedItemPayload.getVotedItemWrapperDetails());
			return rowSaved+" "+"Record successfuly saved." +"="+ "Record successfuly saved.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_FEEDBACK)) {
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			FeedbackPayload feedbackPayload = feedbackService.getFeedbackPayload(requestWrapper);
			List<Feedback> processedFeedbacks = feedbackService.processFeedbacksForSentiments(feedbackPayload.getFeedbackWrapperDetails());
			String rowSaved = feedbackService.saveFeedbacks(processedFeedbacks);
			return rowSaved+" "+"Record successfuly saved." +"="+ "Record successfuly saved.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION)) {
			System.out.println("In notification view");
			NotificationService notificationService = new NotificationService();
			String rowsRetrieved = notificationService.viewNotifications();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
		}
		return null;
	}
}
