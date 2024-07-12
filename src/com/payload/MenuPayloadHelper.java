package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class MenuPayloadHelper<T> implements Payload<T> {
	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;

	public MenuPayloadHelper(Hashtable<String, Object> userInput) {  // pass only the useractionwrapper object in constructor.
		this.userInput = userInput;
	}

	public MenuPayloadHelper() {
		// TODO Auto-generated constructor stub 
	}

	@Override
	public T getRequestPayload() {
		Type type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<Menu>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getMenuPayload());
			requestWrapper.protocolFormat = ProtocolConstant.JSON;
			requestWrapper.exception = null;
		}
		catch(Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission"+requestWrapper);
		return (T) requestWrapper;
		// TODO Auto-generated method stub
	}

	@Override
	public T getResponsePayload() {
		Type type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<Menu>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			responseWrapper = new ResponseWrapper();
			responseWrapper.jsonString = jsonWrapper.convertIntoJson(getMenuPayload());
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

	private UserActionWrapper<Menu> getMenuPayload() {
		System.out.println("In menu get menu payload");
		//MenuPayload menuPayload = null;
		if(ActionChoiceConstant.ADMIN_VIEW == userInput.keySet().toArray()[0] || ActionChoiceConstant.CHEF_VIEW == userInput.keySet().toArray()[0] || ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION == userInput.keySet().toArray()[0] || ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT == userInput.keySet().toArray()[0]) {
			//this needs to change.
			return null;
		}
		else if(ActionChoiceConstant.ADMIN_VIEW_RESPONSE == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<Menu>)userInput.get(ActionChoiceConstant.ADMIN_VIEW_RESPONSE);
		}
		else if(ActionChoiceConstant.ADMIN_ADD == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<Menu>)userInput.get(ActionChoiceConstant.ADMIN_ADD);
		}
		else if(ActionChoiceConstant.ADMIN_UPDATE == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<Menu>)userInput.get(ActionChoiceConstant.ADMIN_UPDATE);
		}
		else if(ActionChoiceConstant.ADMIN_DELETE == userInput.keySet().toArray()[0]){
			return (UserActionWrapper<Menu>)userInput.get(ActionChoiceConstant.ADMIN_DELETE);
		} 
		else
			return (UserActionWrapper<Menu>)userInput.get(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU);
		//return menuPayload;
	}

}
