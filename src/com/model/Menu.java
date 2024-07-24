package com.model;

import com.google.gson.annotations.Expose;

public class Menu {

	@Expose
	private int menuId;
	@Expose
	private float price;
	@Expose
	private String availabilityStatus;
	@Expose
	private String mealType;
	@Expose
	private double score;
	@Expose
	private String name;
	@Expose
	private String preference;
	@Expose
	private String spiceLevel;
	@Expose
	private String cuisinePreference;
	@Expose
	private String sweetTooth;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getAvailabilityStatus() {
		return availabilityStatus;
	}
	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
