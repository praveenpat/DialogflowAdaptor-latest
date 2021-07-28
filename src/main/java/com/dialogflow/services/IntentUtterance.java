package com.dialogflow.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntentUtterance {
	
	private String id;
	private boolean isTemplate;
	private int count;
	private Data[] data;
	
	
	
	
	
	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}





	public boolean isTemplate() {
		return isTemplate;
	}





	public void setIsTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}





	public int getCount() {
		return count;
	}





	public void setCount(int count) {
		this.count = count;
	}





	public Data[] getData() {
		return data;
	}





	public void setData(Data[] data) {
		this.data = data;
	}





	public static class Data{
		
		private String text;
		private String meta;
		private boolean userDefined;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getMeta() {
			return meta;
		}
		public void setMeta(String meta) {
			this.meta = meta;
		}
		public boolean isUserDefined() {
			return userDefined;
		}
		public void setUserDefined(boolean userDefined) {
			this.userDefined = userDefined;
		}
		
		
		
	}
	
	
	
     public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		       IntentUtterance[] utterances= mapper.readValue(new File("/Users/praveen/Downloads/BillingAgent/intents/PromiseToPay_usersays_en.json"), IntentUtterance[].class);
	            
		       List<IntentUtterance> utteranceList= Arrays.asList(utterances);
		       
		       utteranceList.forEach(v->{
		    	   
		    	     
		    	     System.out.println(v.getData()[0].text);
		    	   
		    	   
		       });
		      
     
     }

}
