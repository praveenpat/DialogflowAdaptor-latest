package com.dialogflow.services;

import com.adaptors.dialogflow.dialogflowadaptor.BotResponse;

/**
 * In routing agent configuration on Dialog flow , use intent name or action name to identify the target agent to which the subsequent request needs to be routed.
 * @author praveen
 *
 */
public class RoutingResponse {
	
	private String sessionId;
	
	private String userQuery;
	
	private String intentName;
	
	private String agentName;
	

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

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	
	
	 public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public static RoutingResponse mapToRoutingResponse(BotResponse response) {
		 
		 RoutingResponse routingResp=new RoutingResponse();
		 routingResp.agentName=response.getActionName();
		 routingResp.intentName=response.getIntentName();
		 routingResp.sessionId=response.getSessionId();
		 routingResp.userQuery=response.getUserQuery();
		
		 return routingResp;
		
	}
	

}
