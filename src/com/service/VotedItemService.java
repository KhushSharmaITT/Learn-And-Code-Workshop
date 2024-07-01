package com.service;

import java.sql.SQLException;
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
		return votedItem;
	}

	public VotedItemPayload getVotedItemPayload(RequestWrapper requestWrapper) {
		VotedItemPayload votedItemPayload = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return votedItemPayload;
	}
	public String saveVotedItem(List<VotedItem> votedItems) throws SQLException, DuplicateDataException {
		// TODO Auto-generated method stub
		int rowSaved;
		List<VotedItem> duplicateResponses;
		for(VotedItem votedItem : votedItems) {
			String queryToFind = "SELECT * FROM VotedItem where UserId ="+votedItem.getUserId()+" AND MenuId ="+votedItem.getMenuId()+" AND DATE(Date_Created) = CURDATE()";
			duplicateResponses = repository.findRecords(queryToFind);
			if(!duplicateResponses.isEmpty()) {
				throw new DuplicateDataException("You already voted for this item today, Thank You ");
			}
		}
		rowSaved = repository.save(votedItems);
		return String.valueOf(rowSaved);
	}

}
