package com.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.console.ConsoleService;
import com.exception.DuplicateDataException;
import com.model.VotedItem;
import com.payload.VotedItemPayload;
import com.repository.VotedItemRepository;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;

public class VotedItemService {

	private JsonWrapper<VotedItemPayload> jsonWrapper;
	private VotedItemRepository<VotedItem> repository;
	public VotedItemService() {
		jsonWrapper = new JsonWrapper<>(VotedItemPayload.class);
        jsonWrapper.setPrettyFormat(true);
        repository = new VotedItemRepository<>();
	}
	public VotedItem getItemToVote() {
		VotedItem votedItem = new VotedItem();
		votedItem.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Menu Id to vote for from recommended menu :")));
		votedItem.setMealType(ConsoleService.getUserInput("Enter meal type(Breakfast/Lunch/Dinner): "));
		return votedItem;
	}

	public VotedItemPayload getVotedItemPayload(RequestWrapper requestWrapper) {
		VotedItemPayload votedItemPayload = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return votedItemPayload;
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
