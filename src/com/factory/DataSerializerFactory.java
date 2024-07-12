package com.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.exception.DataSerializationException;
import com.utility.ErrorMessageConstant;
import com.utility.core.DataSerializer;

public class DataSerializerFactory {
	private static Map<String, String> dataSerializerRegistry = new HashMap<>();

    static {
        dataSerializerRegistry.put("json", "com.utility.core.JsonDataSerializer");

    }
	public static DataSerializer getInstance(String protocolFormat) throws DataSerializationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DataSerializer serializer = null;
		String className = dataSerializerRegistry.get(protocolFormat);
			if(className == null || className == "") {
				throw new DataSerializationException(ErrorMessageConstant.NO_SERIALIZER_EXISTS +" : "+protocolFormat);
			}
			Class<?> dipatcherClass = Class.forName(className);
			Constructor<?> constructor = dipatcherClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			serializer = (DataSerializer) constructor.newInstance();
        return serializer;
    } 

}
