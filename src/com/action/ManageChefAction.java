package com.action;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import com.exception.DiscardMenuDurationException;
import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.ChefRecommendation;
import com.model.DiscardItem;
import com.model.Menu;
import com.payload.DiscardItemPayloadHelper;
import com.service.ChefRecommendationService;
import com.service.DiscardItemService;
import com.service.FeedbackService;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.ResponseWrapper;
import com.utility.core.UserActionWrapper;

public class ManageChefAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	MenuService menuService;
	private DiscardItemPayloadHelper<ResponseWrapper> discardItemPayloadHelper;
	private Hashtable<String, Object> outputToProcess;
	private DiscardItemService discardItemService;
	public ManageChefAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
		menuService = new MenuService();
		discardItemService = new DiscardItemService();
	}
	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, DiscardMenuDurationException {
		String actionToPerform = data.split("=")[1].trim();
		if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION)) {
			return viewRecommendation();
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU)) {
			
			return rolloutNextDayMenu(data);
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT)) {
			return viewVotedReport();
        }
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST)) {
			return viewDiscardMenu(data);
        }
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_DISCARD_ITEM)) {
			return discardItem(data);
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK)) {
			return processDetailedFeedback(data);
		}
		
		return null;
	}
	
	private String viewRecommendation() throws SQLException {
		final Hashtable<Integer, Double> menuIdWithScoreMap = menuService.prepareRecommendations();
		List<Menu> updatedItemsScore = menuService.prepareMenuItemforUpdatedScore(menuIdWithScoreMap);
		String rowsUpdated = menuService.updateMenuItems(updatedItemsScore);
		String rowsRetrieved = menuService.viewTopFoodRecommendation();
		return rowsRetrieved +"="+ "Successfully retrieved the records.";
	}
	
	private String rolloutNextDayMenu(String data) throws SQLException {
		final String dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
		ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
		List<ChefRecommendation> chefRecommendations = chefRecommendationService.getChefRecommendation(userActionWrapper.getActionData());
		String rowsInserted = chefRecommendationService.saveChefRecommendations(chefRecommendations);
		return rowsInserted +"="+ "Your input is recorded";
	}

	private String viewVotedReport() throws SQLException {
		ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
		String rowsRetrieved = chefRecommendationService.viewVotedMenu();
		return rowsRetrieved +"="+ "Successfully retrieved the records.";
	}
	
	private String viewDiscardMenu(String data) throws SQLException {
		final String actionToPerform = data.split("=")[1].trim();
		outputToProcess = new Hashtable<>();
		FeedbackService feedbackService = new FeedbackService();
		List<DiscardItem> rowsRetrieved = feedbackService.viewDiscardedItem();
		
		UserActionWrapper<DiscardItem> userActionResponseWrapper = new UserActionWrapper<>();
		userActionResponseWrapper.setActionData(rowsRetrieved);
		userActionResponseWrapper.setActionToPerform(actionToPerform);
		outputToProcess.put(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST_RESPONSE, userActionResponseWrapper);
		discardItemPayloadHelper = new DiscardItemPayloadHelper<>(outputToProcess);
		final ResponseWrapper responseWrapper = discardItemPayloadHelper.getResponsePayload();
		JsonWrapper<ResponseWrapper> jsonResponseWrapper = new JsonWrapper<>(ResponseWrapper.class);
    	jsonResponseWrapper.setPrettyFormat(true);
		final String jsonString = jsonResponseWrapper.convertIntoJson(responseWrapper);
		return jsonString +"="+ "Successfully retrieved the records.";
	}
	
	private String discardItem(String data) throws SQLException, DiscardMenuDurationException {
		final String dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
	    UserActionWrapper<DiscardItem> userActionWrapper = discardItemService.prepareUserActionWrapper(requestWrapper);
	    List<Menu> menuItemsToDelete = discardItemService.prepareItemsToDelete(userActionWrapper);
	    String rowDeleted = menuService.deleteMenu(menuItemsToDelete);
	    return rowDeleted +"="+ "Record Deleted Successfully.";
	}
	
	private String processDetailedFeedback(String data) throws SQLException {
		final String dataToProcess = data.split("=")[0].trim();
		RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
		UserActionWrapper<DiscardItem> userActionWrapper = discardItemService.prepareUserActionWrapperForFeedback(requestWrapper);
		String rowsUpdated = discardItemService.update(userActionWrapper.getActionData());
		return rowsUpdated +"="+ "Successfully updated the records.";
	}
}
