package com.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.console.ConsoleService;
import com.exception.DiscardMenuDurationException;
import com.google.gson.reflect.TypeToken;
import com.model.DiscardItem;
import com.model.Menu;
import com.model.UserLogger;
import com.repository.DiscardItemRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class DiscardItemService {
	private Type type;
	private JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper;
	DiscardItemRepository<DiscardItem> repository = new DiscardItemRepository<>();
	
	public DiscardItemService() {
		type = new TypeToken<UserActionWrapper<DiscardItem>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
	}
	public UserActionWrapper<DiscardItem> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		System.out.println("in prepare userAction");
		UserActionWrapper<DiscardItem> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}
	public DiscardItem getDiscardItemFeedback() {
		DiscardItem discardItem = new DiscardItem();
		discardItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Menu Id : ")));
		discardItem.setItemName(ConsoleService.getUserInput("Enter Name :"));
		return discardItem;
	}
	public UserActionWrapper<DiscardItem> prepareUserActionWrapperForFeedback(RequestWrapper requestWrapper) {
		Type type = new TypeToken<UserActionWrapper<DiscardItem>>() {}.getType();
		JsonWrapper<UserActionWrapper<DiscardItem>> jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
        UserActionWrapper<DiscardItem> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}
	public String update(List<DiscardItem> actionData) throws SQLException {
		System.out.println("Updated discard feedback");
		int rowUpdated = 0;
		rowUpdated = repository.update(actionData);
		return String.valueOf(rowUpdated);
	}
	
	public DiscardItem getDiscardItem() {
		DiscardItem discardItem = new DiscardItem();
		discardItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Menu Id :")));
		discardItem.setItemName(ConsoleService.getUserInput("Enter Name :"));
		return discardItem;
	}
	public List<Menu> prepareItemsToDelete(UserActionWrapper<DiscardItem> userActionWrapper) throws SQLException, DiscardMenuDurationException {
		System.out.println("In item to delete");
		if(checkProcessDuration(userActionWrapper)) {
			throw new DiscardMenuDurationException("UserId = "+ userActionWrapper.getUserWrapper().getId()+" already perform this operation. This operation should be done after 30 days");
		}
		else {
			List<Menu> itemsToDelete = new ArrayList<>();
			for(DiscardItem discardItem : userActionWrapper.getActionData()) {
				Menu itemTodelete = new Menu();
				itemTodelete.setMenuId(discardItem.getMenuId());
				itemsToDelete.add(itemTodelete);
			}
			return itemsToDelete;
		}
		
	}
	private boolean checkProcessDuration(UserActionWrapper<DiscardItem> userActionWrapper) throws SQLException {
		System.out.println("In item to duration");
		boolean isProcessDurationAMonth;
		UserLoggerService userLoggerService = new UserLoggerService();
		isProcessDurationAMonth = userLoggerService.checkUserLogForDiscardProcess(userActionWrapper);
		return isProcessDurationAMonth;
	}

}
