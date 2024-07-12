package com.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import com.model.DiscardItem;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class ChefHelper implements Helper{

	private static ChefHelper chefHelper;
	private Type type;
	private JsonWrapper<UserActionWrapper<Menu>> jsonWrapper;
	private List<Menu> menuItems;
	private List<DiscardItem> discardItems;
	public static ChefHelper getInstance() {
		if(chefHelper == null) {
			chefHelper = new ChefHelper();
		}
		return chefHelper;
	}
	
	private ChefHelper(){
		type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
        menuItems = new ArrayList<>();
        discardItems = new ArrayList<>();
	}

	public UserActionWrapper<Menu> getMenuResponse(ResponseWrapper responseWrapper) {
		UserActionWrapper<Menu> serverResponseWrapper = jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;//lets see
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

	@Override
	public boolean validateInput(Hashtable<String, Object> userInput) {
		String userAction = (String) userInput.keySet().toArray()[0];
		
		if(userAction.equals(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU)) {
			UserActionWrapper<Menu> userInputsToValidate = (UserActionWrapper<Menu>)userInput.get(userAction);
			List<Menu> menuItemsToValidate = userInputsToValidate.getActionData();
			Set<Integer> menuIds = prepareMenuIdSet(menuItems);
			Set<String> invalidIds = new HashSet<>();
			for(Menu itemToValidate : menuItemsToValidate) {
				if(!menuIds.contains(itemToValidate.getMenuId())) {
					invalidIds.add(String.valueOf(itemToValidate.getMenuId()));
				}
			}
			if(!invalidIds.isEmpty()) {
				System.out.println("The entered Menu Id "+String.join(",",invalidIds)+" is not present in the above view. Please enter valid Menu Id");
				return false;
			}
			return true;
		}
		else {
			UserActionWrapper<DiscardItem> userInputsToValidate = (UserActionWrapper<DiscardItem>)userInput.get(userAction);
			List<DiscardItem> discardItemsToValidate = userInputsToValidate.getActionData();
			Set<Integer> discardItemIds = prepareDiscardItemIdSet(discardItems);
			for(DiscardItem discardItemToValidate : discardItemsToValidate) {
				if(!discardItemIds.contains(discardItemToValidate.getMenuId())) {
					System.out.println("The entered Menu Id "+discardItemToValidate.getMenuId()+" is not present in the above view. Please enter valid Menu Id ");
					return false;
				}
			}
			return true;
		}
		
	}

	private Set<Integer> prepareDiscardItemIdSet(List<DiscardItem> discardItems) {
		Set<Integer> discardItemIds = new HashSet<>();
		for(DiscardItem discardItem : discardItems) {
			discardItemIds.add(discardItem.getMenuId());
		}
		return discardItemIds;
	}

	private Set<Integer> prepareMenuIdSet(List<Menu> menuItems2) {
		Set<Integer> menuIds = new HashSet<>();
		for(Menu menuItem : menuItems) {
			menuIds.add(menuItem.getMenuId());
		}
		return menuIds;
	}

	public UserActionWrapper<DiscardItem> getDiscardItemResponse(ResponseWrapper responseWrapper) {
		Type type = new TypeToken<UserActionWrapper<DiscardItem>>() {}.getType();
		JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		UserActionWrapper<DiscardItem> serverResponseWrapper =  jsonWrapper.convertIntoObject(responseWrapper.jsonString);
		return serverResponseWrapper;//lets see
	}

	public void viewDiscardMenu(List<DiscardItem> discardedItems) {
		this.discardItems = discardedItems;
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s%n", "Menu ID", "MenuItem", "Average Rating"));
		result.append("---------------------------------------\n");
		//System.out.println(menuList);
		for (DiscardItem discardItem : discardItems) {
	        result.append(String.format("%-10d %-20s %-10.2f%n",
	        		discardItem.getMenuId(),
	        		discardItem.getItemName(),
	        		discardItem.getAverageRating()
	        ));
	    }
		System.out.println(result.toString());
		
	}

}
