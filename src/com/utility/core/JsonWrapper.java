package com.utility.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class JsonWrapper<T> {
	private Gson gson;
    private JSONParser parser;
    private Type type;

    public JsonWrapper() {
        gson = new Gson();
        parser = new JSONParser();
    }

    public JsonWrapper(Type type) { 
    	this();
        this.type = type;
    } 

    public JSONObject getJsonObject(T entity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) parser.parse(gson.toJson(entity));
        } catch (ParseException issue) {
            System.out.println("Error converting into JSON "+issue.getLocalizedMessage());
        }
        return jsonObject;
    }

    public JSONObject getJsonObject(String jsonText) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) parser.parse(jsonText);
        } catch (ParseException issue) {
            System.out.println("Error converting into JSON "+issue.getLocalizedMessage());
        }
        return jsonObject;
    }

    public T convertIntoObject(String jsonString) {
        T javaObject = gson.fromJson(jsonString, type);
        return javaObject;
    }

    public String convertIntoJson(T entity) {
    	System.out.println("in convert to json");
        return gson.toJson(entity);
    }

    public void setPrettyFormat(boolean prettyFormat) {
        final GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation();
        if(prettyFormat) {
            this.gson = builder.create();
        }
    } 

}
