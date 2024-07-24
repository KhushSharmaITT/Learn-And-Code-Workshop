package com.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import com.console.ConsoleService;
import com.google.gson.reflect.TypeToken;
import com.model.Feedback;
import com.model.Menu;
import com.repository.FeedbackRepository;
import com.repository.MenuRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class MenuService {

	private Type type;
	private JsonWrapper<UserActionWrapper<Menu>> jsonWrapper;
	private MenuRepository<Menu> repository;

	public MenuService() {
		type = new TypeToken<UserActionWrapper<Menu>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
        repository = new MenuRepository<>(); 
	}

	public Menu addItem() {
		Menu menuItem = new Menu();
		menuItem.setName(ConsoleService.getUserInput("Enter Name :"));
		menuItem.setPrice(Float.parseFloat(ConsoleService.getUserInput("Enter Price :")));
		menuItem.setMealType(ConsoleService.getUserInput("Enter Meal Type(Breakfast/ Lunch/ Dinner) :"));
		menuItem.setPreference(ConsoleService.getUserInput("Enter food preference :"));
		menuItem.setSpiceLevel(ConsoleService.getUserInput("Enter food spice level :"));
		menuItem.setCuisinePreference(ConsoleService.getUserInput("Enter food cuisine preference :"));
		menuItem.setSweetTooth(ConsoleService.getUserInput("Is it a sweet(Yes/ No) :"));
		menuItem.setAvailabilityStatus("Yes");
		menuItem.setScore(0.0f);
		return menuItem;
	}

	public UserActionWrapper<Menu> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<Menu> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}

	public String saveItem(List<Menu> newMenuItems) throws SQLException {
		int rowSaved;
		rowSaved = repository.save(newMenuItems);
		return String.valueOf(rowSaved);

	}

	public List<Menu> viewMenu() throws SQLException {
		System.out.println("In view menu");
		List<Menu> menuList = new ArrayList<>();
		menuList = repository.findAll();
		return menuList;
	}

	public Menu updateItem() {
		Menu menuItem = new Menu();
		menuItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Id :")));
		menuItem.setName(ConsoleService.getUserInput("Enter New Name (press Enter to keep current): "));
		menuItem.setMealType(ConsoleService.getUserInput("Enter New Meal Type (Breakfast/Lunch/Dinner) (press Enter to keep current): "));
		menuItem.setAvailabilityStatus(ConsoleService.getUserInput("Enter new availability status (Yes/No) (press Enter to keep current):"));
		String updatedPrice = ConsoleService.getUserInput("Enter New Price (press Enter to keep current): ");
		if(updatedPrice != null && !updatedPrice.isEmpty()) {
			menuItem.setPrice(Float.parseFloat(updatedPrice));
		}
		else {
			menuItem.setPrice(0.0f);
		}
		return menuItem;
	}

	public String updateMenu(List<Menu> updatedMenus) throws SQLException {
		int rowUpdated;
		rowUpdated = repository.update(updatedMenus);
		return String.valueOf(rowUpdated);
	}

	public Menu getDeleteItem() {
		Menu menuItem = new Menu();
		menuItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Id :")));
	    return menuItem;
	}

	public String deleteMenu(List<Menu> deletedItems) throws SQLException {
		System.out.println("delete");
		int rowDeleted;
		rowDeleted = repository.delete(deletedItems);
		return String.valueOf(rowDeleted);
	}

	public Hashtable<Integer, Double> prepareRecommendations() throws SQLException {
		double alpha = 0.5; 
        double beta = 0.5;
		final FeedbackRepository<Feedback> feedbackRepository = new FeedbackRepository<>();
		List<Feedback> feedbacksToUpdate = new ArrayList<>();
		List<Feedback> feedbackList = feedbackRepository.findAll(); // only query those feedbacks which is not processed.
		Hashtable<Integer, Double> scoreMap = new Hashtable<>();
        for (Feedback feedback : feedbackList) {
            int menuId = feedback.getMenuId();
            float rating = feedback.getRating();
            double sentimentScore = feedback.getSentimentScore();
            double combinedScore = alpha * sentimentScore + beta * rating;
            double finalScore = Math.max(1, Math.min(5, combinedScore));
            scoreMap.put(menuId, scoreMap.getOrDefault(menuId, 0.0) + finalScore);
            feedback.setIsProcessed(1);
            feedbacksToUpdate.add(feedback);
        }
        updateFeedbacks(feedbacksToUpdate);
		return scoreMap;
	}

	private void updateFeedbacks(List<Feedback> feedbacksToUpdate) throws SQLException {
		int rowsUpdated;
		FeedbackRepository<Feedback> feedbackRepository = new FeedbackRepository<>();
		rowsUpdated = feedbackRepository.update(feedbacksToUpdate);
		System.out.println(rowsUpdated);
	}

	public List<Menu> prepareMenuItemforUpdatedScore(Hashtable<Integer, Double> menuIdWithScoreMap) {
		List<Menu> updatedItems = new ArrayList<>();
		for (Entry<Integer, Double> entry : menuIdWithScoreMap.entrySet()) {
            Menu updatedItemScore = new Menu();
            updatedItemScore.setMenuId(entry.getKey());
            updatedItemScore.setScore(entry.getValue());
            updatedItems.add(updatedItemScore);
        }
		return updatedItems;
	}

	public String updateMenuItems(List<Menu> updatedItemsScore) throws SQLException {
		int rowsUpdated;
		rowsUpdated = repository.update(updatedItemsScore);
		return String.valueOf(rowsUpdated);
	}

	public String viewTopFoodRecommendation() throws SQLException {
		System.out.println("In view recommended menu");
		final String mealTypes[] = {"Breakfast", "Lunch", "Dinner"};
		List<Menu> menuList = new ArrayList<>();
		for (String mealType : mealTypes) {
			String queryToFind = "SELECT * FROM Menu WHERE AvailabilityStatus = 'Yes' AND MealType = '"+mealType+"' ORDER BY Score DESC LIMIT 3";
			List<Menu> mealSpecificMenu = repository.findRecords(queryToFind);
			menuList.addAll(mealSpecificMenu);
		}
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability", "Score", "Meal Type"));
		result.append("--------------------------------------------------------------------------------------\n");
		for (Menu menu : menuList) {
	        result.append(String.format("%-10d %-20s %-10.2f %-20s %-10.2f %-15s%n",
	            menu.getMenuId(),
	            menu.getName(),
	            menu.getPrice(),
	            menu.getAvailabilityStatus(),
	            menu.getScore(),
	            menu.getMealType()
	        ));
	    }
		System.out.println(result.toString());
		return result.toString();
	}

	public List<Menu> getRolledOutItems(String[] menuIds) {
		List<Menu> itemsToRolledOut = new ArrayList<>();
		for(String id : menuIds) {
			Menu itemTorolledOut = new Menu();
			itemTorolledOut.setMenuId(Integer.parseInt(id));
			itemsToRolledOut.add(itemTorolledOut);
		}
		return itemsToRolledOut;
	}

}
