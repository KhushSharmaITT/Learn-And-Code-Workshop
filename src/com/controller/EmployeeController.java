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
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class EmployeeController implements Controller{ 

	private JsonWrapper<ResponseWrapper> jsonWrapper;
    public EmployeeController() {
    	this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
    }
	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException, UnknownHostException, IOException {
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
	private void processEmployeeAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
		final VotedItemService votedItemService = new VotedItemService();
	    FeedbackService feedbackService;
		Hashtable<String, Object> inputsToProcess;
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
			inputsToProcess = new Hashtable<>();
			inputsToProcess.put("Employee:view_notification", "viewNotification");
			clientInputHandler.processArguments(inputsToProcess);
        	clientInputHandler.processOperation();
        	break;
		case "2":
			inputsToProcess = new Hashtable<>();
        	inputsToProcess.put("Employee:view", "viewMenu");
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
    	    final ClientHandler clientHandler = ClientHandler.getInstance();
            String datafromServer = clientHandler.getDataFromServer();
            if(!datafromServer.isBlank()) {
            	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
            	final EmployeeHelper helper = EmployeeHelper.getInstance();
            	UserActionWrapper<Menu> serverResponseWrapper = helper.getServerResponse(responseWrapper);
            	helper.viewMenu(serverResponseWrapper.getActionData());
            }
			break;
		case "3":
			UserActionWrapper<UserProfile> userActionWrapper_3 = new UserActionWrapper<>();
			final UserService userService = new UserService();
			inputsToProcess = new Hashtable<>();
			List<UserProfile> userProfiles = new ArrayList<>();
			UserProfile userProfile = userService.getUserProfile();
			userProfile.setUserId(userWrapper.getId());
			userProfiles.add(userProfile);
			
			userActionWrapper_3.setActionData(userProfiles);
			userActionWrapper_3.setActionToPerform("Employee:updateProfile");
			
			inputsToProcess.put("Employee:updateProfile", userActionWrapper_3);
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
			break;
        	
        case "4":
        	inputsToProcess = new Hashtable<>();
        	UserActionWrapper<ChefRecommendation> userActionWrapper_4 = new UserActionWrapper<>();
        	userActionWrapper_4.setUserWrapper(userWrapper);
			userActionWrapper_4.setActionToPerform("Employee:view_chefRecommendations");
        	inputsToProcess.put("Employee:view_chefRecommendations", userActionWrapper_4);
        	clientInputHandler.processArguments(inputsToProcess);
        	clientInputHandler.processOperation();
        	break;
        case "5":
        	UserActionWrapper<VotedItem> userActionWrapper = new UserActionWrapper<>();
        	inputsToProcess = new Hashtable<>();
        	List<VotedItem> votedItems = new ArrayList<>();
        	VotedItem itemToVote = votedItemService.getItemToVote();
        	itemToVote.setUserId(userWrapper.getId());
        	votedItems.add(itemToVote);
        	
        	userActionWrapper.setActionData(votedItems);
        	userActionWrapper.setActionToPerform("Employee:voteItem");
        	
        	inputsToProcess.put("Employee:voteItem", userActionWrapper);
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
        	break;
        case "6":
        	UserActionWrapper<Feedback> userActionWrapper_2 = new UserActionWrapper<>();
        	inputsToProcess = new Hashtable<>();
        	List<Feedback> userFeedbacks = new ArrayList<>();
        	feedbackService = new FeedbackService();
        	Feedback userFeedback = new Feedback();
        	userFeedback = feedbackService.getUserFeedback();
        	userFeedback.setUserId(userWrapper.getId());
        	userFeedbacks.add(userFeedback);
        	
        	userActionWrapper_2.setActionData(userFeedbacks);
        	userActionWrapper_2.setActionToPerform("userFeedbacks");
        	
        	inputsToProcess.put("Employee:feedback", userActionWrapper_2);
        	clientInputHandler.processArguments(inputsToProcess);
    	    clientInputHandler.processOperation();
        	
            break;
        case "7":
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
