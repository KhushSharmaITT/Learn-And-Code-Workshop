package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.UserProfile;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class UserProfilePayloadHelper<T> implements Payload<T> {

	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public UserProfilePayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	@Override
	public T getRequestPayload() {
		Type type = new TypeToken<UserActionWrapper<UserProfile>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<UserProfile>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getUserProfilePayload());
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
	public T getResponsePayload() {
		Type type = new TypeToken<UserActionWrapper<UserProfile>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<UserProfile>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			responseWrapper = new ResponseWrapper();
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getUserProfilePayload());
			responseWrapper.protocolFormat = ProtocolConstant.JSON;
			responseWrapper.exception = null;
		}
		catch(Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission"+requestWrapper);
		return (T) responseWrapper;
	}
	
	private UserActionWrapper<UserProfile> getUserProfilePayload() {
		System.out.println("In menu get menu payload");
		 if(ActionChoiceConstant.EMPLOYEE_UPDATE_PROFILE == userInput.keySet().toArray()[0]) {
			 return (UserActionWrapper<UserProfile>)userInput.get(ActionChoiceConstant.EMPLOYEE_UPDATE_PROFILE);
		}
		return null;
	}

}
