package com.controller;

import java.sql.SQLException;
import java.util.Hashtable;

import com.client.ClientInputHandler;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.Menu;
import com.service.MenuService;

public class ChefController implements Controller{

	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException {
		// TODO Auto-generated method stub
		processChefAction();
	}

	private void processChefAction() throws InvalidArgumentException {
		final MenuService menuService = new MenuService();
        final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
        Hashtable<String, Object> inputsToProcess;
		String actions = "Please choose an option\n"+
                         "1. View Food Menu\n"+
		                 "2. View Top Recommendations\n"+
                         "3. Roll Out Next Day Menu\n"+
		                 "4. View Voted Report\n"+
                         "5. Exit\n"+
		                 "Enter your choice: \n";
		
		String choice="";
        boolean endProcess = false;
        do {
              choice = ConsoleService.getUserInput(actions);
              Menu item;
            // Perform action based on user choice
            switch (choice) {
                case "1":
                	inputsToProcess = new Hashtable<String, Object>();
                	inputsToProcess.put("Chef:view", "viewMenu");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
//                	item = menuService.addItem();
//                    inputsToProcess = new Hashtable<String, Object>();
//                	inputsToProcess.put("Admin:add", item);
//                	clientInputHandler.processArguments(inputsToProcess);
//            	    clientInputHandler.processOperation();
                    break;
                case "2":
                	inputsToProcess = new Hashtable<String, Object>();
                	inputsToProcess.put("Chef:view_recommendation", "viewRecommendation");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                	//userInputAction = new String[] {"View_Menu","Admin","Database"};
                    break;
                case "3":
                	item = menuService.updateItem();
                	inputsToProcess = new Hashtable<String, Object>();
                	inputsToProcess.put("Admin:update", item);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                    //userInputAction = new String[] {"Update_Menu","Admin","Database"};
                    break;
                case "4":
                	item = menuService.getDeleteItem();
                	inputsToProcess = new Hashtable<String, Object>();
                	inputsToProcess.put("Admin:delete", item);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                	//userInputAction = new String[] {"Delete_Item","Admin","Database"};
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
		
	}

}
