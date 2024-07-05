package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.model.Notification;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class NotificationPayloadHelper<T> implements Payload<T> {
	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public NotificationPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	@Override
	public T getPayload() {
		Type type = new TypeToken<UserActionWrapper<Notification>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<Notification>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getNotificationPayload());
			requestWrapper.protocolFormat = ProtocolConstant.JSON;
			requestWrapper.exception = null;
		}
		catch(Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission"+requestWrapper);
		return (T) requestWrapper;
	}

	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub

	}
	
	private UserActionWrapper<Notification> getNotificationPayload() {
		if(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION == userInput.keySet().toArray()[0]) {
			return null;
		}
		return null;
	}

}
