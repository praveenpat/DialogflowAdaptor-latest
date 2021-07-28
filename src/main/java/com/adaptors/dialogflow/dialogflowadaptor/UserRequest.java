package com.adaptors.dialogflow.dialogflowadaptor;

import java.util.Map;

public class UserRequest {
	
	private String sessionId;
	
	private String userQuery;
	
	private String eventName;
	
	

	private String languageCode;
	
	Map<String,String> channelAttributes;
	
	
	Map<String,String> parameters;
	
	
	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	
	
	public Map<String, String> getChannelAttributes() {
		return channelAttributes;
	}

	public void setChannelAttributes(Map<String, String> channelAttributes) {
		this.channelAttributes = channelAttributes;
	}

	public UserRequest() {
		super();
		
	}
	
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
	}

}
