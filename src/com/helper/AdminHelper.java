package com.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class AdminHelper implements Helper{

	private static AdminHelper adminHelper;
	private Type type;
	private JsonWrapper<UserActionWrapper<Menu>> jsonWrapper;
	private List<Menu> menuItems;
	
	public static AdminHelper getInstance() {
		if(adminHelper == null) {
			adminHelper = new AdminHelper();
		}
		return adminHelper;
	}
	
	private AdminHelper(){
		type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
        menuItems = new ArrayList<>();
	}

	public UserActionWrapper<Menu> getServerResponse(ResponseWrapper responseWrapper) {
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;
	}

	public void viewMenu(List<Menu> menu) {
		menuItems = menu;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability", "Score", "Meal Type"));
		result.append("-------------------------------------------------------------------------------------\n");
		//System.out.println(menuList);
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

	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];
		UserActionWrapper<Menu> userInputsToValidate = (UserActionWrapper<Menu>)userInput.get(userAction);
		List<Menu> menuItemsToValidate = userInputsToValidate.getActionData();
		Set<Integer> menuIds = prepareMenuIdSet(menuItems);
		for(Menu itemToValidate : menuItemsToValidate) {
			if(!menuIds.contains(itemToValidate.getMenuId())) {
				System.out.println("The entered Menu Id "+itemToValidate.getMenuId()+" is not present in the above view. Please enter valid Menu Id ");
				return false;
			}
		}
		return true;
	}

	private Set<Integer> prepareMenuIdSet(List<Menu> menuItems) {
		Set<Integer> menuIds = new HashSet<>();
		for(Menu menuItem : menuItems) {
			menuIds.add(menuItem.getMenuId());
		}
		return menuIds;
	}
	
}
