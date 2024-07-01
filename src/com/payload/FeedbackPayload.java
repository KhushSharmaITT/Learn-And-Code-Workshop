package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.Feedback;

public class FeedbackPayload {

	@Expose
	private List<Feedback> feedbackWrappers = new ArrayList<>();

	public void setFeedbackWrapperDetails(List<Feedback> feedbackWrapperList) {

		this.feedbackWrappers.addAll(feedbackWrapperList);
	}

	public List<Feedback> getFeedbackWrapperDetails() { 
		return this.feedbackWrappers;
	}
}
