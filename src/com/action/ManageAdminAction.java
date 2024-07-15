package com.action;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.Menu;
import com.payload.MenuPayloadHelper;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class ManageAdminAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	private MenuService menuService;
	private RequestWrapper requestWrapper;
	private String dataToProcess;
	private String actionToPerform;
	private Hashtable<String, Object> outputToProcess;
	private MenuPayloadHelper<ResponseWrapper> menuPayloadHelper;
	public ManageAdminAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        this.menuService = new MenuService();
	}

	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {
		String actionToPerform = data.split("=")[1].trim();	
		if(actionToPerform.equals(ActionChoiceConstant.ADMIN_ADD)) {
			return addItem(data);
		}
		else if(actionToPerform.equals(ActionChoiceConstant.ADMIN_VIEW)|| actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW)||actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_MENU)) {
			return viewMenu(data);
		}
		else if(actionToPerform.equals(ActionChoiceConstant.ADMIN_UPDATE)) {
			return updateItem(data);
		}
		else {
			return deleteItem(data);
		}
	}
	
	private String addItem(String requestedData) throws SQLException {
		dataToProcess = requestedData.split("=")[0].trim();
		requestWrapper= jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
		String rowSaved = menuService.saveItem(userActionWrapper.getActionData());
		return rowSaved+" Record successfuly saved." +"="+ "Record successfuly saved.";
	}
	
	private String viewMenu(String requestedData) throws SQLException {
		actionToPerform = requestedData.split("=")[1].trim();
		outputToProcess = new Hashtable<>();
		List<Menu> rowsRetrieved = menuService.viewMenu();
		UserActionWrapper<Menu> userActionResponseWrapper = new UserActionWrapper<Menu>();
		userActionResponseWrapper.setActionData(rowsRetrieved);
		userActionResponseWrapper.setActionToPerform(actionToPerform);
		outputToProcess.put(ActionChoiceConstant.ADMIN_VIEW_RESPONSE, userActionResponseWrapper);
		menuPayloadHelper = new MenuPayloadHelper<>(outputToProcess);
		final ResponseWrapper responseWrapper = menuPayloadHelper.getResponsePayload();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
    	jsonResponseWrapper.setPrettyFormat(true);
		final String jsonString = jsonResponseWrapper.convertIntoJson(responseWrapper);
		return jsonString +"="+ "Successfully retrieved the records.";
	}

	private String updateItem(String requestedData) throws SQLException {
		dataToProcess = requestedData.split("=")[0].trim();
		requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
		String rowUpdated = menuService.updateMenu(userActionWrapper.getActionData());
		return rowUpdated+" Record Updated Successfully."+"="+ "Record Updated Successfully.";
	}
	
	private String deleteItem(String requestedData) throws SQLException {
		dataToProcess = requestedData.split("=")[0].trim();
		requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
	    UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
	    String rowDeleted = menuService.deleteMenu(userActionWrapper.getActionData());
	    return rowDeleted+"Record Deleted Successfully." +"="+ "Record Deleted Successfully.";
	}
}
