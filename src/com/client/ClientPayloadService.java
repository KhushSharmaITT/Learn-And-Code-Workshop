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
		final String data = jsonWrapper.convertIntoJson(requestWrapper);
		communicationProtocol.setData(data);
		communicationProtocol.setProtocolFormat(requestDetails.get(ProtocolConstant.PROTOCOL_FORMAT));
		communicationProtocol.setProtocolType(ProtocolConstant.REQUEST);
		communicationProtocol.setHeaders(prepareHeaders());
		if (requestWrapper.exception != null) {
			communicationProtocol.setErrorMessage(requestWrapper.exception.getLocalizedMessage());
		}
		return communicationProtocol;
	}

	public CommunicationProtocol createResponseCommunicationProtocol(String data) {
		communicationProtocol.setData(data);
		communicationProtocol.setProtocolFormat(requestDetails.get(ProtocolConstant.PROTOCOL_FORMAT));
		communicationProtocol.setProtocolType(ProtocolConstant.RESPONSE);
		communicationProtocol.setHeaders(prepareHeaders());
		return communicationProtocol;
	}

	private Hashtable<String, String> prepareHeaders() {
		final Hashtable<String, String> headers = new Hashtable<>();
		headers.put(ProtocolConstant.METHOD, "");
		headers.put(ProtocolConstant.ACTION_NAME, requestDetails.get(ProtocolConstant.ACTION_NAME));
		return headers;
	}

}
