package com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.server.ServerConfiguration;
import com.utility.ActionChoiceConstant;

public class Client{

	public static void main(String... args) throws UnknownHostException, IOException {
		final ClientInputHandler commandHelper = ClientInputHandler.getInstance();
		final String emailId = ConsoleService.getUserInput("Enter the emailId:");
		final String password = ConsoleService.getUserInput("Enter your password:");
		final String inputsToAuthenticate[] = new String[]{ActionChoiceConstant.AUTHENTICATE_USER,emailId,password};
		try {
			commandHelper.processArguments(inputsToAuthenticate);
			commandHelper.processOperation();
		}catch(InvalidArgumentException issue) {
            System.out.println("Argument Issue : "+issue.getLocalizedMessage());
        }
//		Socket client = new Socket(ServerConfiguration.SERVER_IP, ServerConfiguration.PORT);
//		System.out.println("[Client]: Client connected");
//		Scanner input = new Scanner(System.in);
//        System.out.println("Enter your name: ");
//        String name = input.nextLine();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        writer.write(name+"\n");
//        writer.flush();
//        String serverData;
//        while ((serverData = reader.readLine()) != null) {
//            System.out.println("Received from Server: " + serverData);
//           // out.println("Message received: " + clientData);
//        }
//        System.out.println("[CLIENT]: End");
    }
}
