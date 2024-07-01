package com.payload;

import java.util.Hashtable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.VotedItem;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;

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
		System.out.println("In voted Item get payload");
		JsonWrapper<VotedItemPayload> jsonWrapper = new JsonWrapper<>(VotedItemPayload.class);
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
	private VotedItemPayload getVotedItemPayload() {
		// TODO Auto-generated method stub
		System.out.println("In menu get voted item payload");
		VotedItemPayload votedItemPayload = null;
		if(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS == userInput.keySet().toArray()[0]) {
			System.out.println("in if");
			votedItemPayload = new VotedItemPayload();
			votedItemPayload.setVotedItemWrapperDetails((List<VotedItem>)userInput.get(ActionChoiceConstant.EMPLOYEE_VOTE_ITEMS));
			System.out.println(votedItemPayload.getVotedItemWrapperDetails());
			return votedItemPayload;
		}
		return null;
	}
}
