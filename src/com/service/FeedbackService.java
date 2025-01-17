package com.service;

import com.payload.FeedbackPayload;
import com.repository.DiscardItemRepository;
import com.repository.FeedbackRepository;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import com.console.ConsoleService;
import com.exception.DuplicateDataException;
import com.google.gson.reflect.TypeToken;
import com.model.ChefRecommendation;
import com.model.DiscardItem;
import com.model.Feedback;
import com.model.Menu;
import com.model.VotedItem;
import com.utility.core.JsonWrapper;
import com.utility.core.RequestWrapper;
import com.utility.core.UserActionWrapper;

public class FeedbackService {
	private Type type;
	private JsonWrapper<UserActionWrapper<Feedback>> jsonWrapper;
	private FeedbackRepository<Feedback> repository;
	private final HashSet<String> goodWords = new HashSet<>(Arrays.asList(
            "delicious", "tasty", "delectable", "savory", "scrumptious", 
            "mouth-watering", "healthy", "fresh", "organic", "homemade", 
            "gourmet", "nutritious", "satisfying", "appetizing", "flavorful", 
            "succulent", "tender", "juicy", "exquisite", "zesty", 
            "refreshing", "aromatic", "hearty", "luscious", "ripe", 
            "tempting", "velvety", "divine", "crisp", "wholesome", 
            "perfectly cooked", "crave-worthy", "savory", "golden-brown", 
            "sumptuous", "well-balanced", "top-quality", "premium", 
            "mouthwatering", "decadent", "ambrosial", "spicy", "palatable"
        ));
	private final HashSet<String> badWords = new HashSet<>(Arrays.asList(
            "bland", "overcooked", "undercooked", "soggy", "stale", 
            "spoiled", "greasy", "unappetizing", "salty", "bitter", 
            "burnt", "fatty", "rancid", "tough", "unhealthy", 
            "inedible", "tasteless", "gross", "sour", "off-putting", 
            "disgusting", "sickening", "rubbery", "overly sweet", 
            "greasy", "artificial", "contaminated", "dry", "hard", 
            "stiff", "bland", "chewy", "flavorless", "underdone", 
            "fishy", "rancid", "gamey", "lukewarm", "cloying", 
            "over-seasoned", "watery", "mealy", "starchy", "limp","bad"
        ));
	private final HashSet<String> neutralWords = new HashSet<>(Arrays.asList(
            "baked", "boiled", "steamed", "grilled", "sautéed", 
            "roasted", "simmered", "chopped", "minced", "diced", 
            "raw", "preserved", "fermented", "frozen", "packaged", 
            "whole", "processed", "sliced", "blended", "marinated", 
            "dried", "poached", "braised", "brined", "canned", 
            "peeled", "trimmed", "shelled", "dehydrated", "defrosted", 
            "crushed", "pickled", "seasoned", "mixed", "shredded", 
            "cubed", "crumbled", "glazed", "grated", "layered", 
            "melted", "ground", "pureed", "refrigerated", "smoked", 
            "sprouted", "vacuum-packed", "wrapped", "browned", "folded"
        ));
	 private final HashSet<String> negationWords = new HashSet<>(Arrays.asList(
	            "not", "no", "never", "none", "neither", "nor", 
	            "nobody", "nothing", "nowhere", "non-", "can't", 
	            "won't", "isn't", "aren't", "doesn't", "didn't", 
	            "haven't", "hasn't", "hadn't", "won't", "shouldn't", 
	            "wouldn't", "couldn't", "mustn't", "wasn't", 
	            "weren't", "isn't", "ain't", "cannot", "without", 
	            "barely", "hardly", "scarcely", "not only", "not at all", 
	            "not even", "not really", "not quite", "not necessarily", 
	            "not very", "not much", "not so", "not in the least", 
	            "not anymore", "not yet", "not too", "not as", "no longer", 
	            "no way", "no matter"
	        ));
	public FeedbackService() {
		type = new TypeToken<UserActionWrapper<Feedback>>() {}.getType();
		jsonWrapper = new JsonWrapper<>(type);
		jsonWrapper.setPrettyFormat(true);
        repository = new FeedbackRepository<>();
	}

//	public float analyzeSentiment(String sentiment) {
//
//        switch (sentiment.toLowerCase()) {
//            case "positive":
//                return 1.0f;
//            case "neutral":
//                return 0.5f;
//            case "negative":
//                return -1.0f;
//            default:
//                return 0.0f;
//        }
//	}

	public Feedback getUserFeedback() {
		Feedback userFeedback = new Feedback();
		userFeedback.setMenuId(Integer.parseInt(ConsoleService.getUserInput("Enter Menu Id: ")));
		userFeedback.setComment(ConsoleService.getUserInput("Enter Comment: "));
		userFeedback.setRating(Float.parseFloat(ConsoleService.getUserInput("Enter Rating(Range between 1-5): ")));
		return userFeedback;
	}

	public UserActionWrapper<Feedback> prepareUserActionWrapper(RequestWrapper requestWrapper) {
		UserActionWrapper<Feedback> userActionWrapper = jsonWrapper.convertIntoObject(requestWrapper.jsonString);
		return userActionWrapper;
	}

	public List<Feedback> processFeedbacksForSentiments(List<Feedback> feedbackWrapperDetails) {
		List<Feedback> processedFeedbacks = new ArrayList<>();
		for(Feedback feedback : feedbackWrapperDetails) {
			double sentimentScore = processSentimentsFromComments(feedback.getComment());
			feedback.setSentimentScore(sentimentScore);
			feedback.setSentiments(analyzeSentiment(sentimentScore));
			processedFeedbacks.add(feedback);
		}
		return processedFeedbacks;
	}

	private double processSentimentsFromComments(String comment) {
		String commentToProcess[] = comment.split("\\s+");
		Hashtable<String,Integer> wordsWithScore = new Hashtable<>();
		int positiveCount = 0, negativeCount = 0, neutralCount = 0;
		//String finalSentiment;
		int positiveWeight = 3;
        int negativeWeight = -1;
        int neutralWeight = 1;
        int maxPossibleSentimentScore = 10;
		boolean negate = false;
		for(int index = 0; index < commentToProcess.length; index++) {
			String word = commentToProcess[index].toLowerCase();
			
			 if (negationWords.contains(word)) {
	                negate = !negate;
	                continue;
	            }
			 if (goodWords.contains(word)) {
	                if (negate) {
	                    negativeCount++; 
	                } else {
	                    positiveCount++;
	                }
	                negate = false;
			 }
			 else if (badWords.contains(word)) {
	                if (negate) {
	                    positiveCount++; 
	                } else {
	                    negativeCount++;
	                }
	                negate = false;
	            } 
			 else if (neutralWords.contains(word)) {
	                if (negate) {
	                    negativeCount++; 
	                } else {
	                    neutralCount++;
	                }
	                negate = false;
	            }
		}
		wordsWithScore.put("goodWords", positiveCount);
		wordsWithScore.put("badWords", negativeCount);
		wordsWithScore.put("neutralWords", neutralCount);
		
		 int cumulativeSentimentScore = positiveCount * positiveWeight 
                                        + negativeCount * negativeWeight 
                                        + neutralCount * neutralWeight;
		 System.out.print("score--"+cumulativeSentimentScore);
		 int K = maxPossibleSentimentScore / 2;
		 double weightedSentiment = (double)(cumulativeSentimentScore + K) / (maxPossibleSentimentScore + K) * 5;
		 System.out.print("score--"+weightedSentiment);
		 weightedSentiment = Math.max(1, Math.min(5, weightedSentiment));
		 return weightedSentiment;
	}
	
    private String analyzeSentiment(double weightedSentiment) {
    	 String finalSentiment;
		 if (weightedSentiment > 3.5) {
			    finalSentiment = "Positive";
	        } else if (weightedSentiment >= 2.5) {
	        	finalSentiment = "Neutral";
	        } else {
	        	finalSentiment = "Negative";
	        }
		 return finalSentiment;
	}

	public String saveFeedbacks(List<Feedback> processedFeedbacks) throws SQLException, DuplicateDataException {
		int rowSaved;
		List<Feedback> duplicateResponses = new ArrayList<>();
		for(Feedback feedback : processedFeedbacks) {
			String queryToFind = "SELECT UserId, MenuId FROM feedback WHERE UserId = "+feedback.getUserId()
			+" AND MenuId = "+feedback.getMenuId()+" AND DATE(Date_Created) = CURDATE()";
			duplicateResponses = repository.findRecords(queryToFind);
			if(!duplicateResponses.isEmpty()) {
				throw new DuplicateDataException("Your feedback for particular meal type is already recorded.");
			}
		}
		rowSaved = repository.save(processedFeedbacks);
		return String.valueOf(rowSaved);
	}

	public String viewDiscardedItem() throws SQLException {
		List<DiscardItem> discardedItemList = new ArrayList<>();
		List<Feedback> itemFeedbacks = new ArrayList<>();
		List<Feedback> feedbacksToUpdate = new ArrayList<>();
		String queryToFind = "SELECT f.MenuId, m.ItemName, AVG(f.Rating) as Avg_Rating, AVG(f.SentimentScore) as Avg_SentimentScore "
				+ "FROM "
				+ "feedback f INNER JOIN Menu m "
				+ "ON f.MenuId = m.MenuId "
				+ "WHERE f.Is_Discard_Process_Done = 0 "
				+ "GROUP BY f.MenuId,  m.ItemName";
		itemFeedbacks = repository.findRecords(queryToFind);
		for(Feedback feedback : itemFeedbacks) {
			double avgRating = processFeedbackForDiscardItem(feedback);
			if(avgRating < 2) {
				DiscardItem discardItem = new DiscardItem();
				discardItem.setMenuId(feedback.getMenuId());
				discardItem.setItemName(feedback.getItemName());
				discardItem.setAverageRating(avgRating);
				discardedItemList.add(discardItem);
			}
			feedback.setIsDiscardProcessDone(1);
			feedbacksToUpdate.add(feedback);
		}
		updateFeedbacks(feedbacksToUpdate);
		DiscardItemRepository<DiscardItem> discardItemRepository = new DiscardItemRepository<>();
		discardItemRepository.save(discardedItemList);
		
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-10s %-20s %-10s%n", "Menu ID", "MenuItem", "Average Rating"));
		result.append("---------------------------------------\n");
		//System.out.println(menuList);
		for (DiscardItem discardItem : discardedItemList) {
	        result.append(String.format("%-10d %-20s %-10.2f%n",
	        		discardItem.getMenuId(),
	        		discardItem.getItemName(),
	        		discardItem.getAverageRating()
	        ));
	    }
		System.out.println(result.toString());
	    return result.toString();
	}

	private void updateFeedbacks(List<Feedback> feedbacksToUpdate) throws SQLException {
		int rowsUpdated;
		rowsUpdated = repository.update(feedbacksToUpdate);
		System.out.println(rowsUpdated);
		//return String.valueOf(rowsUpdated);
		
	}

	private double processFeedbackForDiscardItem(Feedback feedback) {
		double sentimentScore = feedback.getSentimentScore();
		float rating = feedback.getRating();
		double alpha = 0.5; 
	    double beta = 0.5;
	    double combinedScore = alpha * sentimentScore + beta * rating;
	    //boolean isItemDiscarded = combinedScore < 2;
	    return combinedScore;
	}
}
