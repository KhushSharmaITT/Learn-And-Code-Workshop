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
import com.model.Menu;
import com.service.MenuService;

public class AdminController implements Controller{

	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException {

		getAdminAction();

	}

	private void getAdminAction() throws InvalidArgumentException {
		final MenuService menuService = new MenuService();
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
		Hashtable<String, Object> inputsToProcess;
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
        do {
              choice = ConsoleService.getUserInput(actions);
              Menu item;
            // Perform action based on user choice
            switch (choice) {
                case "1":
                	menuItems = new ArrayList<>();
                	item = menuService.addItem();
                	menuItems.add(item);
                    inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:add", menuItems);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                    break;
                case "2":
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:view", "viewMenu");
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                	//userInputAction = new String[] {"View_Menu","Admin","Database"};
                    break;
                case "3":
                	menuItems = new ArrayList<>();
                    item = menuService.updateItem();
                    menuItems.add(item);
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:update", menuItems);
                	clientInputHandler.processArguments(inputsToProcess);
            	    clientInputHandler.processOperation();
                    //userInputAction = new String[] {"Update_Menu","Admin","Database"};
                    break;
                case "4":
                	menuItems = new ArrayList<>();
                	item = menuService.getDeleteItem();
                	menuItems.add(item);
                	inputsToProcess = new Hashtable<>();
                	inputsToProcess.put("Admin:delete", menuItems);
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
        //return inputsToProcess;

	}

}
