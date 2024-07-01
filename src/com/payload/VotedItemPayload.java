package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.VotedItem;

public class VotedItemPayload {

	@Expose
	private List<VotedItem> votedItemWrappers = new ArrayList<>();
	
	public void setVotedItemWrapperDetails(List<VotedItem> votedItemWrapperList) {
		this.votedItemWrappers.addAll(votedItemWrapperList);
	} 

	public List<VotedItem> getVotedItemWrapperDetails() { 
		return this.votedItemWrappers;
	}
}
