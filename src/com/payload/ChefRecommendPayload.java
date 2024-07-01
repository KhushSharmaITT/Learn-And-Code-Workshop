package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.model.ChefRecommendation;

public class ChefRecommendPayload {
	private List<ChefRecommendation> recommendWrappers = new ArrayList<>();

	public List<ChefRecommendation> getRecommendWrappersDetails() {
		return this.recommendWrappers;
	}

	public void setRecommendWrappersDetails(List<ChefRecommendation> recommendWrappers) {
		this.recommendWrappers.addAll(recommendWrappers);
	}

}
