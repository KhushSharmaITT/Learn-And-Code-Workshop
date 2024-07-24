package com.utility.core;

import com.google.gson.annotations.Expose;

public class ResponseWrapper {
	@Expose
	public String jsonString;
	@Expose
	public String protocolFormat;
	@Expose
	public Exception exception;

	public ResponseWrapper() { 

	}

}
