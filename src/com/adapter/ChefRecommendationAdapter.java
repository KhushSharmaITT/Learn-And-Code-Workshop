package com.adapter;

import java.sql.SQLException;
import com.model.UserProfile;

public class ChefRecommendationAdapter {

	public String prepareChefRecommendationQuery(UserProfile currentUserProfile) throws SQLException {
		System.out.println("In adapter");
		String queryToFind = "SELECT Id,cr.MenuId AS MenuId, m.ItemName AS MenuName, m.MealType AS MealType, m.Score AS Score, "+
				 "m.vegetarian_preference AS Preference, m.spice_level AS SpiceLevel, m.cuisine_preference AS CuisinePreference, m.sweet_tooth AS SweetTooth"+
                " FROM ChefRecommendation cr"+
	             " INNER JOIN Menu m ON cr.MenuId = m.MenuId"+
                " WHERE DATE(cr.Date_Created) = CURDATE() "+
	             "ORDER BY (CASE WHEN m.vegetarian_preference = '"+currentUserProfile.getPreference()+"' THEN 0 ELSE 1 END), "+
                "(CASE WHEN m.spice_level = '"+currentUserProfile.getSpiceLevel()+"' THEN 0 ELSE 1 END), "+
	             "(CASE WHEN m.cuisine_preference = '"+currentUserProfile.getCuisinePreference()+"' THEN 0 ELSE 1 END), "+
                "(CASE WHEN m.sweet_tooth ='"+currentUserProfile.getSweetTooth()+"' THEN 0 ELSE 1 END) DESC";
		
		System.out.println(queryToFind);
		
		return queryToFind;
		
	}

}
