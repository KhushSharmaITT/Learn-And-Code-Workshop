package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.DiscardItem;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class DiscardItemFeedbackPayloadHelper<T> implements Payload<T> {
	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public DiscardItemFeedbackPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	
	@Override
	public T getRequestPayload() {
		Type type = new TypeToken<UserActionWrapper<DiscardItem>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getDiscardItemPayload());
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
		Type type = new TypeToken<UserActionWrapper<DiscardItem>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			responseWrapper = new ResponseWrapper();
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getDiscardItemPayload());
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

	private UserActionWrapper<DiscardItem> getDiscardItemPayload() {
//		if(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST == userInput.keySet().toArray()[0]) {
//			return null;
//		}
		if(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<DiscardItem>)userInput.get(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK);
		}
		return null;
		
	}
}
