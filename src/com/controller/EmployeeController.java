package com.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.helper.EmployeeHelper;
import com.payload.UserPayload;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class EmployeeController implements Controller {

	private JsonWrapper<ResponseWrapper> jsonWrapper;
	private UserWrapper userWrapper;
	private EmployeeHelper helper;

	public EmployeeController() {
		this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
		this.jsonWrapper.setPrettyFormat(true);
		helper = EmployeeHelper.getInstance();
	}

	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException,
			InvalidArgumentException, UnknownHostException, IOException {
		ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
		UserPayload userResponsePayload = getUserDetails(responseWrapper);
		this.userWrapper = userResponsePayload.getUserWrapperDetails();
		processEmployeeAction();
	}

	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper;
		jsonWrapper = new JsonWrapper<>(UserPayload.class);
		jsonWrapper.setPrettyFormat(true);
		UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}

	private void processEmployeeAction() throws InvalidArgumentException, UnknownHostException, IOException {
		String actions = "Please choose an option\n" + "1. View Notification\n" + "2. View Menu\n"
				+ "3. Update your Profile\n" + "4. View Chef's Recommendation\n"
				+ "5. Vote for Next Day Recommendation\n" + "6. Give Feedback to Chef\n" + "7. Exit\n"
				+ "Enter your choice: \n";

		String choice = "";
		boolean endProcess = false;
		do {
			choice = ConsoleService.getUserInput(actions);
			switch (choice) {
			case "1":
				viewNotification();
				break;
			case "2":
				viewMenu();
				break;
			case "3":
				updateYourProfile();
				break;
			case "4":
				viewChefsRecommendation();
				break;
			case "5":
				voteForNextDayRecommendation();
				break;
			case "6":
				giveFeedbackToChef();
				break;
			case "7":
				endProcess = true;
				System.out.println("Exiting the program...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
			System.out.println();
		} while (!endProcess);
	}

	private void viewNotification() throws InvalidArgumentException {
		helper.viewNotificationAction(userWrapper);
	}

	private void viewMenu() throws InvalidArgumentException, UnknownHostException, IOException {
		helper.viewMenuAction(userWrapper);
	}

	private void updateYourProfile() throws InvalidArgumentException {
        helper.updateYourProfileAction(userWrapper);
	}

	private void viewChefsRecommendation() throws InvalidArgumentException, UnknownHostException, IOException {
		helper.viewChefsRecommendationAction(userWrapper);
	}

	private void voteForNextDayRecommendation() throws InvalidArgumentException {
		helper.voteForNextDayRecommendationAction(userWrapper);
	}

	private void giveFeedbackToChef() throws InvalidArgumentException {
		helper.giveFeedbackToChefAction(userWrapper);
	}
}
