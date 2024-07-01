package com.payload;

import java.util.Hashtable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.ChefRecommendation;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;

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
		System.out.println("In menu get payload");
		JsonWrapper<ChefRecommendPayload> jsonWrapper = new JsonWrapper<>(ChefRecommendPayload.class);
        jsonWrapper.setPrettyFormat(true);
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

	private ChefRecommendPayload getChefRecommendationPayload() {
		System.out.println("In menu get menu payload");
		ChefRecommendPayload chefRecommendPayload = null;
		if(ActionChoiceConstant.EMPLOYEE_VIEW_CHEF_RECOMMENDATION == userInput.keySet().toArray()[0]) {
			return null;
//			chefRecommendPayload = new ChefRecommendPayload();
//			chefRecommendPayload.setRecommendWrappersDetails((List<ChefRecommendation>)userInput.get(ActionChoiceConstant.ADMIN_ADD));
//			return chefRecommendPayload;
		//return null;
	}
		return null;
	}
	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub
		
	}

}
