package com.action;

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

public class AuthenticateUserAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	private UserService userService;
	@Expose
	public UserPayload userResponsePayload;
	private UserPayloadHelper<UserPayload> userPayloadHelper;

	public AuthenticateUserAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        this.userService = new UserService();
        userResponsePayload = new UserPayload();
        this.userPayloadHelper = new UserPayloadHelper<>(); 
	} 

	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {

    	System.out.println("yeahhhhhh finally");
    	final String dataToProcess = data.split("=")[0].trim();
    	RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
    	UserPayload userRequestPayload = userService.prepareUserPayload(requestWrapper);
    	prepareUserResponse(userRequestPayload);
    	userPayloadHelper.setPayload(this.userResponsePayload);
    	JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
    	jsonResponseWrapper.setPrettyFormat(true);
        String jsonString = jsonResponseWrapper.convertIntoJson(userPayloadHelper.getResponseWrapper());
    	return jsonString+"="+userResponsePayload.getUserWrapperDetails().getRole();
}

    private void prepareUserResponse(UserPayload userRequestPayload) throws SQLException, InvalidDataException, UserNotFoundException {
    	UserWrapper userToAuthenticate = userRequestPayload.getUserWrapperDetails();
    	userToAuthenticate = userService.getUserWithRole(userToAuthenticate);
    	//userToAuthenticate.setRole(userRole);
    	userResponsePayload.setUserWrapperDetails(userToAuthenticate);
    }
}
