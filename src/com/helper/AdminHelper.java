package com.helper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.client.ClientHandler;
import com.client.ClientInputHandler;
import com.exception.InvalidArgumentException;
import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.service.MenuService;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class AdminHelper implements Helper {

	private List<Menu> menuItemsToProcess;
	private MenuService menuService;
	private UserActionWrapper<Menu> userActionWrapper;
	private Hashtable<String, Object> inputsToProcess;
	private ClientInputHandler clientInputHandler;
	//private UserWrapper userWrapper;
	private boolean isMenuIdValid;
	private static AdminHelper adminHelper;
	private Type type;
	private JsonWrapper<UserActionWrapper<Menu>> jsonWrapper;
	private List<Menu> menuItems;

	public static AdminHelper getInstance() {
		if (adminHelper == null) {
			adminHelper = new AdminHelper();
		}
		return adminHelper;
	}

	private AdminHelper() {
		type = new TypeToken<UserActionWrapper<Menu>>() {
		}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		menuItems = new ArrayList<>();
		menuService = new MenuService();
		clientInputHandler = ClientInputHandler.getInstance();
	}

	private UserActionWrapper<Menu> getServerResponse(ResponseWrapper responseWrapper) {
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}
	
	public void addItemAction(UserWrapper userWrapper) throws InvalidArgumentException {

		menuItemsToProcess = new ArrayList<>();
		Menu item = menuService.addItem();
		menuItemsToProcess.add(item);

		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionData(menuItemsToProcess);
		userActionWrapper.setActionToPerform("Admin:add");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess = new Hashtable<>();
		inputsToProcess.put("Admin:add", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}
	
	public void viewMenuAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {

		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionToPerform("Admin:view");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess = new Hashtable<>();
		inputsToProcess.put("Admin:view", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
		final ClientHandler clientHandler = ClientHandler.getInstance();
		String datafromServer = clientHandler.getDataFromServer();
		if (!datafromServer.isBlank()) {
			ResponseWrapper responseWrapper = jsonResponseWrapper.convertIntoObject(datafromServer);
			UserActionWrapper<Menu> serverResponseWrapper = getServerResponse(responseWrapper);
		    viewMenu(serverResponseWrapper.getActionData());
		}
	}
	
	public void updateItemAction(UserWrapper userWrapper) throws InvalidArgumentException {
		menuItemsToProcess = new ArrayList<>();
		Menu item = menuService.updateItem();
		menuItemsToProcess.add(item);

		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionData(menuItemsToProcess);
		userActionWrapper.setActionToPerform("Admin:update");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess = new Hashtable<>();
		inputsToProcess.put("Admin:update", userActionWrapper);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}
	
	public void deleteItemAction(UserWrapper userWrapper) throws InvalidArgumentException {
		menuItemsToProcess = new ArrayList<>();
		Menu item = menuService.getDeleteItem();
		menuItemsToProcess.add(item);

		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionData(menuItemsToProcess);
		userActionWrapper.setActionToPerform("Admin:delete");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess = new Hashtable<>();
		inputsToProcess.put("Admin:delete", userActionWrapper);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}

	private void viewMenu(List<Menu> menu) {
		menuItems = menu;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s %-23s %-20s %-23s %-15s%n", "Menu ID", "Name",
				"Price", "Availability", "Score", "Meal Type", "Vegetarian Preference", "Spice Level",
				"Cuisine Preference", "Sweet Tooth"));
		result.append(
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		for (Menu menuItem : menuItems) {
			result.append(String.format("%-10d %-20s %-10.2f %-20s %-10.2f %-15s %-23s %-20s %-23s %-15s%n",
					menuItem.getMenuId(), menuItem.getName(), menuItem.getPrice(), menuItem.getAvailabilityStatus(),
					menuItem.getScore(), menuItem.getMealType(), menuItem.getPreference(), menuItem.getSpiceLevel(),
					menuItem.getCuisinePreference(), menuItem.getSweetTooth()));
		}
		System.out.println(result.toString());
	}

	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];
		UserActionWrapper<Menu> userInputsToValidate = (UserActionWrapper<Menu>) userInput.get(userAction);
		List<Menu> menuItemsToValidate = userInputsToValidate.getActionData();
		Set<Integer> menuIds = prepareMenuIdSet(menuItems);
		for (Menu itemToValidate : menuItemsToValidate) {
			if (!menuIds.contains(itemToValidate.getMenuId())) {
				System.out.println("The entered Menu Id " + itemToValidate.getMenuId()
						+ " is not present in the above view. Please enter valid Menu Id ");
				return false;
			}
		}
		return true;
	}

	private Set<Integer> prepareMenuIdSet(List<Menu> menuItems) {
		Set<Integer> menuIds = new HashSet<>();
		for (Menu menuItem : menuItems) {
			menuIds.add(menuItem.getMenuId());
		}
		return menuIds;
	}

}
