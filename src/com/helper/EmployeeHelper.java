package com.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import com.model.ChefRecommendation;
import com.model.Menu;
import com.model.VotedItem;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class EmployeeHelper implements Helper {
	private static EmployeeHelper employeeHelper;
	private List<Menu> menuItems;
	private List<ChefRecommendation> chefRecommendations;
	
	public static EmployeeHelper getInstance() {
		if(employeeHelper == null) {
			employeeHelper = new EmployeeHelper();
		}
		return employeeHelper;
	}
	
	private EmployeeHelper(){
        menuItems = new ArrayList<>();
        chefRecommendations = new ArrayList<>();
	}
	@Override
	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];
		UserActionWrapper<VotedItem> userInputsToValidate = (UserActionWrapper<VotedItem>)userInput.get(userAction);
		List<VotedItem> votedItemsToValidate = userInputsToValidate.getActionData();
		Set<Integer> recommendedMenuIds = prepareMenuIdSet(chefRecommendations);
		Set<String> invalidIds = new HashSet<>();
		for(VotedItem itemToValidate : votedItemsToValidate) {
			if(!recommendedMenuIds.contains(itemToValidate.getMenuId())) {
				invalidIds.add(String.valueOf(itemToValidate.getMenuId()));
			}
		}
		if(!invalidIds.isEmpty()) {
			System.out.println("The entered Menu Id "+String.join(",",invalidIds)+" is not present in the above view. Please enter valid Menu Id");
			return false;
		}
		return true;
	}

	private Set<Integer> prepareMenuIdSet(List<ChefRecommendation> chefRecommendations) {
		Set<Integer> recommendedMenuIds = new HashSet<>();
		for(ChefRecommendation recommendedItem : chefRecommendations) {
			recommendedMenuIds.add(recommendedItem.getMenuId());
		}
		return recommendedMenuIds;
	}

	public UserActionWrapper<Menu> getServerResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		JsonWrapper<UserActionWrapper<Menu>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}
	
	public void viewMenu(List<Menu> menu) {
		menuItems = menu;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability", "Score", "Meal Type"));
		result.append("-------------------------------------------------------------------------------------\n");
		for (Menu menuItem : menuItems) {
	        result.append(String.format("%-10d %-20s %-10.2f %-20s %-10.2f %-15s%n",
	        menuItem.getMenuId(),
	        menuItem.getName(),
	        menuItem.getPrice(),
	        menuItem.getAvailabilityStatus(),
	        menuItem.getScore(),
	        menuItem.getMealType()
	        ));
	    }
		System.out.println(result.toString());
	}

	public UserActionWrapper<ChefRecommendation> getChefRecommendationResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {}.getType();
		JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
        UserActionWrapper<ChefRecommendation> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}

	public void viewChefRecommendation(List<ChefRecommendation> actionData) {
		chefRecommendations = actionData;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Menu ID", "MenuItem", "MealType", "Score", "Preference", "SpiceLevel", "CuisinePreference", "SweetTooth"));
		result.append("------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		for (ChefRecommendation chefRecommendation : chefRecommendations) {
	        result.append(String.format("%-10d %-20s %-20s %-20.2f %-20s %-20s %-20s %-20s%n",
	            chefRecommendation.getMenuId(),
	            chefRecommendation.getMenuName(),
	            chefRecommendation.getMealType(),
	            chefRecommendation.getScore(),
	            chefRecommendation.getPreference(),
	            chefRecommendation.getSpiceLevel(),
	            chefRecommendation.getCuisinePreference(),
	            chefRecommendation.getSweetTooth()
	        ));
	    }
		System.out.println(result.toString());
	}

}
