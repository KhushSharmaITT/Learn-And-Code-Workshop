package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.ChefRecommendation;
import com.model.DiscardItem;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
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
	public T getPayload() {
		Type type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper = new JsonWrapper<>(type);
		try {
			requestWrapper = new RequestWrapper();
			requestWrapper.jsonString = jsonWrapper.convertIntoJson(getChefRecommendationPayload());
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

	private UserActionWrapper<ChefRecommendation> getChefRecommendationPayload() {
		System.out.println("In menu get menu payload");
		if(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<ChefRecommendation>)userInput.get(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION);
	}
		return null;
	}
	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub
		
	}

}
