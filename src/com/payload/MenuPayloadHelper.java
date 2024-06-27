package com.payload;

import java.util.Hashtable;

import com.google.gson.annotations.Expose;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;

public class MenuPayloadHelper<T> implements Payload<T> {
	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public MenuPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	
	public MenuPayloadHelper() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public T getPayload() {
		System.out.println("In menu get payload");
		JsonWrapper<MenuPayload> jsonWrapper = new JsonWrapper<>(MenuPayload.class);
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
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub

	}
	
	private MenuPayload getMenuPayload() {
		System.out.println("In menu get menu payload");
		MenuPayload menuPayload = null;
		if(ActionChoiceConstant.ADMIN_VIEW == userInput.keySet().toArray()[0] || ActionChoiceConstant.CHEF_VIEW == userInput.keySet().toArray()[0] || ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION == userInput.keySet().toArray()[0]) {
			return null;
		}
		else if(ActionChoiceConstant.ADMIN_ADD == userInput.keySet().toArray()[0]) {
			menuPayload = new MenuPayload();
			menuPayload.setMenuWrapperDetails((Menu)userInput.get(ActionChoiceConstant.ADMIN_ADD));
			return menuPayload;
		}
		else if(ActionChoiceConstant.ADMIN_UPDATE == userInput.keySet().toArray()[0]) {
			menuPayload = new MenuPayload();
			menuPayload.setMenuWrapperDetails((Menu)userInput.get(ActionChoiceConstant.ADMIN_UPDATE));
			return menuPayload;
		}
		else{
			menuPayload = new MenuPayload();
			menuPayload.setMenuWrapperDetails((Menu)userInput.get(ActionChoiceConstant.ADMIN_DELETE));
			return menuPayload;
		}
		
		//return menuPayload;
	}

}
