package com.utility.core;

import java.util.Hashtable;

import com.google.gson.annotations.Expose;

public class CommunicationProtocol {
	 public int size;
	    @Expose
		private String data;
		private String protocolVersion;
		@Expose
	    private String protocolFormat;
		@Expose
		private String protocolType;
		@Expose
	    private String status;
		@Expose
	    private int statusCode;
	    @Expose
	    private String errorMessage;
		private String sourceIp;
		private int sourcePort;
		private String destIp;
		private int destPort;
		@Expose
		private Hashtable<String, String> headers;

	    public int getSize() {
	        return size;
	    }
	    public void setSize(int size) {
	        this.size = size;
	    }
	    public String getStatus() {
	        return status;
	    }
	    public void setStatus(String status) {
	        this.status = status;
	    }
	    public String getData() {
	        return data;
	    }
	    public int getStatusCode() {
	        return statusCode;
	    }
	    public void setStatusCode(int statusCode) {
	        this.statusCode = statusCode;
	    }
	    public void setData(String data) {
	        this.data = data;
	    }
	    public String getProtocolVersion() {
	        return protocolVersion;
	    }
	    public void setProtocolVersion(String protocolVersion) {
	        this.protocolVersion = protocolVersion;
	    }
	    public String getProtocolFormat() {
	        return protocolFormat;
	    }
	    public void setProtocolFormat(String protocolFormat) {
	        this.protocolFormat = protocolFormat;
	    }
	    public String getProtocolType() {
	        return protocolType;
	    }
	    public void setProtocolType(String protocolType) {
	        this.protocolType = protocolType;
	    }
	    public String getSourceIp() {
	        return sourceIp;
	    }
	    public void setSourceIp(String sourceIp) {
	        this.sourceIp = sourceIp;
	    }
	    public int getSourcePort() {
	        return sourcePort;
	    }
	    public void setSourcePort(int sourcePort) {
	        this.sourcePort = sourcePort;
	    }
	    public String getDestIp() {
	        return destIp;
	    }
	    public void setDestIp(String destIp) {
	        this.destIp = destIp;
	    }
	    public int getDestPort() {
	        return destPort;
	    }
	    public void setDestPort(int destPort) {
	        this.destPort = destPort;
	    }
	    public Hashtable<String, String> getHeaders() {
	        return headers;
	    }
	    public void setHeaders(Hashtable<String, String> headers) {
	        this.headers = headers;
	    }
	    public String getErrorMessage() {
	        return errorMessage;
	    }
	    public void setErrorMessage(String errorMessage) {
	        this.errorMessage = errorMessage;
	    }

}
