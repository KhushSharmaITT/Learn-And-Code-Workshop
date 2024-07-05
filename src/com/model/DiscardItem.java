package com.model;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

public class DiscardItem {
	@Expose
	private int id;
	@Expose
	private int menuId;
	@Expose
    private String itemName;
	@Expose
    private double averageRating;
	@Expose
    private String chefQuestions;
	@Expose
    private Timestamp dateCreated; 

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getChefQuestions() {
        return chefQuestions;
    }

    public void setChefQuestions(String chefQuestions) {
        this.chefQuestions = chefQuestions;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
