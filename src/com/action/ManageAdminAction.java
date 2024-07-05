package com.action;

import java.sql.SQLException;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.Menu;
import com.payload.MenuPayload;
import com.payload.MenuPayloadHelper;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class ManageAdminAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	private MenuService menuService;
	public MenuPayload menuResponsePayload;
	private MenuPayloadHelper<MenuPayload> menuPayloadHelper;
	public ManageAdminAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
        this.menuService = new MenuService();
        menuResponsePayload = new MenuPayload();
        this.menuPayloadHelper = new MenuPayloadHelper<>();
	}

	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {
		System.out.println("yeahhhhhh finally in manage menu");

		String actionToPerform = data.split("=")[1].trim();
		final String dataToProcess = data.split("=")[0].trim();

		if(actionToPerform.equals(ActionChoiceConstant.ADMIN_ADD)) {
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
			String rowSaved = menuService.saveItem(userActionWrapper.getActionData());
			return rowSaved+" Record successfuly saved." +"="+ "Record successfuly saved.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.ADMIN_VIEW)|| actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW)||actionToPerform.equals(ActionChoiceConstant.EMPLOYEE_VIEW_MENU)) {
			System.out.println("In admin view");
			String rowsRetrieved = menuService.viewMenu();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.ADMIN_UPDATE)) {
			System.out.println("In admin update");
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
			String rowUpdated = menuService.updateMenu(userActionWrapper.getActionData());
			return rowUpdated+" Record Updated Successfully."+"="+ "Record Updated Successfully.";
		}
		else {
			System.out.println("In admin delete");
		    RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		    UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
		    String rowDeleted = menuService.deleteMenu(userActionWrapper.getActionData());
		    return rowDeleted+"Record Deleted Successfully." +"="+ "Record Deleted Successfully.";
		}
	}

	private void prepareMenuResponse(MenuPayload menuRequestPayload) {
		// TODO Auto-generated method stub

	}

}
