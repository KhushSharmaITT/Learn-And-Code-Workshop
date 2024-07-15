package com.payload;

import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class UserPayloadHelper<T> implements Payload<T>{

	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	public UserPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}

	@Override 
	public T getRequestPayload() {
		JsonWrapper<UserPayload> jsonWrapper = new JsonWrapper<>(UserPayload.class);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getUserPayload());
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
		JsonWrapper<UserPayload> jsonWrapper = new JsonWrapper<>(UserPayload.class);
        jsonWrapper.setPrettyFormat(true);
		responseWrapper = new ResponseWrapper();
		try {
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getUserPayload());
			responseWrapper.protocolFormat = ProtocolConstant.JSON;
			responseWrapper.exception = null;
		}
		catch(Exception issue) {
			responseWrapper.jsonString = null;
			responseWrapper.exception = issue;
		}
		return (T) responseWrapper;

	}

	private UserPayload getUserPayload() {
		UserPayload userPayload = new UserPayload();
		userPayload.setUserWrapperDetails((UserWrapper)userInput.get(ActionChoiceConstant.AUTHENTICATE_USER));
		return userPayload;
	}

}
