package com.action;

import java.sql.SQLException;
import java.util.Hashtable;

import com.exception.InvalidDataException;
import com.exception.UserNotFoundException;
import com.service.FeedbackService;
import com.service.MenuService;
import com.utility.ActionChoiceConstant;

public class MenuRecommendationAction implements Action{

	MenuService menuService;
	public MenuRecommendationAction() {
		menuService = new MenuService();
	}
	@Override
	public String handleAction(String data) throws InvalidDataException, SQLException, UserNotFoundException {
		System.out.println("yeahhhhhh finally in menu recommendation");
		String actionToPerform = data.split("=")[1].trim();
		if(actionToPerform.equals(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION)) {
			final Hashtable<Integer, Float> menuIdWithScoreMap;
			menuIdWithScoreMap = menuService.prepareRecommendations();
		}
		return null;
	}

}
