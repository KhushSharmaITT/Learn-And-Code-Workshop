package com.utility.core;

import com.google.gson.annotations.Expose;

public class RequestWrapper {
	@Expose
	public String jsonString;
	@Expose
	public String protocolFormat;
	@Expose
	public Exception exception;
    
//	public RequestWrapper(Hashtable<String, String> requestDetails) {
//        this.requestDetails = requestDetails;
//    }

    public RequestWrapper() {
		// TODO Auto-generated constructor stub
	}

//	public CommunicationProtocol createRequestProtocol() {
//        //final String data = requestDetails.get("data");
//        //final String protocolFormat = requestDetails.get("protocolFormat");
//        CommunicationProtocol request = new CommunicationProtocol();
//        request.setProtocolVersion("1.0");
//        request.setProtocolFormat(protocolFormat);
//        //request.setData(data);
//        request.setProtocolType(ProtocolConstant.REQUEST);
//        request.setHeaders(prepareHeaders());
//
//        return request;
//    }

//    private Hashtable<String, String> prepareHeaders() {
//        final Hashtable<String, String> headers = new Hashtable<>();
//        headers.put(ProtocolConstant.METHOD, "");
//        headers.put(ProtocolConstant.ACTION_NAME, requestDetails.get("actionName"));
//        return headers;
//    }

}
