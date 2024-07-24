package com.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.helper.AdminHelper;
import com.payload.UserPayload;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class AdminController implements Controller {

	private JsonWrapper<ResponseWrapper> jsonWrapper;
	private UserWrapper userWrapper;
	private AdminHelper helper;

	public AdminController() {
		this.jsonWrapper = new JsonWrapper<>(ResponseWrapper.class);
		this.jsonWrapper.setPrettyFormat(true);
		helper = AdminHelper.getInstance();
	}

	@Override
	public void handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException,
			InvalidArgumentException, UnknownHostException, IOException {
		ResponseWrapper responseWrapper = jsonWrapper.convertIntoObject(data);
		UserPayload userResponsePayload = getUserDetails(responseWrapper);
		this.userWrapper = userResponsePayload.getUserWrapperDetails();
		processAdminAction();

	}

	private UserPayload getUserDetails(ResponseWrapper responseWrapper) {
		JsonWrapper<UserPayload> jsonWrapper = new JsonWrapper<>(UserPayload.class);
		jsonWrapper.setPrettyFormat(true);
		UserPayload userResponsePayload = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return userResponsePayload;
	}

	private void processAdminAction() throws InvalidArgumentException, UnknownHostException, IOException {
		String actions = "Please choose an option\n" + "1. Add Item\n" + "2. View Menu\n" + "3. Update Menu\n"
				+ "4. Delete Item\n" + "5. Exit\n" + "Enter your choice: \n";

		String choice = "";
		boolean endProcess = false;
		do {
			choice = ConsoleService.getUserInput(actions);
			switch (choice) {
			case "1":
				add();
				break;
			case "2":
				view();
				break;
			case "3":
				update();
				break;
			case "4":
				delete();
				break;
			case "5":
				endProcess = true;
				System.out.println("Exiting the program...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

			System.out.println();
		} while (!endProcess);

	}

	private void add() throws InvalidArgumentException {
		
		helper.addItemAction(userWrapper);
	}

	private void view() throws InvalidArgumentException, UnknownHostException, IOException {

		helper.viewMenuAction(userWrapper);
	}

	private void update() throws InvalidArgumentException {
		
		helper.updateItemAction(userWrapper);
	}

	private void delete() throws InvalidArgumentException {
		
		helper.deleteItemAction(userWrapper);
	}
}
