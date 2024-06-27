package com.model;

public class Feedback {
	private String employeeId;
    private int menuId;
    private String comment;
    private int rating;
    private String sentiments;
    //private Timestamp createdDate;

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public void setCreatedDate(Timestamp createdDate) {
//        this.createdDate = createdDate;
//    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSentiments(String sentiments) {
        this.sentiments = sentiments;
    }

    public int getMenuId() {
        return this.menuId;
    }

    public int getRating() {
        return this.rating;
    }

    public String getComment() {
        return this.comment;
    }

    public String getEmployeeId() { //this is user id
        return this.employeeId;
    }

    public String getSentiments() {
        return this.sentiments;
    }

//    public Timestamp getCreatedDate() {
//        return createdDate;
//    }
}
