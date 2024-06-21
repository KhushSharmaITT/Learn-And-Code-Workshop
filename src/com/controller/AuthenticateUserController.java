package com.controller;

import java.sql.SQLException;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.google.gson.annotations.Expose;
import com.payload.UserPayload;
import com.payload.UserPayloadHelper;
import com.service.UserService;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.user.UserWrapper;

public class AuthenticateUserController {
	
	private JsonWrapper<RequestWrapper> jsonWrapper;
	private UserService userService;
	@Expose
	public UserPayload userResponsePayload;
	private UserPayloadHelper<UserPayload> userPayloadHelper;
	
	public AuthenticateUserController() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        this.userService = new UserService();
        userResponsePayload = new UserPayload();
        this.userPayloadHelper = new UserPayloadHelper<UserPayload>();
	}
    
	public String handleAuthorization(String data) throws InvalidDataException, SQLException, UserNotFoundException {
    	
    	System.out.println("yeahhhhhh finally");
    	RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(data);
    	UserPayload userRequestPayload = userService.prepareUserPayload(requestWrapper);
    	prepareUserResponse(userRequestPayload);
    	//final ResponseWrapper responseWrapper = new ResponseWrapper();
    	System.out.println(userResponsePayload.getUserWrapperDetails().getRole());
    	userPayloadHelper.setPayload(this.userResponsePayload);
    	System.out.println((userPayloadHelper.getResponseWrapper().jsonString));
    	JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
    	jsonResponseWrapper.setPrettyFormat(true);
        System.out.println("in auth");
        String jsonString = jsonResponseWrapper.convertIntoJson(userPayloadHelper.getResponseWrapper());
    	return jsonString+"="+userResponsePayload.getUserWrapperDetails().getRole();
}

    private void prepareUserResponse(UserPayload userRequestPayload) throws SQLException, InvalidDataException, UserNotFoundException {
    	UserWrapper userToAuthenticate = userRequestPayload.getUserWrapperDetails();
    	String userRole = userService.getUserRole(userToAuthenticate);
    	userToAuthenticate.setRole(userRole);
    	userResponsePayload.setUserWrapperDetails(userToAuthenticate);	
    }
}
