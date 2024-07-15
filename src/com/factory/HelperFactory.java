package com.factory;

import com.helper.AdminHelper;
import com.helper.ChefHelper;
import com.helper.EmployeeHelper;
import com.helper.Helper;
import com.utility.ActionChoiceConstant;

public class HelperFactory {	
	
	public static Helper getInstance(String role) {
		if(role.equals(ActionChoiceConstant.ADMIN)) {
			return AdminHelper.getInstance();
		}
		else if(role.equals(ActionChoiceConstant.CHEF)) {
			return ChefHelper.getInstance();
		}
		else if(role.equals(ActionChoiceConstant.EMPLOYEE)) {
			return EmployeeHelper.getInstance();
		}
		return null;
		
	}
}
