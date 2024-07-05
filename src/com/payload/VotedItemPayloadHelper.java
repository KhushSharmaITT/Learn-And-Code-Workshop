package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.model.VotedItem;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class VotedItemPayloadHelper<T> implements Payload<T> {

	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public VotedItemPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	@Override
	public T getPayload() {
		Type type = new TypeToken<UserActionWrapper<VotedItem>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<VotedItem>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getVotedItemPayload());
			requestWrapper.protocolFormat = ProtocolConstant.JSON;
			requestWrapper.exception = null;
		}
		catch(Exception issue) {
			requestWrapper.jsonString = null;
			requestWrapper.exception = issue;
		}
		System.out.println("32 transmission"+requestWrapper);
		return (T) requestWrapper;
		//return null;
	}
	
	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub
		
	}
	private UserActionWrapper<VotedItem> getVotedItemPayload() {
		// TODO Auto-generated method stub
		System.out.println("In menu get voted item payload");
		if(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<VotedItem>)userInput.get(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS);
		}
		return null;
	}
}
