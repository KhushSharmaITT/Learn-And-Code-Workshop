package com.payload;

import com.google.gson.annotations.Expose;
import com.model.Menu;

public class MenuPayload {

	@Expose 
	private Menu menuWrapper;
	
	public void setMenuWrapperDetails(Menu menuWrapper) {
		System.out.println(menuWrapper.getAvailabilityStatus());
		System.out.println(menuWrapper.getMealType());
		System.out.println(menuWrapper.getName());
		System.out.println(menuWrapper.getPrice());
		//System.out.println(menuWrapper.getAvailabilityStatus());
		this.menuWrapper = menuWrapper;
	}
	
	public Menu getMenuWrapperDetails() {
		return this.menuWrapper;
	}
}
