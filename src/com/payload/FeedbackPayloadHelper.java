package com.payload;

import java.util.Hashtable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.Feedback;
import com.model.Menu;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;

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
		System.out.println("In menu get payload");
		JsonWrapper<FeedbackPayload> jsonWrapper = new JsonWrapper<>(FeedbackPayload.class);
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

	private FeedbackPayload getMenuPayload() {
		System.out.println("In feedback payload");
		FeedbackPayload feedbackPayload = null;
		if(ActionChoiceConstant.EMPLOYEE_FEEDBACK == userInput.keySet().toArray()[0]) {
			feedbackPayload = new FeedbackPayload();
			feedbackPayload.setFeedbackWrapperDetails((List<Feedback>)userInput.get(ActionChoiceConstant.EMPLOYEE_FEEDBACK));
			return feedbackPayload;
		}
		return null;
	}
	@Override
	public void setPayload(T Entity) {
		// TODO Auto-generated method stub
		
	}

}
