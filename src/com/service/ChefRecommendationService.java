package com.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.ChefRecommendation;
import com.model.Menu;
//import com.model.Menu;
import com.repository.ChefRecommendationRepository;
//import com.repository.MenuRepository;

public class ChefRecommendationService {
	private ChefRecommendationRepository<ChefRecommendation> repository;

	public ChefRecommendationService() {
		repository = new ChefRecommendationRepository<>();
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
		String queryToFind = "SELECT * FROM chefrecommendation WHERE DATE(Date_Created) = CURDATE()";
        votedMenu = repository.findRecords(queryToFind);
        StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s%n", "ID", "Menu Id", "Vote Count"));
		result.append("---------------------------------------------------\n");
		//System.out.println(menuList);
		for (ChefRecommendation chefRecommendation : votedMenu) {
	        result.append(String.format("%-10d %-20s %-10s%n",
	            chefRecommendation.getId(),
	            chefRecommendation.getMenuId(),
	            chefRecommendation.getVoteCount()
	        ));
	    }
		System.out.println(result.toString());
		return result.toString();
	}
	
	public String viewChefsRecommendation() throws SQLException {
			List<ChefRecommendation> chefsRecommendations  = new ArrayList<>();
			String queryToFind = "SELECT Id,cr.MenuId, m.Name AS MenuItem, m.Score AS Score, VoteCount"+
			                     " FROM ChefRecommendation cr"+
					             " INNER JOIN Menu m ON cr.MenuId = m.MenuId"+
			                     " WHERE DATE(cr.Date_Created) = CURDATE()";
			chefsRecommendations = repository.findRecords(queryToFind);
			StringBuilder result = new StringBuilder();
			result.append(String.format("%-10s %-20s %-10s%n", "Menu ID", "MenuItem", "Score"));
			result.append("---------------------------------------\n");
			//System.out.println(menuList);
			for (ChefRecommendation chefRecommendation : chefsRecommendations) {
		        result.append(String.format("%-10d %-20s %-10.2f%n",
		            chefRecommendation.getMenuId(),
		            chefRecommendation.getMenuName(),
		            chefRecommendation.getScore()
		        ));
		    }
			System.out.println(result.toString());
;		return result.toString();
		
	}

}
