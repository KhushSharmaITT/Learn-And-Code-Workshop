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
import com.console.ConsoleService;
import com.exception.InvalidArgumentException;
import com.google.gson.reflect.TypeToken;
import com.model.DiscardItem;
import com.model.Menu;
import com.service.DiscardItemService;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class ChefHelper implements Helper {

	private static ChefHelper chefHelper;
	private Type type;
	private JsonWrapper<UserActionWrapper<Menu>> jsonWrapper;
	private List<Menu> menuItems;
	private List<DiscardItem> discardItems;
	private Hashtable<String, Object> inputsToProcess;
	private ClientInputHandler clientInputHandler;
	private ClientHandler clientHandler;
	private MenuService menuService;
	private boolean isMenuIdValid;
	private DiscardItemService discardItemService;
	private UserActionWrapper<Menu> userActionWrapper;

	public static ChefHelper getInstance() {
		if (chefHelper == null) {
			chefHelper = new ChefHelper();
		}
		return chefHelper;
	}

	private ChefHelper() {
		type = new TypeToken<UserActionWrapper<Menu>>() {
		}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		menuItems = new ArrayList<>();
		discardItems = new ArrayList<>();
		clientInputHandler = ClientInputHandler.getInstance();
		menuService = new MenuService();
		discardItemService = new DiscardItemService();
	}
	
	@Override
	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];

		if (userAction.equals(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU)) {
			UserActionWrapper<Menu> userInputsToValidate = (UserActionWrapper<Menu>) userInput.get(userAction);
			List<Menu> menuItemsToValidate = userInputsToValidate.getActionData();
			Set<Integer> menuIds = prepareMenuIdSet(menuItems);
			Set<String> invalidIds = new HashSet<>();
			for (Menu itemToValidate : menuItemsToValidate) {
				if (!menuIds.contains(itemToValidate.getMenuId())) {
					invalidIds.add(String.valueOf(itemToValidate.getMenuId()));
				}
			}
			if (!invalidIds.isEmpty()) {
				System.out.println("The entered Menu Id " + String.join(",", invalidIds)
						+ " is not present in the above view. Please enter valid Menu Id");
				return false;
			}
			return true;
		} else {
			UserActionWrapper<DiscardItem> userInputsToValidate = (UserActionWrapper<DiscardItem>) userInput
					.get(userAction);
			List<DiscardItem> discardItemsToValidate = userInputsToValidate.getActionData();
			Set<Integer> discardItemIds = prepareDiscardItemIdSet(discardItems);
			for (DiscardItem discardItemToValidate : discardItemsToValidate) {
				if (!discardItemIds.contains(discardItemToValidate.getMenuId())) {
					System.out.println("The entered Menu Id " + discardItemToValidate.getMenuId()
							+ " is not present in the above view. Please enter valid Menu Id ");
					return false;
				}
			}
			return true;
		}

	}
	
	public void viewMenuAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionToPerform("Chef:view");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Chef:view", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
		clientHandler = ClientHandler.getInstance();
		String datafromServer = clientHandler.getDataFromServer();
		if (!datafromServer.isBlank()) {
			ResponseWrapper responseWrapper = jsonResponseWrapper.convertIntoObject(datafromServer);
			//final ChefHelper helper = ChefHelper.getInstance();
			UserActionWrapper<Menu> serverResponseWrapper = getMenuResponse(responseWrapper);
			viewMenu(serverResponseWrapper.getActionData());
		}
	}
	
	public void viewTopRecommendationAction(UserWrapper userWrapper) throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();

		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionToPerform("Chef:view_recommendation");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Chef:view_recommendation", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}
	
	public void rollOutNextDayMenuAction(UserWrapper userWrapper) throws InvalidArgumentException {
		userActionWrapper = new UserActionWrapper<Menu>();
		String Ids = ConsoleService.getUserInput("Enter the MenuIds for the next day (comma separated): ");
		inputsToProcess = new Hashtable<>();
		String menuIds[] = Ids.split(",");
		List<Menu> itemsToRolledOut = menuService.getRolledOutItems(menuIds);

		userActionWrapper.setActionData(itemsToRolledOut);
		userActionWrapper.setActionToPerform("Chef:roll_out_next_day_menu");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Chef:roll_out_next_day_menu", userActionWrapper);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}
	
	public void viewVotedReportAction(UserWrapper userWrapper) throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();

		userActionWrapper = new UserActionWrapper<Menu>();
		userActionWrapper.setActionToPerform("Chef:view_voted_report");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Chef:view_voted_report", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}
	
	public void viewDiscardMenuAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		UserActionWrapper<DiscardItem> userActionWrapper = new UserActionWrapper<DiscardItem>();
		userActionWrapper.setActionToPerform("Chef:view_discard_menu_item_list");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Chef:view_discard_menu_item_list", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
		clientHandler = ClientHandler.getInstance();
		String datafromServer = clientHandler.getDataFromServer();
		if (!datafromServer.isBlank()) {
			ResponseWrapper responseWrapper = jsonResponseWrapper.convertIntoObject(datafromServer);
			UserActionWrapper<DiscardItem> serverResponseWrapper = getDiscardItemResponse(responseWrapper);
			viewDiscardMenu(serverResponseWrapper.getActionData());
			if(!discardItems.isEmpty()){
			String discardActions = "Please choose an option\n" + "1. Remove the Food Item from Menu List\n"
					+ "2. Get Detailed Feedback\n" + "3. Exit\n" + "Enter your choice: \n";
			String discardActionChoice;
			boolean endInnerProcess = false;
			do {
				discardActionChoice = ConsoleService.getUserInput(discardActions);
				switch (discardActionChoice) {
				case "1":
					removeFoodItemAction(userWrapper);
					break;

				case "2":
					detailedFeedbackAction(userWrapper);
					break;

				case "3":
					endInnerProcess = true;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} while (!endInnerProcess);
		 }
			else
				System.out.println("Discard Item List Is Empty");
	  }
	}
	
	private void removeFoodItemAction(UserWrapper userWrapper) throws InvalidArgumentException {
		List<DiscardItem> discardedMenuItems = new ArrayList<>();
		DiscardItem itemToDelete;
		itemToDelete = discardItemService.getDiscardItem();
		discardedMenuItems.add(itemToDelete);

		UserActionWrapper<DiscardItem> userActionWrapper = new UserActionWrapper<DiscardItem>();
		userActionWrapper.setActionData(discardedMenuItems);
		userActionWrapper.setUserWrapper(userWrapper);
		userActionWrapper.setActionToPerform("Chef:discard_item");

		inputsToProcess.put("Chef:discard_item", userActionWrapper);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}
	
	private void detailedFeedbackAction(UserWrapper userWrapper) throws InvalidArgumentException {
		List<DiscardItem> discardItems = new ArrayList<>();
		DiscardItem discardItem = discardItemService.getDiscardItemFeedback();
		String chefQuestions = "Q1. What didn't you like about " + discardItem.getItemName() + " ?\n"
				+ "Q2. How would you like " + discardItem.getItemName() + " ?\n" + "Q3. Share your mom’s recipe";
		discardItem.setChefQuestions(chefQuestions);
		discardItems.add(discardItem);
		UserActionWrapper<DiscardItem> userActionWrapperForDiscard = new UserActionWrapper<DiscardItem>();
		userActionWrapperForDiscard.setActionData(discardItems);
		userActionWrapperForDiscard.setUserWrapper(userWrapper);
		userActionWrapperForDiscard.setActionToPerform("Chef:get_detailed_feedback");
		inputsToProcess.put("Chef:get_detailed_feedback", userActionWrapperForDiscard);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}

	private void viewMenu(List<Menu> menu) {
		menuItems = menu;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability",
				"Score", "Meal Type"));
		result.append("-------------------------------------------------------------------------------------\n");
		for (Menu menuItem : menuItems) {
			result.append(String.format("%-10d %-20s %-10.2f %-20s %-10.2f %-15s%n", menuItem.getMenuId(),
					menuItem.getName(), menuItem.getPrice(), menuItem.getAvailabilityStatus(), menuItem.getScore(),
					menuItem.getMealType()));
		}
		System.out.println(result.toString());
	}

	private Set<Integer> prepareDiscardItemIdSet(List<DiscardItem> discardItems) {
		Set<Integer> discardItemIds = new HashSet<>();
		for (DiscardItem discardItem : discardItems) {
			discardItemIds.add(discardItem.getMenuId());
		}
		return discardItemIds;
	}
	
	private UserActionWrapper<Menu> getMenuResponse(ResponseWrapper responseWrapper) {
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}

	private Set<Integer> prepareMenuIdSet(List<Menu> menuItems2) {
		Set<Integer> menuIds = new HashSet<>();
		for (Menu menuItem : menuItems) {
			menuIds.add(menuItem.getMenuId());
		}
		return menuIds;
	}

	private UserActionWrapper<DiscardItem> getDiscardItemResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<DiscardItem>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		UserActionWrapper<DiscardItem> serverResponseWrapper = jsonWrapper
				.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}

	private void viewDiscardMenu(List<DiscardItem> discardedItems) {
		this.discardItems = discardedItems;
		StringBuilder result = new StringBuilder();
		if(!discardItems.isEmpty()){
			result.append(String.format("%-10s %-20s %-10s%n", "Menu ID", "MenuItem", "Average Rating"));
			result.append("---------------------------------------\n");
			for (DiscardItem discardItem : discardItems) {
				result.append(String.format("%-10d %-20s %-10.2f%n", discardItem.getMenuId(), discardItem.getItemName(),
						discardItem.getAverageRating()));
			}
			System.out.println(result.toString());
		}
		}
	}
