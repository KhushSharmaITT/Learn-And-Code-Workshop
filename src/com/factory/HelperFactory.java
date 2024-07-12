package com.factory;

import java.util.Hashtable;

import com.helper.AdminHelper;
import com.helper.ChefHelper;
import com.helper.Helper;
import com.utility.ActionChoiceConstant;

public class HelperFactory {
	private static Hashtable<String, String> helperRegistry = new Hashtable<>();
	
//	static {
//		helperRegistry.put(ActionChoiceConstant.ADMIN,"com.helper.AdminHelper");
//		helperRegistry.put(ActionChoiceConstant.CHEF,"com.helper.ChefHelper");
//		helperRegistry.put(ActionChoiceConstant.EMPLOYEE,"com.helper.EmployeeHelper");
//	}
	
	public static Helper getInstance(String role) {
		if(role.equals(ActionChoiceConstant.ADMIN)) {
			return AdminHelper.getInstance();
		}
		else if(role.equals(ActionChoiceConstant.CHEF)) {
			return ChefHelper.getInstance();
		}
		else if(role.equals(ActionChoiceConstant.EMPLOYEE)) {
			return AdminHelper.getInstance();
		}
		return null;
		
	}
}
