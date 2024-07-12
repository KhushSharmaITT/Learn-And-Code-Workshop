package com.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.model.DiscardItem;
import com.model.Menu;
import com.utility.core.JsonWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class EmployeeHelper implements Helper {
	private static EmployeeHelper employeeHelper;
	private List<Menu> menuItems;
	
	public static EmployeeHelper getInstance() {
		if(employeeHelper == null) {
			employeeHelper = new EmployeeHelper();
		}
		return employeeHelper;
	}
	
	private EmployeeHelper(){
        menuItems = new ArrayList<>();
	}
	@Override
	public boolean validateInput(Hashtable<String, Object> arguments) {
		
		return false;
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

}
