package com.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.console.ConsoleService;
import com.exception.DuplicateDataException;
import com.google.gson.reflect.TypeToken;
import com.model.Menu;
import com.model.VotedItem;
import com.payload.VotedItemPayload;
import com.repository.VotedItemRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class VotedItemService {

	private Type type;
	private JsonWrapper<UserActionWrapper<VotedItem>> jsonWrapper;
	private VotedItemRepository<VotedItem> repository;
	public VotedItemService() {
		type = new TypeToken<UserActionWrapper<VotedItem>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
        jsonWrapper.setPrettyFormat(true);
        repository = new VotedItemRepository<>();
	}
	public VotedItem getItemToVote() {
		VotedItem votedItem = new VotedItem();
		votedItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Menu Id to vote for from recommended menu :")));
		votedItem.setMealType(ConsoleService.getUserInput("Enter meal type(Breakfast/Lunch/Dinner): "));
		return votedItem;
	}

	public UserActionWrapper<VotedItem> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<VotedItem> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}
	public String saveVotedItem(List<VotedItem> votedItems) throws SQLException, DuplicateDataException {
		// TODO Auto-generated method stub
		int rowSaved;
		List<VotedItem> duplicateResponses = new ArrayList<>();
		for(VotedItem votedItem : votedItems) {
			if(votedItem.getMenuId()>0) {
			String queryToFind = "SELECT vi.UserId, vi.MenuId, m.MealType "
					             + "FROM "
					             + "VotedItem vi INNER JOIN Menu m "
					             + "ON vi.MenuId = m.MenuId WHERE vi.UserId ="+votedItem.getUserId()
					             + " AND m.MealType = '"+votedItem.getMealType()
					             + "' AND DATE(Date_Created) = CURDATE()";
			duplicateResponses =repository.findRecords(queryToFind);
			}
			if(!duplicateResponses.isEmpty()) {
				throw new DuplicateDataException("Your vote for particular meal type is already recorded.");
			}
		}
			rowSaved = repository.save(votedItems);
			return String.valueOf(rowSaved);
		}
		
	}
