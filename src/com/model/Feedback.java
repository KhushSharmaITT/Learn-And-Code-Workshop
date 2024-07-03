package com.model;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

public class Feedback {
	@Expose
	private int id;
	@Expose
	private int userId;
	@Expose
    private int menuId;
	@Expose
	private String itemName;
	@Expose
    private String comment;
	@Expose
    private float rating;
	@Expose
    private String sentiments;
	@Expose
	private double sentimentScore;
	@Expose
    private Timestamp createdDate;
	@Expose
	private int isProcessed;            
	@Expose
	private int isDiscardProcessDone;

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public void setSentimentScore(Double sentimentScore) {
    	this.sentimentScore = sentimentScore;
    }

    public double getSentimentScore() {
    	return this.sentimentScore;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setSentiments(String sentiments) {
        this.sentiments = sentiments;
    }

    public int getMenuId() {
        return this.menuId;
    }

    public float getRating() {
        return this.rating;
    }

    public String getComment() {
        return this.comment;
    }

    public int getUserId() { //this is user id
        return this.userId;
    }

    public String getSentiments() {
        return this.sentiments;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public Timestamp getCreatedDate() {
        return createdDate;
    }

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(int isProcessed) {
		this.isProcessed = isProcessed;
	}

	public int getIsDiscardProcessDone() {
		return isDiscardProcessDone;
	}

	public void setIsDiscardProcessDone(int isDiscardProcessDone) {
		this.isDiscardProcessDone = isDiscardProcessDone;
	}
}
