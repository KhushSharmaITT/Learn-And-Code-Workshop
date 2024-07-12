package com.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import com.action.Action;
import com.exception.InvalidOperationException;
import com.payload.Payload;
import com.utility.ActionChoiceConstant;
import com.utility.ErrorMessageConstant;

public class PayloadFactory { 
	private static Hashtable<String, String> payloadRegistry = new Hashtable<>();
	
	static {
		payloadRegistry.put(ActionChoiceConstant.AUTHENTICATE_USER,"com.payload.UserPayloadHelper");
		
		payloadRegistry.put(ActionChoiceConstant.ADMIN_ADD,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.ADMIN_VIEW,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.ADMIN_UPDATE,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.ADMIN_DELETE,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_VIEW,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT,"com.payload.MenuPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_MENU,"com.payload.MenuPayloadHelper");
		
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION,"com.payload.ChefRecommendPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS,"com.payload.VotedItemPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_FEEDBACK,"com.payload.FeedbackPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION,"com.payload.NotificationPayloadHelper");
		
		payloadRegistry.put(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST,"com.payload.DiscardItemPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_DISCARD_ITEM,"com.payload.DiscardItemPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK,"com.payload.DiscardItemPayloadHelper");
		payloadRegistry.put(ActionChoiceConstant.EMPLOYEE_UPDATE_PROFILE,"com.payload.UserProfilePayloadHelper");
	}
	
	public static <T> Payload<T> getInstance(String userActionChoice, Hashtable<String, Object> userInput) throws  ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		Payload<T> payload = null;
		String className = payloadRegistry.get(userActionChoice);
			if(className == null || className == "") { //error msg should be changed.
				throw new InvalidOperationException(ErrorMessageConstant.NO_ACTION_FOUND +" : "+userActionChoice);// todo - no action exist.
			}
			Class<?> dipatcherClass = Class.forName(className);
			Constructor<?> constructor = dipatcherClass.getDeclaredConstructor(Hashtable.class);
			constructor.setAccessible(true);
			payload = (Payload<T>) constructor.newInstance(userInput);
        return payload;
    }
}
