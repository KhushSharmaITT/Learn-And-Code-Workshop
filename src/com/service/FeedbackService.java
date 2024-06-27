package com.service;

import java.util.Hashtable;

public class FeedbackService {

	public float analyzeSentiment(String sentiment) {

        switch (sentiment.toLowerCase()) {
            case "positive":
                return 1.0f;
            case "neutral":
                return 0.5f;
            case "negative":
                return -1.0f;
            default:
                return 0.0f;
        }
	}
}
