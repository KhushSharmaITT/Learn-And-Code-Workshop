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
import com.payload.MenuPayload;
import com.service.ChefRecommendationService;
import com.service.DiscardItemService;
import com.service.FeedbackService;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class ManageChefAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	MenuService menuService;
	public ManageChefAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
		menuService = new MenuService();
	}
	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException, DiscardMenuDurationException {
		System.out.println("yeahhhhhh finally in menu recommendation");
		String actionToPerform = data.split("=")[1].trim();
		if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION)) {
			final Hashtable<Integer, Double> menuIdWithScoreMap;
			menuIdWithScoreMap = menuService.prepareRecommendations();
			List<Menu> updatedItemsScore = menuService.prepareMenuItemforUpdatedScore(menuIdWithScoreMap);
			String rowsUpdated = menuService.updateMenuItems(updatedItemsScore);
			System.out.println(rowsUpdated);
			String rowsRetrieved = menuService.viewTopFoodRecommendation();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_ROLLOUT_NEXT_DAY_MENU)) {
			final String dataToProcess = data.split("=")[0].trim();
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			UserActionWrapper<Menu> userActionWrapper = menuService.prepareUserActionWrapper(requestWrapper);
            //String menuIds[] = dataToProcess.split(",");
			ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
			List<ChefRecommendation> chefRecommendations = chefRecommendationService.getChefRecommendation(userActionWrapper.getActionData());
			String rowsInserted = chefRecommendationService.saveChefRecommendations(chefRecommendations);
			return rowsInserted +"="+ "Your input is recorded";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT)) {
			ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
			String rowsRetrieved = chefRecommendationService.viewVotedMenu();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
        }
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_DISCARD_MENU_ITEM_LIST)) {
			//ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
			FeedbackService feedbackService = new FeedbackService();
			String rowsRetrieved = feedbackService.viewDiscardedItem();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
        }
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_DISCARD_ITEM)) {
			final String dataToProcess = data.split("=")[0].trim();
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			DiscardItemService discardItemService = new DiscardItemService();
			System.out.println("In discard Item");
		    UserActionWrapper<DiscardItem> userActionWrapper = discardItemService.prepareUserActionWrapper(requestWrapper);
		    System.out.println("In discard Item 2");
		    List<Menu> menuItemsToDelete = discardItemService.prepareItemsToDelete(userActionWrapper);
		    String rowDeleted = menuService.deleteMenu(menuItemsToDelete);
		    return rowDeleted +"="+ "Record Deleted Successfully.";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_GET_DETAILED_FEEDBACK)) {
			final String dataToProcess = data.split("=")[0].trim();
			RequestWrapper requestWrapper = jsonWrapper.convertIntoObject(dataToProcess);
			DiscardItemService discardItemService = new DiscardItemService();
			UserActionWrapper<DiscardItem> userActionWrapper = discardItemService.prepareUserActionWrapperForFeedback(requestWrapper);
			String rowsUpdated = discardItemService.update(userActionWrapper.getActionData());
			return rowsUpdated +"="+ "Successfully updated the records.";
		}
		
		return null;
	}

}
