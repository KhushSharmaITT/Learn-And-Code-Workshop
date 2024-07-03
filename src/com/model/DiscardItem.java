package com.model;

import java.sql.Timestamp;

public class DiscardItem {
	private int menuId;
    private String itemName;
    private double averageRating;
    private String chefQuestions;
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
}
