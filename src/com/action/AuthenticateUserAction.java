package com.action;

import java.sql.SQLException;
import java.util.Hashtable;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.google.gson.annotations.Expose;
import com.payload.UserPayload;
import com.payload.UserPayloadHelper;
import com.service.UserService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class AuthenticateUserAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	private UserService userService;
	@Expose
	public UserPayload userResponsePayload;
	private UserPayloadHelper<ResponseWrapper> userPayloadHelper;

	public AuthenticateUserAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        this.userService = new UserService();
        userResponsePayload = new UserPayload();
        
	} 

	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {
    	final String dataToProcess = data.split("=")[0].trim();
    	final Hashtable<String, Object> outputToProcess = new Hashtable<>();
    	RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
    	UserPayload userRequestPayload = userService.prepareUserPayload(requestWrapper);
    	UserWrapper userResponseWrapper = prepareUserResponse(userRequestPayload);
    	outputToProcess.put(ActionChoiceConstant.AUTHENTICATE_USER, userResponseWrapper);
    	this.userPayloadHelper = new UserPayloadHelper<>(outputToProcess); 
    	final ResponseWrapper responseWrapper = userPayloadHelper.getResponsePayload();
    	JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
    	jsonResponseWrapper.setPrettyFormat(true);
        String jsonString = jsonResponseWrapper.convertIntoJson(responseWrapper);
    	return jsonString+"="+userResponseWrapper.getRole();
}

    private UserWrapper prepareUserResponse(UserPayload userRequestPayload) throws SQLException, InvalidDataException, UserNotFoundException {
    	UserWrapper userToAuthenticate = userRequestPayload.getUserWrapperDetails();
    	userToAuthenticate = userService.getUserWithRole(userToAuthenticate);
    	return userToAuthenticate;
    }
}
