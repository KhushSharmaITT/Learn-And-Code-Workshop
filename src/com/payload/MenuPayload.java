package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.Menu;

public class MenuPayload {

	@Expose
	private List<Menu> menuWrappers = new ArrayList<>();

	public void setMenuWrapperDetails(List<Menu> menuWrapperList) {
//		System.out.println(menuWrapper.getAvailabilityStatus());
//		System.out.println(menuWrapper.getMealType());
//		System.out.println(menuWrapper.getName());
//		System.out.println(menuWrapper.getPrice());
		//System.out.println(menuWrapper.getAvailabilityStatus());
		this.menuWrappers.addAll(menuWrapperList);
	}

	public List<Menu> getMenuWrapperDetails() { 
		return this.menuWrappers;
	}
}
