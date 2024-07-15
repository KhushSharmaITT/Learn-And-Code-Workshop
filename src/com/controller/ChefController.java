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
import com.helper.ChefHelper;
import com.model.DiscardItem;
import com.model.Menu;
import com.payload.UserPayload;
import com.service.DiscardItemService;
import com.service.MenuService;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class ChefController implements Controller{
	private JsonWrapper<ResponseWrapper> jsonWrapper;
	private Hashtable<String, Object> inputsToProcess;
	private ClientInputHandler clientInputHandler;
	private ClientHandler clientHandler;
	private MenuService menuService;
	private boolean isMenuIdValid;
	private UserWrapper userWrapper;
	DiscardItemService discardItemService;
	 public ChefController() {
	    	this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
	        this.jsonWrapper.setPrettyFormat(true);
	        clientInputHandler = ClientInputHandler.getInstance();
	        menuService = new MenuService();
	        discardItemService = new DiscardItemService();
	    }
	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException, UnknownHostException, IOException {
		ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
    	UserPayload userResponsePayload = getUserDetails(responseWrapper);
    	this.userWrapper = userResponsePayload.getUserWrapperDetails();
		processChefAction(); 
	}

	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper;
	    jsonWrapper = new JsonWrapper<>(UserPayload.class);
	    jsonWrapper.setPrettyFormat(true);
	    UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}
	
	private void processChefAction() throws InvalidArgumentException, UnknownHostException, IOException {
		String actions = "Please choose an option\n"+
                         "1. View Food Menu\n"+
		                 "2. View Top Recommendations\n"+
                         "3. Roll Out Next Day Menu\n"+
		                 "4. View Voted Report\n"+
                         "5. View Discard Menu Item List\n"+
                         "6. Exit\n"+
		                 "Enter your choice: \n";

		String choice="";
        boolean endProcess = false;
        do {
              choice = ConsoleService.getUserInput(actions);
              
            switch (choice) {
                case "1":
                	viewMenu();
                    break;
                case "2":
                	viewTopRecommendation();
                    break;
                case "3":
                	rollOutNextDayMenu();
                    break;
                case "4":
                	viewVotedReport();
                    break;
                case "5":
                	viewDiscardMenu();
                    break;
                case "6":
                	endProcess = true;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println(); 
        } while (!endProcess);

	}
	
	private void viewMenu() throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
    	inputsToProcess.put("Chef:view", "viewMenu");
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	    clientHandler = ClientHandler.getInstance();
	    String datafromServer = clientHandler.getDataFromServer();
	    if(!datafromServer.isBlank()) {
	    	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
	    	final ChefHelper helper = ChefHelper.getInstance();  
	    	UserActionWrapper<Menu> serverResponseWrapper = helper.getMenuResponse(responseWrapper);
	    	helper.viewMenu(serverResponseWrapper.getActionData());
	    }
	}
	
	private void viewTopRecommendation() throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();
    	inputsToProcess.put("Chef:view_recommendation", "viewRecommendation");
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	}

	private void rollOutNextDayMenu() throws InvalidArgumentException {
		UserActionWrapper<Menu> userActionWrapper = new UserActionWrapper<Menu>();
    	String Ids = ConsoleService.getUserInput("Enter the MenuIds for the next day (comma separated): ");
    	inputsToProcess = new Hashtable<>();
		String menuIds[] = Ids.split(",");
		List<Menu> itemsToRolledOut = menuService.getRolledOutItems(menuIds);
		
		userActionWrapper.setActionData(itemsToRolledOut);
		userActionWrapper.setActionToPerform("Chef:roll_out_next_day_menu");
		
        inputsToProcess.put("Chef:roll_out_next_day_menu", userActionWrapper);
        isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
        if(isMenuIdValid) {
    		clientInputHandler.processOperation();
    	}
	}
	
	private void viewVotedReport() throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();
    	inputsToProcess.put("Chef:view_voted_report", "viewVotedReport");
    	clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	}
	
	private void viewDiscardMenu() throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
    	inputsToProcess.put("Chef:view_discard_menu_item_list", "viewDiscardedItem");
    	clientInputHandler.processArguments(inputsToProcess);
    	clientInputHandler.processOperation();
    	clientHandler = ClientHandler.getInstance();
    	String datafromServer = clientHandler.getDataFromServer();
        if(!datafromServer.isBlank()) {
        	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
        	final ChefHelper helper = ChefHelper.getInstance();
        	UserActionWrapper<DiscardItem> serverResponseWrapper = helper.getDiscardItemResponse(responseWrapper);
        	helper.viewDiscardMenu(serverResponseWrapper.getActionData());
        	String discardActions = "Please choose an option\n"+
                    "1. Remove the Food Item from Menu List\n"+
	                    "2. Get Detailed Feedback\n"+
                    "3. Exit\n"+
	                    "Enter your choice: \n";
        	String discardActionChoice;
        	boolean endInnerProcess = false;
        	do {
        		discardActionChoice = ConsoleService.getUserInput(discardActions);
            	switch(discardActionChoice) {
            	case "1":
            		List<DiscardItem> discardedMenuItems = new ArrayList<>();
            		DiscardItem itemToDelete;
            		itemToDelete = discardItemService.getDiscardItem();
            		discardedMenuItems.add(itemToDelete);
            		
            		UserActionWrapper<DiscardItem> userActionWrapper = new UserActionWrapper<DiscardItem>();
            		userActionWrapper.setActionData(discardedMenuItems);
            		userActionWrapper.setUserWrapper(userWrapper);
            		userActionWrapper.setActionToPerform("Chef:discard_item");
            		
            		inputsToProcess.put("Chef:discard_item", userActionWrapper);
            		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
            		if(isMenuIdValid) {
                		clientInputHandler.processOperation();
                	}
            	    break;
            	    
            	case "2":
            		List<DiscardItem> discardItems = new ArrayList<>();
            		DiscardItem discardItem = discardItemService.getDiscardItemFeedback();
            		String chefQuestions = "Q1. What didn't you like about "+ discardItem.getItemName()+" ?\n"
            				               + "Q2. How would you like "+discardItem.getItemName()+" ?\n"
            				               +"Q3. Share your mom’s recipe";                    		
            		discardItem.setChefQuestions(chefQuestions);
            		discardItems.add(discardItem);
            		UserActionWrapper<DiscardItem> userActionWrapperForDiscard = new UserActionWrapper<DiscardItem>();
            		userActionWrapperForDiscard.setActionData(discardItems);
            		userActionWrapperForDiscard.setUserWrapper(userWrapper);
            		userActionWrapperForDiscard.setActionToPerform("Chef:get_detailed_feedback");
            		inputsToProcess.put("Chef:get_detailed_feedback", userActionWrapperForDiscard);
            		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
            		if(isMenuIdValid) {
                		clientInputHandler.processOperation();
                	}
            	    break;
            	    
            	case "3":
            		endInnerProcess = true;
                    System.out.println("Exiting the program...");
                    break;
            	default:
                    System.out.println("Invalid choice. Please try again.");    
            	}
        	}
        	while (!endInnerProcess);
        	
        }
	}
}
