package com.service;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adapter.ChefRecommendationAdapter;
import com.google.gson.reflect.TypeToken;
import com.model.ChefRecommendation;
import com.model.Menu;
import com.model.UserProfile;
//import com.model.Menu;
import com.repository.ChefRecommendationRepository;
import com.repository.UserProfileRepository;
import com.utility.core.JsonWrapper;
//import com.repository.MenuRepository;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;
import com.utility.user.UserWrapper;

public class ChefRecommendationService {
	private ChefRecommendationRepository<ChefRecommendation> repository;
    private List<UserProfile> currentUserProfile;
    private Type type;
	private JsonWrapper<UserActionWrapper<ChefRecommendation>> jsonWrapper;
	private ChefRecommendationAdapter adapter;
	public ChefRecommendationService() {
		type = new TypeToken<UserActionWrapper<ChefRecommendation>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
		repository = new ChefRecommendationRepository<>();
		currentUserProfile = new ArrayList<>();
		adapter = new ChefRecommendationAdapter();
	}
	public List<ChefRecommendation> getChefRecommendation(List<Menu> recommendedItems) {
		List<ChefRecommendation> chefRecommendations = new ArrayList<>();
		for(Menu recommendedItem : recommendedItems) {
			ChefRecommendation chefRecommendation = new ChefRecommendation();
			chefRecommendation.setMenuId(recommendedItem.getMenuId());
			chefRecommendations.add(chefRecommendation);
		}
		return chefRecommendations;
	}

	public String saveChefRecommendations(List<ChefRecommendation> chefRecommendations) throws SQLException {
		int rowsInserted;
		rowsInserted = repository.save(chefRecommendations);
		return String.valueOf(rowsInserted);
	}
	public String viewVotedMenu() throws SQLException {
		List<ChefRecommendation> votedMenu = new ArrayList<>();
		String queryToFind = "SELECT vi.MenuId, Count(vi.Vote) as VoteCount, m.ItemName AS MenuName , m.Score AS Score"
				             + " FROM "
				             + "VotedItem vi INNER JOIN Menu m "
				             + "ON vi.MenuId = m.MenuId WHERE DATE(vi.Date_Created) = CURDATE() GROUP BY(vi.MenuId)";
        votedMenu = repository.findRecords(queryToFind);
        StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s%n", "Menu Id", "MenuName", "Vote Count","Score"));
		result.append("---------------------------------------------------\n");
		//System.out.println(menuList);
		for (ChefRecommendation chefRecommendation : votedMenu) {
	        result.append(String.format("%-10d %-20s %-10d %-10.2f%n",
	            //chefRecommendation.getId(),
	            chefRecommendation.getMenuId(),
	            chefRecommendation.getMenuName(),
	            chefRecommendation.getVoteCount(),
	            chefRecommendation.getScore()
	        ));
	    }
		System.out.println(result.toString());
		return result.toString();
	}
	
	public String viewChefsRecommendation() throws SQLException {
			List<ChefRecommendation> chefsRecommendations  = new ArrayList<>();
			List<ChefRecommendation> actualChefsRecommendations = new ArrayList<>();
			for(UserProfile userProfile : currentUserProfile) {
				String queryToFind = adapter.prepareChefRecommendationQuery(userProfile);
				chefsRecommendations = repository.findRecords(queryToFind);
				actualChefsRecommendations.addAll(chefsRecommendations);
			}
			System.out.println("printing the data");
			StringBuilder result = new StringBuilder();
			result.append(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Menu ID", "MenuItem", "MealType", "Score", "Preference", "SpiceLevel", "CuisinePreference", "SweetTooth"));
			result.append("------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			//System.out.println(menuList);
			for (ChefRecommendation chefRecommendation : actualChefsRecommendations) {
		        result.append(String.format("%-10d %-20s %-20s %-20.2f %-20s %-20s %-20s %-20s%n",
		            chefRecommendation.getMenuId(),
		            chefRecommendation.getMenuName(),
		            chefRecommendation.getMealType(),
		            chefRecommendation.getScore(),
		            chefRecommendation.getPreference(),
		            chefRecommendation.getSpiceLevel(),
		            chefRecommendation.getCuisinePreference(),
		            chefRecommendation.getSweetTooth()
		        ));
		    }
			System.out.println(result.toString());
;		return result.toString();
		
	}
	public void prepareItemsForRecommendation(UserWrapper userWrapper) throws SQLException {

		String queryToFind = "SELECT profile_id, user_id, preference, spice_level, cuisine_preference, sweet_tooth "
				+ "FROM "
				+ "user_profiles "
				+ "WHERE user_id = "+userWrapper.getId();
		UserProfileRepository<UserProfile> profileRepo = new UserProfileRepository<>();
		currentUserProfile = profileRepo.findRecords(queryToFind);
	}
	public UserActionWrapper<ChefRecommendation> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<ChefRecommendation> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}

}
