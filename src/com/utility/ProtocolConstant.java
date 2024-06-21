package com.utility;

import com.google.gson.annotations.Expose;

public class ProtocolConstant {
	@Expose
	public static final String REQUEST = "request";
	@Expose
	public static final String RESPONSE = "response";
	@Expose
	public static final String METHOD = "method";
	@Expose
	public static final String STATUS = "status";
	@Expose
	public static final int ERROR_CODE = 500;
	@Expose
	public static final String SUCCESS = "Successful";
	@Expose
	public static final int SUCCESS_CODE = 200;
	@Expose
	public static final String FAILURE = "Failure";
	@Expose
	public static final String ERROR_MESSAGE = "error-message";
	@Expose
	public static final String PROTOCOL_FORMAT = "protocolFormat";
	@Expose
	public static final String JSON = "json";
	@Expose
	public static final String ACTION_NAME = "actionName";

}
