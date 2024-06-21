package com.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.controller.Controller;
import com.exception.DataSerializationException;
import com.exception.InvalidOperationException;
import com.utility.ErrorMessageConstant;

public class ControllerFactory {
	private static Map<String, String> controllerRegistry = new HashMap<>();

    static {
        controllerRegistry.put("Admin", "com.controller.AdminController");
        controllerRegistry.put("Chef", "com.controller.ChefController");
        controllerRegistry.put("Employee", "com.controller.EmployeeController");
    }
    public static Controller getInstance(String protocolFormat) throws DataSerializationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		Controller actionController = null;
		String className = controllerRegistry.get(protocolFormat);
			if(className == null || className == "") {
				throw new InvalidOperationException(ErrorMessageConstant.NO_CONTROLLER_EXISTS +" : "+protocolFormat);
			}
			Class<?> dipatcherClass = Class.forName(className);
			Constructor<?> constructor = dipatcherClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			actionController = (Controller) constructor.newInstance();  
        return actionController;
    }

}
