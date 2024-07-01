package com.action;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.model.ChefRecommendation;
import com.model.Menu;
import com.payload.MenuPayload;
import com.service.ChefRecommendationService;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;

public class MenuRecommendationAction implements Action{

	private JsonWrapper<RequestWrapper> jsonWrapper;
	MenuService menuService;
	public MenuRecommendationAction() {
		this.jsonWrapper = new JsonWrapper<>(RequestWrapper.class);
        this.jsonWrapper.setPrettyFormat(true);
		menuService = new MenuService();
	}
	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {
		System.out.println("yeahhhhhh finally in menu recommendation");
		String actionToPerform = data.split("=")[1].trim();
		if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION)) {
			final Hashtable<Integer, Float> menuIdWithScoreMap;
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
			MenuPayload menuRequestPayload = menuService.prepareMenuPayload(requestWrapper);
            //String menuIds[] = dataToProcess.split(",");
			ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
			List<ChefRecommendation> chefRecommendations = chefRecommendationService.getChefRecommendation(menuRequestPayload.getMenuWrapperDetails());
			String rowsInserted = chefRecommendationService.saveChefRecommendations(chefRecommendations);
			return rowsInserted +"="+ "Your input is recorded";
		}
		else if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_VOTED_REPORT)) {
			ChefRecommendationService chefRecommendationService = new ChefRecommendationService();
			String rowsRetrieved = chefRecommendationService.viewVotedMenu();
			return rowsRetrieved +"="+ "Successfully retrieved the records.";
        }
		
		return null;
	}

}
