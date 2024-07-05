package com.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import com.action.Action;
import com.exception.InvalidOperationException;
import com.utility.ActionChoiceConstant;
import com.utility.ErrorMessageConstant;

public class UserActionFactory {
	private static Hashtable<String, String> actionRegistry = new Hashtable<>();

	static {
		actionRegistry.put(ActionChoiceConstant.AUTHENTICATE_USER,"com.action.AuthenticateUserAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_ADD,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_VIEW,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_UPDATE,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_DELETE,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_MENU,"com.action.ManageAdminAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION,"com.action.ManageEmployeeAction");
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS,"com.action.ManageEmployeeAction"); 
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_FEEDBACK,"com.action.ManageEmployeeAction");
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION,"com.action.ManageEmployeeAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_DISCARD_ITEM,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK,"com.action.ManageChefAction");
		actionRegistry.put(ActionChoiceConstant.EMPLOYEE_UPDATE_PROFILE,"com.action.ManageEmployeeAction");
	}
	// argument name should be changed.
	public static Action getInstance(String protocolFormat) throws  ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		Action action = null;
		String className = actionRegistry.get(protocolFormat);
			if(className == null || className == "") { //error msg should be changed.
				throw new InvalidOperationException(ErrorMessageConstant.NO_CONTROLLER_EXISTS +" : "+protocolFormat);// todo - no action exist.
			}
			Class<?> dipatcherClass = Class.forName(className);
			Constructor<?> constructor = dipatcherClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			action = (Action) constructor.newInstance();
        return action;
    }
}
