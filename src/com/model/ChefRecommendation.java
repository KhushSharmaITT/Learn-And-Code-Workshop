package com.model;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

public class ChefRecommendation {
	@Expose
	private int id;
	@Expose
    private int menuId;
	@Expose
    private String menuName;
	@Expose
    private float score;
	@Expose
    private int voteCount;
	@Expose
    private String preference;
	@Expose
    private String spiceLevel;
	@Expose
    private String cuisinePreference;
	@Expose
    private String sweetTooth;
	@Expose
    private String mealType;
	@Expose
    private Timestamp createdDate;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public String getSpiceLevel() {
		return spiceLevel;
	}
	public void setSpiceLevel(String spiceLevel) {
		this.spiceLevel = spiceLevel;
	}
	public String getCuisinePreference() {
		return cuisinePreference;
	}
	public void setCuisinePreference(String cuisinePreference) {
		this.cuisinePreference = cuisinePreference;
	}
	public String getSweetTooth() {
		return sweetTooth;
	}
	public void setSweetTooth(String sweetTooth) {
		this.sweetTooth = sweetTooth;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

}
