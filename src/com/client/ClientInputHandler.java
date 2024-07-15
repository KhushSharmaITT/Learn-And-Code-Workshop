package com.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.exception.DataSerializationException;
import com.exception.InvalidArgumentException;
import com.factory.HelperFactory;
import com.helper.Helper;
import com.utility.ActionChoiceConstant;

public class ClientInputHandler {

	private Hashtable<String,Object> userInput;
    private static ClientInputHandler clientInputHandler;
    private Set<String> actionsToValidate = new HashSet<String>(Arrays.asList(
    		ActionChoiceConstant.ADMIN_DELETE, ActionChoiceConstant.ADMIN_UPDATE,
    		ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU,ActionChoiceConstant.CHEF_DISCARD_ITEM,
    		ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK, ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS
    		));
    public static ClientInputHandler getInstance() {
        if(clientInputHandler == null) {
        	clientInputHandler = new ClientInputHandler();
        }
        return clientInputHandler;
    }
    public boolean processArguments(Hashtable<String, Object> arguments) throws InvalidArgumentException{
    	this.userInput = arguments;
    	boolean isInputValid = true;
    	if(actionsToValidate.contains(userInput.keySet().toArray()[0])) {
    		String role = ((String) userInput.keySet().toArray()[0]).split(":")[0];
    		final Helper helper = HelperFactory.getInstance(role);
    		isInputValid = helper.validateInput(userInput);
    		return isInputValid;
    	}
    	return isInputValid;
    }

    public void processOperation() {
        try {
            final ClientHandler clientHandler = ClientHandler.getInstance();
            clientHandler.startClient();
        } catch (IOException issue) {
            System.out.println("Failed to start client : "+issue.getLocalizedMessage());
        } catch (ParseException|InvalidArgumentException|DataSerializationException issue) {
            System.out.println("Data invalid : "+issue.getLocalizedMessage());
        }catch (Exception issue) {
        	//issue.printStackTrace();
            System.out.println("Failed to start client : "+issue.getLocalizedMessage());
        }
    } 

    public Hashtable<String,Object> getInputArguments() {

        return userInput;
    }

	public String getActionName() {
		return (String) userInput.keySet().toArray()[0];
	}
}
