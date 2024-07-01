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

	public UserPayloadHelper() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public T getPayload() {
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
	public void setPayload(T Entity) {
		UserPayload userResponsePayload = (UserPayload)Entity;
		JsonWrapper<UserPayload> jsonWrapper = new JsonWrapper<>(UserPayload.class);
        jsonWrapper.setPrettyFormat(true);
		System.out.println("IN setPayload");
		System.out.println(userResponsePayload.getUserWrapperDetails().getRole());
		responseWrapper = new ResponseWrapper();
		try {
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(userResponsePayload);
			responseWrapper.protocolFormat = ProtocolConstant.JSON;
			responseWrapper.exception = null;
		}
		catch(Exception issue) {
			responseWrapper.jsonString = null;
			responseWrapper.exception = issue;
		}
		System.out.println("32 transmission "+responseWrapper.jsonString);
		System.out.println("32 transmission "+responseWrapper.protocolFormat);

	}
	public ResponseWrapper getResponseWrapper() {
		return this.responseWrapper;
	}

	private UserPayload getUserPayload() {
		UserPayload userPayload = new UserPayload();
		userPayload.setUserWrapperDetails((UserWrapper)userInput.get(ActionChoiceConstant.AUTHENTICATE_USER));
		return userPayload;
	}

}
