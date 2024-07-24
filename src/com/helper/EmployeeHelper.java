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
import com.model.ChefRecommendation;
import com.model.Feedback;
import com.model.Menu;
import com.model.Notification;
import com.model.UserProfile;
import com.model.VotedItem;
import com.service.FeedbackService;
import com.service.UserService;
import com.service.VotedItemService;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class EmployeeHelper implements Helper {
	private static EmployeeHelper employeeHelper;
	private List<Menu> menuItems;
	private List<ChefRecommendation> chefRecommendations;
	private Hashtable<String, Object> inputsToProcess;
	private ClientInputHandler clientInputHandler;
	private ClientHandler clientHandler;
	private VotedItemService votedItemService;
	private FeedbackService feedbackService;
	private boolean isMenuIdValid;

	public static EmployeeHelper getInstance() {
		if (employeeHelper == null) {
			employeeHelper = new EmployeeHelper();
		}
		return employeeHelper;
	}

	private EmployeeHelper() {
		menuItems = new ArrayList<>();
		chefRecommendations = new ArrayList<>();
		clientInputHandler = ClientInputHandler.getInstance();
		votedItemService = new VotedItemService();
		feedbackService = new FeedbackService();
	}

	@Override
	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];
		UserActionWrapper<VotedItem> userInputsToValidate = (UserActionWrapper<VotedItem>) userInput.get(userAction);
		List<VotedItem> votedItemsToValidate = userInputsToValidate.getActionData();
		Set<Integer> recommendedMenuIds = prepareMenuIdSet(chefRecommendations);
		Set<String> invalidIds = new HashSet<>();
		for (VotedItem itemToValidate : votedItemsToValidate) {
			if (!recommendedMenuIds.contains(itemToValidate.getMenuId())) {
				invalidIds.add(String.valueOf(itemToValidate.getMenuId()));
			}
		}
		if (!invalidIds.isEmpty()) {
			System.out.println("The entered Menu Id " + String.join(",", invalidIds)
					+ " is not present in the above view. Please enter valid Menu Id");
			return false;
		}
		return true;
	}
	
	public void viewNotificationAction(UserWrapper userWrapper) throws InvalidArgumentException {
		inputsToProcess = new Hashtable<>();

		UserActionWrapper<Notification> userActionWrapper = new UserActionWrapper<>();
		userActionWrapper.setActionToPerform("Employee:view_notification");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Employee:view_notification", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}
	
	public void viewMenuAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		inputsToProcess = new Hashtable<>();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		UserActionWrapper<Menu> userActionWrapper = new UserActionWrapper<>();
		userActionWrapper.setActionToPerform("Employee:view");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Employee:view", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
		clientHandler = ClientHandler.getInstance();
		String datafromServer = clientHandler.getDataFromServer();
		if (!datafromServer.isBlank()) {
			ResponseWrapper responseWrapper = jsonResponseWrapper.convertIntoObject(datafromServer);
			UserActionWrapper<Menu> serverResponseWrapper = getServerResponse(responseWrapper);
			viewMenu(serverResponseWrapper.getActionData());
		}
	}
	
	public void updateYourProfileAction(UserWrapper userWrapper) throws InvalidArgumentException {
		UserActionWrapper<UserProfile> userActionWrapper = new UserActionWrapper<>();
		final UserService userService = new UserService();
		inputsToProcess = new Hashtable<>();
		List<UserProfile> userProfiles = new ArrayList<>();
		UserProfile userProfile = userService.getUserProfile();
		userProfile.setUserId(userWrapper.getId());
		userProfiles.add(userProfile);

		userActionWrapper.setActionData(userProfiles);
		userActionWrapper.setActionToPerform("Employee:updateProfile");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Employee:updateProfile", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}
	
	public void viewChefsRecommendationAction(UserWrapper userWrapper) throws InvalidArgumentException, UnknownHostException, IOException {
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
		jsonResponseWrapper.setPrettyFormat(true);
		inputsToProcess = new Hashtable<>();
		UserActionWrapper<ChefRecommendation> userActionWrapper = new UserActionWrapper<>();
		userActionWrapper.setUserWrapper(userWrapper);
		userActionWrapper.setActionToPerform("Employee:view_chefRecommendations");
		inputsToProcess.put("Employee:view_chefRecommendations", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
		clientHandler = ClientHandler.getInstance();
		String datafromServer = clientHandler.getDataFromServer();
		if (!datafromServer.isBlank()) {
			ResponseWrapper responseWrapper = jsonResponseWrapper.convertIntoObject(datafromServer);
			UserActionWrapper<ChefRecommendation> userActionResponseWrapper = getChefRecommendationResponse(responseWrapper);
			viewChefRecommendation(userActionResponseWrapper.getActionData());
		}

	}
	
	public void voteForNextDayRecommendationAction(UserWrapper userWrapper) throws InvalidArgumentException {
		UserActionWrapper<VotedItem> userActionWrapper = new UserActionWrapper<>();
		inputsToProcess = new Hashtable<>();
		List<VotedItem> votedItems = new ArrayList<>();
		VotedItem itemToVote = votedItemService.getItemToVote();
		itemToVote.setUserId(userWrapper.getId());
		votedItems.add(itemToVote);

		userActionWrapper.setActionData(votedItems);
		userActionWrapper.setActionToPerform("Employee:voteItem");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Employee:voteItem", userActionWrapper);
		isMenuIdValid = clientInputHandler.processArguments(inputsToProcess);
		if (isMenuIdValid) {
			clientInputHandler.processOperation();
		}
	}
	
	public void giveFeedbackToChefAction(UserWrapper userWrapper) throws InvalidArgumentException {
		UserActionWrapper<Feedback> userActionWrapper = new UserActionWrapper<>();
		inputsToProcess = new Hashtable<>();
		List<Feedback> userFeedbacks = new ArrayList<>();
		feedbackService = new FeedbackService();
		Feedback userFeedback = new Feedback();
		userFeedback = feedbackService.getUserFeedback();
		userFeedback.setUserId(userWrapper.getId());
		userFeedbacks.add(userFeedback);

		userActionWrapper.setActionData(userFeedbacks);
		userActionWrapper.setActionToPerform("userFeedbacks");
		userActionWrapper.setUserWrapper(userWrapper);

		inputsToProcess.put("Employee:feedback", userActionWrapper);
		clientInputHandler.processArguments(inputsToProcess);
		clientInputHandler.processOperation();
	}

	private Set<Integer> prepareMenuIdSet(List<ChefRecommendation> chefRecommendations) {
		Set<Integer> recommendedMenuIds = new HashSet<>();
		for (ChefRecommendation recommendedItem : chefRecommendations) {
			recommendedMenuIds.add(recommendedItem.getMenuId());
		}
		return recommendedMenuIds;
	}

	private UserActionWrapper<Menu> getServerResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<Menu>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<Menu>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
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

	private UserActionWrapper<ChefRecommendation> getChefRecommendationResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		UserActionWrapper<ChefRecommendation> serverResponseWrapper = jsonWrapper
				.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}

	private void viewChefRecommendation(List<ChefRecommendation> actionData) {
		chefRecommendations = actionData;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Menu ID", "MenuItem",
				"MealType", "Score", "Preference", "SpiceLevel", "CuisinePreference", "SweetTooth"));
		result.append(
				"------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		for (ChefRecommendation chefRecommendation : chefRecommendations) {
			result.append(String.format("%-10d %-20s %-20s %-20.2f %-20s %-20s %-20s %-20s%n",
					chefRecommendation.getMenuId(), chefRecommendation.getMenuName(), chefRecommendation.getMealType(),
					chefRecommendation.getScore(), chefRecommendation.getPreference(),
					chefRecommendation.getSpiceLevel(), chefRecommendation.getCuisinePreference(),
					chefRecommendation.getSweetTooth()));
		}
		System.out.println(result.toString());
	}

}
