package com.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.Hashtable;
import java.util.List;

import com.client.ClientHandler;
import com.client.ClientInputHandler;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.helper.EmployeeHelper;
import com.model.ChefRecommendation;
import com.model.Feedback;
import com.model.Menu;
import com.model.UserProfile;
import com.model.VotedItem;
import com.payload.UserPayload;
import com.service.FeedbackService;
import com.service.UserService;
import com.service.VotedItemService;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class EmployeeController implements Controller{ 

	private JsonWrapper<ResponseWrapper> jsonWrapper;
	private Hashtable<String, Object> inputsToProcess;
	private ClientInputHandler clientInputHandler;
	private ClientHandler clientHandler;
	private UserWrapper userWrapper;
	private VotedItemService votedItemService;
	private FeedbackService feedbackService;
	private EmployeeHelper helper;
	private boolean isMenuIdValid;
    public EmployeeController() {
    	this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        clientInputHandler =  ClientInputHandler.getInstance();
        votedItemService = new VotedItemService();
        feedbackService = new FeedbackService();
        helper = EmployeeHelper.getInstance();
    }
	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException, UnknownHostException, IOException {
    	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
    	UserPayload userResponsePayload = getUserDetails(responseWrapper);
    	this.userWrapper = userResponsePayload.getUserWrapperDetails();
        processEmployeeAction();		
	}

	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper;
	    jsonWrapper = new JsonWrapper<>(UserPayload.class);
	    jsonWrapper.setPrettyFormat(true);
	    UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}
	private void processEmployeeAction() throws InvalidArgumentException, UnknownHostException, IOException {
		String actions = "Please choose an option\n"+
		                 "1. View Notification\n"+
				         "2. View Menu\n"+
				         "3. Update your Profile\n"+
		                 "4. View Chef's Recommendation\n"+
                         "5. Vote for Next Day Recommendation\n"+
		                 "6. Give Feedback to Chef\n"+
                         "7. Exit\n"+
		                 "Enter your choice: \n";

		String choice = "";
		boolean endProcess = false;
		do {
			choice = ConsoleService.getUserInput(actions);
		switch (choice) {
		case "1":
			viewNotification();
        	break;
		case "2":
			viewMenu();
			break;
		case "3":
			updateYourProfile();
			break;
        case "4":
        	viewChefsRecommendation();
        	break;
        case "5":
        	voteForNextDayRecommendation();
        	break;
        case "6":
        	giveFeedbackToChef();        	
            break;
        case "7":
        	endProcess = true;
            System.out.println("Exiting the program...");
            break;
        default:
            System.out.println("Invalid choice. Please try again.");
	}
		System.out.println(); 
    } while (!endProcess);
}
	
	private void viewNotification() throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();
		inputsToProcess.put("Employee:view_notification", "viewNotification");
		clientInputHandler.processArguments(inputsToProcess);
    	clientInputHandler.processOperation();
	}
	
	private void viewMenu() throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
    	inputsToProcess.put("Employee:view", "viewMenu");
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	    clientHandler = ClientHandler.getInstance();
        String datafromServer = clientHandler.getDataFromServer();
        if(!datafromServer.isBlank()) {
        	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
        	UserActionWrapper<Menu> serverResponseWrapper = helper.getServerResponse(responseWrapper);
        	helper.viewMenu(serverResponseWrapper.getActionData());
        }
	}

	private void updateYourProfile() throws InvalidArgumentException {
		UserActionWrapper<UserProfile> userActionWrapper = new UserActionWrapper<>();
		final UserService userService = new UserService();
		inputsToProcess = new Hashtable<>();
		List<UserProfile> userProfiles = new ArrayList<>();
		UserProfile userProfile = userService.getUserProfile();
		userProfile.setUserId(userWrapper.getId());
		userProfiles.add(userProfile);
		
		userActionWrapper.setActionData(userProfiles);
		userActionWrapper.setActionToPerform("Employee:updateProfile");
		
		inputsToProcess.put("Employee:updateProfile", userActionWrapper);
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	}
	
	private void viewChefsRecommendation() throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
    	UserActionWrapper<ChefRecommendation> userActionWrapper = new UserActionWrapper<>();
    	userActionWrapper.setUserWrapper(userWrapper);
    	userActionWrapper.setActionToPerform("Employee:view_chefRecommendations");
    	inputsToProcess.put("Employee:view_chefRecommendations", userActionWrapper);
    	clientInputHandler.processArguments(inputsToProcess);
    	clientInputHandler.processOperation();
    	clientHandler = ClientHandler.getInstance();
    	String datafromServer = clientHandler.getDataFromServer();
    	if(!datafromServer.isBlank()) {
    		ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
    		UserActionWrapper<ChefRecommendation> userActionResponseWrapper = helper.getChefRecommendationResponse(responseWrapper);
    		helper.viewChefRecommendation(userActionResponseWrapper.getActionData());
    	}
    	    
	}
	
	private void voteForNextDayRecommendation() throws InvalidArgumentException {
		UserActionWrapper<VotedItem> userActionWrapper = new UserActionWrapper<>();
    	inputsToProcess = new Hashtable<>();
    	List<VotedItem> votedItems = new ArrayList<>();
    	VotedItem itemToVote = votedItemService.getItemToVote();
    	itemToVote.setUserId(userWrapper.getId());
    	votedItems.add(itemToVote);
    	
    	userActionWrapper.setActionData(votedItems);
    	userActionWrapper.setActionToPerform("Employee:voteItem");
    	
    	inputsToProcess.put("Employee:voteItem", userActionWrapper);
    	isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
    	if(isMenuIdValid) {
    		clientInputHandler.processOperation();
    	}
	}
	
	private void giveFeedbackToChef() throws InvalidArgumentException {
		UserActionWrapper<Feedback> userActionWrapper = new UserActionWrapper<>();
    	inputsToProcess = new Hashtable<>();
    	List<Feedback> userFeedbacks = new ArrayList<>();
    	feedbackService = new FeedbackService();
    	Feedback userFeedback = new Feedback();
    	userFeedback = feedbackService.getUserFeedback();
    	userFeedback.setUserId(userWrapper.getId());
    	userFeedbacks.add(userFeedback);
    	
    	userActionWrapper.setActionData(userFeedbacks);
    	userActionWrapper.setActionToPerform("userFeedbacks");
    	
    	inputsToProcess.put("Employee:feedback", userActionWrapper);
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	}
}
