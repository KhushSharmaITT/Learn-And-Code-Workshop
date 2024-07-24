package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.Notification;
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
	public T getRequestPayload() {
		Type type = new TypeToken<UserActionWrapper<Notification>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<Notification>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getNotificationPayload());
			requestWrapper.protocolFormat = ProtocolConstant.JSON;
			requestWrapper.exception = null;
		} catch (Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission" + requestWrapper);
		return (T) requestWrapper;
	}

	@Override
	public T getResponsePayload() {
		Type type = new TypeToken<UserActionWrapper<Notification>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<Notification>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		try {
			responseWrapper = new ResponseWrapper();
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getNotificationPayload());
			responseWrapper.protocolFormat = ProtocolConstant.JSON;
			responseWrapper.exception = null;
		} catch (Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission" + requestWrapper);
		return (T) responseWrapper;

	}

	private UserActionWrapper<Notification> getNotificationPayload() {
		String userActionChoice = (String) userInput.keySet().toArray()[0];
		return (UserActionWrapper<Notification>) userInput.get(userActionChoice);
	}
}
