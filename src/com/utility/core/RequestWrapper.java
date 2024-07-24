package com.utility.core;

import com.google.gson.annotations.Expose;

public class RequestWrapper {
	@Expose
	public String jsonString;
	@Expose
	public String protocolFormat;
	@Expose
	public Exception exception;

    public RequestWrapper() {}

}
