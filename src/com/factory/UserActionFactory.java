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
		actionRegistry.put(ActionChoiceConstant.ADMIN_ADD,"com.action.ManageMenuAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_VIEW,"com.action.ManageMenuAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_UPDATE,"com.action.ManageMenuAction");
		actionRegistry.put(ActionChoiceConstant.ADMIN_DELETE,"com.action.ManageMenuAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW,"com.action.ManageMenuAction");
		actionRegistry.put(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION,"com.action.MenuRecommendationAction");
		}
	// argument name should be changed.
	public static Action getInstance(String protocolFormat) throws  ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		Action action = null;
		String className = actionRegistry.get(protocolFormat);
			if(className == null || className == "") {
				throw new InvalidOperationException(ErrorMessageConstant.NO_CONTROLLER_EXISTS +" : "+protocolFormat);// todo - no action exist.
			}
			Class<?> dipatcherClass = Class.forName(className);
			Constructor<?> constructor = dipatcherClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			action = (Action) constructor.newInstance();  
        return action;
    }
}
