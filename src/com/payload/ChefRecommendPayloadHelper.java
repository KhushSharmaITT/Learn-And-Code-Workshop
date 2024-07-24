package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.ChefRecommendation;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class ChefRecommendPayloadHelper<T> implements Payload<T> {

	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;

	public ChefRecommendPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}

	@Override
	public T getRequestPayload() {
		Type type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {
		}.getType();
		JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper = new JsonWrapper<>(type);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getChefRecommendationPayload());
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
		Type type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {
		}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		try {
			responseWrapper = new ResponseWrapper();
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getChefRecommendationPayload());
			responseWrapper.protocolFormat = ProtocolConstant.JSON;
			responseWrapper.exception = null;
		} catch (Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission" + requestWrapper);
		return (T) responseWrapper;

	}

	private UserActionWrapper<ChefRecommendation> getChefRecommendationPayload() {
		String userActionChoice = (String) userInput.keySet().toArray()[0];
		return (UserActionWrapper<ChefRecommendation>) userInput.get(userActionChoice);
	}

}
