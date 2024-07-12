package com.client;

import java.util.Hashtable;

import com.utility.ProtocolConstant;
import com.utility.core.CommunicationProtocol;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;

public class ClientPayloadService {

	CommunicationProtocol communicationProtocol;
	private Hashtable<String, String> requestDetails;
	private JsonWrapper<RequestWrapper> jsonWrapper;
	public ClientPayloadService(Hashtable<String, String> requestDetails) {
		communicationProtocol = new CommunicationProtocol();
		this.requestDetails = requestDetails;
		jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        jsonWrapper.setPrettyFormat(true);
}
	public CommunicationProtocol createRequestCommunicationProtocol(RequestWrapper requestWrapper) {
		//System.out.println("------>"+"in createRequestProtocol");
		final String data = jsonWrapper.convertIntoJson(requestWrapper);
		//System.out.println("client service"+data);
		communicationProtocol.setData(data);
		//System.out.println("------>"+"24");
		communicationProtocol.setProtocolFormat(requestDetails.get(ProtocolConstant.PROTOCOL_FORMAT));
		//System.out.println("------>"+"26");
		communicationProtocol.setProtocolType(ProtocolConstant.REQUEST);
		//System.out.println("------>"+"28");
		communicationProtocol.setHeaders(prepareHeaders());
		//System.out.println("------>"+"30");
		if(requestWrapper.exception != null) {
		    communicationProtocol.setErrorMessage(requestWrapper.exception.getLocalizedMessage());
        }
		System.out.println("------>"+"31");
		return communicationProtocol;
	} 

	public CommunicationProtocol createResponseCommunicationProtocol(String data) {
		System.out.println("------>"+"in createResponseCommunicationProtocol");
		//System.out.println("client service"+data);
		communicationProtocol.setData(data);
		//System.out.println("------>"+"24");
		communicationProtocol.setProtocolFormat(requestDetails.get(ProtocolConstant.PROTOCOL_FORMAT));
		//System.out.println("------>"+"26");
		communicationProtocol.setProtocolType(ProtocolConstant.RESPONSE);
		//System.out.println("------>"+"28");
		communicationProtocol.setHeaders(prepareHeaders());
		//System.out.println("------>"+"30");
		System.out.println("------>"+"31");
		return communicationProtocol;
	}
	private Hashtable<String, String> prepareHeaders() {
		final Hashtable<String, String> headers = new Hashtable<>();
        headers.put(ProtocolConstant.METHOD, "");
        headers.put(ProtocolConstant.ACTION_NAME, requestDetails.get(ProtocolConstant.ACTION_NAME));
        return headers;
	}

}
