package com.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.utility.ActionChoiceConstant;
import com.utility.user.UserWrapper;

public class Client{

	public static void main(String... args) throws UnknownHostException, IOException {
		final ClientInputHandler clientInputHandler = ClientInputHandler.getInstance();
		UserWrapper userWrapper = new UserWrapper();
		userWrapper.setEmailId(ConsoleService.getUserInput("Enter the emailId:"));
		userWrapper.setPassword(ConsoleService.getUserInput("Enter your password:"));
		final Hashtable<String, Object> inputsToAuthenticate = new Hashtable<>();
		inputsToAuthenticate.put(ActionChoiceConstant.AUTHENTICATE_USER, userWrapper);

		try {
			clientInputHandler.processArguments(inputsToAuthenticate);
			clientInputHandler.processOperation();
		}catch(InvalidArgumentException issue) {
            System.out.println("Argument Issue : "+issue.getLocalizedMessage()); 
        }
    }
}
