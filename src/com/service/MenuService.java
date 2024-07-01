package com.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import com.console.ConsoleService;
import com.model.Feedback;
import com.model.Menu;
import com.payload.MenuPayload;
import com.repository.FeedbackRepository;
import com.repository.MenuRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;

public class MenuService {

	private JsonWrapper<MenuPayload> jsonWrapper;
	private MenuRepository<Menu> repository;

	public MenuService() {
		jsonWrapper = new JsonWrapper<>(MenuPayload.class);
        jsonWrapper.setPrettyFormat(true);
        repository = new MenuRepository<>(); 
	}

	public Menu addItem() {
		Menu menuItem = new Menu();
		menuItem.setName(ConsoleService.getUserInput("Enter Name :"));
		menuItem.setPrice(Float.parseFloat(ConsoleService.getUserInput("Enter Price :")));
		menuItem.setMealType(ConsoleService.getUserInput("Enter Meal Type :"));
		menuItem.setAvailabilityStatus("Yes");
		menuItem.setScore(0.0f);
		return menuItem;
	}

	public MenuPayload prepareMenuPayload(RequestWrapper requestWrapper) {
		MenuPayload menuPayload = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return menuPayload;
	}

	public String saveItem(List<Menu> newMenuItems) throws SQLException {
		int rowSaved;
		rowSaved = repository.save(newMenuItems);
		return String.valueOf(rowSaved);

	}

	public String viewMenu() throws SQLException {
		System.out.println("In view menu");
		List<Menu> menuList = new ArrayList<>();
		menuList = repository.findAll();
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability", "Score", "Meal Type"));
		result.append("-------------------------------------------------------------------------------------\n");
		//System.out.println(menuList);
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

	public Menu updateItem() {
		Menu menuItem = new Menu();
		menuItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Id :")));
		menuItem.setName(ConsoleService.getUserInput("Enter New Name (press Enter to keep current): "));
		//menuItem.setPrice(Float.parseFloat(ConsoleService.getUserInput("Enter New Price (press Enter to keep current): ")));
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
		int rowDeleted;
		rowDeleted = repository.delete(deletedItems);
		return String.valueOf(rowDeleted);
	}

	public Hashtable<Integer, Float> prepareRecommendations() throws SQLException {
		final FeedbackRepository<Feedback> feedbackRepository = new FeedbackRepository<>();
		final FeedbackService feedbackService = new FeedbackService();
		List<Feedback> feedbackList = feedbackRepository.findAll();
		Hashtable<Integer, Float> scoreMap = new Hashtable<>();
        for (Feedback feedback : feedbackList) {
            int menuId = feedback.getMenuId();
            float rating = feedback.getRating();
            String sentiment = feedback.getSentiments();

            float sentimentScore = feedbackService.analyzeSentiment(sentiment);
            float finalScore = rating + sentimentScore;

            scoreMap.put(menuId, scoreMap.getOrDefault(menuId, 0.0f) + finalScore);
        }
		return scoreMap;
	}

	public List<Menu> prepareMenuItemforUpdatedScore(Hashtable<Integer, Float> menuIdWithScoreMap) {
		List<Menu> updatedItems = new ArrayList<>();
		for (Entry<Integer, Float> entry : menuIdWithScoreMap.entrySet()) {
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
		List<Menu> menuList = new ArrayList<>();
		String queryToFind = "SELECT * FROM Menu WHERE AvailabilityStatus = 'Yes' ORDER BY Score DESC LIMIT 3";
		menuList = repository.findRecords(queryToFind);
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s %-20s %-10s %-15s%n", "Menu ID", "Name", "Price", "Availability", "Score", "Meal Type"));
		result.append("--------------------------------------------------------------------------------------\n");
		//System.out.println(menuList);
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
		List<Menu> itemsToRolledOut = new ArrayList();
		for(String id : menuIds) {
			Menu itemTorolledOut = new Menu();
			itemTorolledOut.setMenuId(Integer.parseInt(id));
			itemsToRolledOut.add(itemTorolledOut);
		}
		// TODO Auto-generated method stub
		return itemsToRolledOut;
	}

}
