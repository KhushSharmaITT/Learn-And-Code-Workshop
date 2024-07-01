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
    private String comment;
	@Expose
    private float rating;
	@Expose
    private String sentiments;
	@Expose
    private Timestamp createdDate;

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
