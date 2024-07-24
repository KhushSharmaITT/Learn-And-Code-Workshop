package com.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.helper.ChefHelper;
import com.payload.UserPayload;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class ChefController implements Controller {
	private JsonWrapper<ResponseWrapper> jsonWrapper;
	private UserWrapper userWrapper;
	private ChefHelper helper;

	public ChefController() {
		this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
		this.jsonWrapper.setPrettyFormat(true);
		helper = ChefHelper.getInstance();
	}

	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException,
			InvalidArgumentException, UnknownHostException, IOException {
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
		String actions = "Please choose an option\n" + "1. View Food Menu\n" + "2. View Top Recommendations\n"
				+ "3. Roll Out Next Day Menu\n" + "4. View Voted Report\n" + "5. View Discard Menu Item List\n"
				+ "6. Exit\n" + "Enter your choice: \n";

		String choice = "";
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
		helper.viewMenuAction(userWrapper);
	}

	private void viewTopRecommendation() throws InvalidArgumentException {
		helper.viewTopRecommendationAction(userWrapper);
	}

	private void rollOutNextDayMenu() throws InvalidArgumentException {
		helper.rollOutNextDayMenuAction(userWrapper);
	}

	private void viewVotedReport() throws InvalidArgumentException {
		helper.viewVotedReportAction(userWrapper);
	}

	private void viewDiscardMenu() throws InvalidArgumentException, UnknownHostException, IOException {
		helper.viewDiscardMenuAction(userWrapper);
	}
}
