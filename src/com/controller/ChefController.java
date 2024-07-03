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
import com.model.Menu;
import com.service.MenuService;

public class ChefController implements Controller{

	@Override
	public void handleAction(String data)
			throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		processChefAction();
	}

	private void processChefAction() throws InvalidArgumentException, UnknownHostException, IOException {
		final MenuService menuService = new MenuService();
        final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
        Hashtable<String, Object> inputsToProcess;
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
              
            // Perform action based on user choice
            switch (choice) {
                case "1":
                	inputsToProcess = new Hashtable<>();
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
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Chef:view_recommendation", "viewRecommendation");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                	//userInputAction = new String[] {"View_Menu","Admin","Database"};
                    break;
                case "3":
                	//item = menuService.updateItem();
                	String Ids	 = ConsoleService.getUserInput("Enter the MenuIds for the next day (comma separated): ");
                	inputsToProcess = new Hashtable<>();
        			String menuIds[] = Ids.split(",");
        			List<Menu> itemsToRolledOut = menuService.getRolledOutItems(menuIds);
                    inputsToProcess.put("Chef:roll_out_next_day_menu", itemsToRolledOut);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                    //userInputAction = new String[] {"Update_Menu","Admin","Database"};
                    break;
                case "4":
                	//item = menuService.getDeleteItem();
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Chef:view_voted_report", "viewVotedReport");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                    //userInputAction = new String[] {"Delete_Item","Admin","Database"};
                    break;
                case "5":
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Chef:view_discard_menu_item_list", "viewVotedReport");
                	clientInputHandler.processArguments(inputsToProcess);
                	clientInputHandler.processOperation();
                	final ClientHandler clientHandler = ClientHandler.getInstance();
                    String datafromServer = clientHandler.getDataFromServer();
                    if(!datafromServer.isBlank()) {
                    	String discardActions = "Please choose an option\n"+
                                "1. Remove the Food Item from Menu List\n"+
       		                    "2. Get Detailed Feedback\n"+
       		                    "Enter your choice: \n";
                    	String discardActionChoice = ConsoleService.getUserInput(discardActions);
                    	switch(discardActionChoice) {
                    	case "1":
                    		List<Menu> discardedMenuItems = new ArrayList<>();
                    		Menu discardItem;
                    		discardItem = menuService.getDiscardItem();
                    		discardedMenuItems.add(discardItem);
                    		inputsToProcess.put("Chef:discard_item", discardedMenuItems);
                        	clientInputHandler.processArguments(inputsToProcess);
                    	    clientInputHandler.processOperation();
                    	    break;
                    	}
                    }
                    break;
                case "6":
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
