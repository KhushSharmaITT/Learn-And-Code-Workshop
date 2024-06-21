package com.controller;

import java.sql.SQLException;

import com.client.ClientInputHandler;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;

public class AdminController implements Controller{

	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, InvalidArgumentException {
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
	    String inputsToProcess[] = getAdminAction();
	    clientInputHandler.processArguments(inputsToProcess);
	    clientInputHandler.processOperation();
	}

	private String[] getAdminAction() {
		String userInputAction[] = null;
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

            // Perform action based on user choice
            switch (choice) {
                case "1":
                	userInputAction = new String[] {"Add_Item","Admin","Database"}; 
                    break;
                case "2":
                	userInputAction = new String[] {"View_Menu","Admin","Database"};
                    break;
                case "3":
                    userInputAction = new String[] {"Update_Menu","Admin","Database"};
                    break;
                case "4":
                	userInputAction = new String[] {"Delete_Item","Admin","Database"};
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
        return userInputAction;
		
	}

}
