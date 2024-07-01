package com.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.client.ClientInputHandler;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.Feedback;
import com.model.VotedItem;
import com.payload.UserPayload;
import com.service.FeedbackService;
import com.service.VotedItemService;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class EmployeeController implements Controller{

	private JsonWrapper<ResponseWrapper> jsonWrapper;
    public EmployeeController() {
    	this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
    }
	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException {
    	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
    	UserPayload userResponsePayload = getUserDetails(responseWrapper);
        processEmployeeAction(userResponsePayload.getUserWrapperDetails());
		// TODO Auto-generated method stub
		
	}

	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper;
	    jsonWrapper = new JsonWrapper<>(UserPayload.class);
	    jsonWrapper.setPrettyFormat(true);
	    UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}
	private void processEmployeeAction(UserWrapper userWrapper) throws InvalidArgumentException {
		// TODO Auto-generated method stub
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
		final VotedItemService votedItemService = new VotedItemService();
	    FeedbackService feedbackService;
		Hashtable<String, Object> inputsToProcess;
		String actions = "Please choose an option\n"+
		                 "1. View Chef's Recommendation\n"+
                         "2. Vote for Next Day Recommendation\n"+
		                 "3. Give Feedback to Chef\n"+
                         "4. Exit\n"+
		                 "Enter your choice: \n";

		String choice = "";
		boolean endProcess = false;
		do {
			choice = ConsoleService.getUserInput(actions);
		switch (choice) {
        case "1":
        	inputsToProcess = new Hashtable<>();
        	inputsToProcess.put("Employee:view_chefRecommendations", "viewChefRecommendations");
        	clientInputHandler.processArguments(inputsToProcess);
        	clientInputHandler.processOperation();
        	break;
        case "2":
        	inputsToProcess = new Hashtable<>();
        	List<VotedItem> votedItems = new ArrayList<>();
        	VotedItem itemToVote = votedItemService.getItemToVote();
        	itemToVote.setUserId(userWrapper.getId());
        	System.out.println(itemToVote.getUserId());
        	//System.out.println(itemToVote.getChefRecommendedItemId());
        	votedItems.add(itemToVote);
        	inputsToProcess.put("Employee:voteItem", votedItems);
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
        	break;
        case "3":
        	inputsToProcess = new Hashtable<>();
        	List<Feedback> userFeedbacks = new ArrayList<>();
        	feedbackService = new FeedbackService();
        	Feedback userFeedback = new Feedback();
        	userFeedback = feedbackService.getUserFeedback();
        	userFeedback.setUserId(userWrapper.getId());
        	userFeedbacks.add(userFeedback);
        	inputsToProcess.put("Employee:feedback", userFeedbacks);
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
        	
            break;
        case "4":
        	endProcess = true;
            System.out.println("Exiting the program...");
            break;
        default:
            System.out.println("Invalid choice. Please try again.");
	}
		System.out.println(); // Print a newline for better readability
    } while (!endProcess);
}

}
