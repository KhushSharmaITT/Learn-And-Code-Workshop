package com.payload;

import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;

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
		System.out.println("In menu get payload");
		JsonWrapper<NotificationPayload> jsonWrapper = new JsonWrapper<>(NotificationPayload.class);
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
	
	private NotificationPayload getNotificationPayload() {
		NotificationPayload notificationPayload = null;
		if(ActionChoiceConstant.EMPLOYEE_VIEW_NOTIFICATION == userInput.keySet().toArray()[0]) {
			return notificationPayload;
		}
		return null;
	}

}
