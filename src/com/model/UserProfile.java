package com.model;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

public class UserProfile {

	@Expose
	private int profileId;
	@Expose
    private int userId;
	@Expose
    private String preference;
	@Expose
    private String spiceLevel;
	@Expose
    private String cuisinePreference;
	@Expose
    private String sweetTooth;
	@Expose
    private Timestamp createdAt;
	@Expose
    private Timestamp updatedAt;

    
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

   
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
