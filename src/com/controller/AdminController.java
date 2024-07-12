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
import com.helper.AdminHelper;
import com.model.Menu;
import com.payload.UserPayload;
import com.service.MenuService;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class AdminController implements Controller{

	private JsonWrapper<ResponseWrapper> jsonWrapper;
    public AdminController() {
    	this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
    }
	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException, UnknownHostException, IOException {
		ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
    	UserPayload userResponsePayload = getUserDetails(responseWrapper);
		processAdminAction(userResponsePayload.getUserWrapperDetails());

	}
	
	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper;
	    jsonWrapper = new JsonWrapper<>(UserPayload.class);
	    jsonWrapper.setPrettyFormat(true);
	    UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}

	private void processAdminAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		final MenuService menuService = new MenuService();
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
		Hashtable<String, Object> inputsToProcess;
		UserActionWrapper<Menu> userActionWrapper = null;
		List<Menu> menuItems;
		String actions = "Please choose an option\n"+
	                      "1. Add Item\n"+
				          "2. View Menu\n"+
	                      "3. Update Menu\n"+
				          "4. Delete Item\n"+
	                      "5. Exit\n"+
				          "Enter your choice: \n";

        String choice="";
        boolean endProcess = false;
        boolean isMenuIdValid;
        do {
              choice = ConsoleService.getUserInput(actions);
              Menu item;
            // Perform action based on user choice
            switch (choice) {
                case "1":
                	menuItems = new ArrayList<>();
                	item = menuService.addItem();
                	menuItems.add(item);
                	
                	userActionWrapper = new UserActionWrapper<Menu>();
                	userActionWrapper.setActionData(menuItems);
                	userActionWrapper.setActionToPerform("Admin:add");
                	
                    inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:add", userActionWrapper);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
            	    
                    break;
                case "2":
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:view", "viewMenu");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
            	    final ClientHandler clientHandler = ClientHandler.getInstance();
                    String datafromServer = clientHandler.getDataFromServer();
                    if(!datafromServer.isBlank()) {
                    	ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(datafromServer);
                    	final AdminHelper helper = AdminHelper.getInstance();
                    	UserActionWrapper<Menu> serverResponseWrapper = helper.getServerResponse(responseWrapper);
                    	helper.viewMenu(serverResponseWrapper.getActionData());
                    }
                	//userInputAction = new String[] {"View_Menu","Admin","Database"};
                    break;
                case "3":
                	menuItems = new ArrayList<>();
                    item = menuService.updateItem();
                    menuItems.add(item);
                    
                    userActionWrapper = new UserActionWrapper<Menu>();
                	userActionWrapper.setActionData(menuItems);
                	userActionWrapper.setActionToPerform("Admin:update");
                	userActionWrapper.setUserWrapper(userWrapper);
                	
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:update", userActionWrapper);
                	isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
                	if(isMenuIdValid) {
                		clientInputHandler.processOperation();
                	}
            	    //userInputAction = new String[] {"Update_Menu","Admin","Database"};
                    break;
                case "4":
                	menuItems = new ArrayList<>();
                	item = menuService.getDeleteItem();
                	menuItems.add(item);
                	
                	userActionWrapper = new UserActionWrapper<Menu>();
                	userActionWrapper.setActionData(menuItems);
                	userActionWrapper.setActionToPerform("Admin:delete");
                	userActionWrapper.setUserWrapper(userWrapper);
                	
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:delete", userActionWrapper);
                	isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
                	if(isMenuIdValid) {
                		clientInputHandler.processOperation();
                	}
                    break;
                case "5":
                	endProcess = true;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println(); // Print a newline for better readability
        } while (!endProcess);
        //return inputsToProcess;

	}

}
