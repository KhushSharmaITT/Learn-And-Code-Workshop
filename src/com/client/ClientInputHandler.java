package com.client;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.exception.DataSerializationException;
import com.exception.InvalidArgumentException;

public class ClientInputHandler {

	private Hashtable<String, String> operations;
	private Hashtable<String,Object> userInput;
    private static ClientInputHandler clientInputHandler;
    private Scanner keyboardInput;

    public static ClientInputHandler getInstance() {
        if(clientInputHandler == null) {
        	clientInputHandler = new ClientInputHandler();
        }
        return clientInputHandler;
    }

    private ClientInputHandler() {
        this.operations = new Hashtable<>();
        keyboardInput = new Scanner(System.in);
        //this.userInput = new String[] {};
    }
    public void processArguments(Hashtable<String, Object> arguments) throws InvalidArgumentException{
    	this.userInput = arguments;
//    	Menu itemMenu = (Menu) arguments.get(ActionChoiceConstant.ADMIN_ADD);
//		System.out.println(itemMenu.getName());
//    	UserWrapper userToAuthentaicate = arguments.get()
//        if(userInput.get <= 0) {
//            throw new InvalidArgumentException("Cannot proceed, no arguments passed");
//        }
//        else if(userInput.length > 0){
//            for(int index = 1; index < userInput.length; index += 1) {
//            	if(userInput[index].trim().isEmpty()) {
//            		throw new InvalidArgumentException("Cannot proceed, no inputs given");
//            	}
//            }
//        }
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
