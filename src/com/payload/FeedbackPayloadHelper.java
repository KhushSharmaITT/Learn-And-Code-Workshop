package com.payload;

import java.lang.reflect.Type;
import java.util.Hashtable;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.model.Feedback;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class FeedbackPayloadHelper<T> implements Payload<T> {

	private RequestWrapper requestWrapper;
	@Expose
	private ResponseWrapper responseWrapper;
	private Hashtable<String, Object> userInput;
	
	public FeedbackPayloadHelper(Hashtable<String, Object> userInput) {
		this.userInput = userInput;
	}
	@Override
	public T getPayload() {
		Type type = new TypeToken<UserActionWrapper<Feedback>>() {}.getType();
		System.out.println("In menu get payload");
		JsonWrapper<UserActionWrapper<Feedback>> jsonWrapper = new JsonWrapper<>(type);
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
		//return null;
	}

	private UserActionWrapper<Feedback> getMenuPayload() {
		System.out.println("In feedback payload");
		if(ActionChoiceConstant.EMPLOYEE_FEEDBACK == userInput.keySet().toArray()[0]) {
			return (UserActionWrapper<Feedback>)userInput.get(ActionChoiceConstant.EMPLOYEE_FEEDBACK);
		}
		return null;
	}
	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub
		
	}

}
