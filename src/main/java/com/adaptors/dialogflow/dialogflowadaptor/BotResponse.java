package com.adaptors.dialogflow.dialogflowadaptor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

import com.dialogflow.services.KnowledgeAnswers;
import com.fasterxml.jackson.annotation.JsonInclude;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class BotResponse {
	
	private String sessionId;
	
	private String userQuery;
	
	private String fullfillmentText;
	
	private List<String> fullfillmentMessages;
	
	
	private Map<String,Object> entities;
	
	private Map<String,Object> contextParameters;
	
	
	private String actionName;
	
	private String intentName;
	
	private List<Map<String,Object>> customPayloads;
	

	private List<KnowledgeAnswers> knowledgeAnswers;
	
	private Number sentimentScore;
	
	private boolean endInteraction;
	
	private boolean apiTimeoutFallback;
	
	
	
	public Map<String, Object> getContextParameters() {
		return contextParameters;
	}



	public void setContextParameters(Map<String, Object> contextParameters) {
		this.contextParameters = contextParameters;
	}
	
	public List<String> getFullfillmentMessages() {
		return fullfillmentMessages;
	}



	public void setFullfillmentMessages(List<String> fullfillmentMessages) {
		this.fullfillmentMessages = fullfillmentMessages;
	}
	
	public void addFullfillmentMessage(String message) {
		
		if(CollectionUtils.isEmpty(this.fullfillmentMessages)) {
			this.fullfillmentMessages=new ArrayList<>();
		}
		
		this.fullfillmentMessages.add(message);
	}
	
	
	
	public boolean isApiTimeoutFallback() {
		return apiTimeoutFallback;
	}



	public void setApiTimeoutFallback(boolean apiTimeoutFallback) {
		this.apiTimeoutFallback = apiTimeoutFallback;
	}



	public boolean isEndInteraction() {
		return endInteraction;
	}



	public void setEndInteraction(boolean endInteraction) {
		this.endInteraction = endInteraction;
	}



	public void setCustomPayloads(List<Map<String, Object>> customPayloads) {
		this.customPayloads = customPayloads;
	}



	public String getUserQuery() {
		return userQuery;
	}



	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
	}



	public Number getSentimentScore() {
		return sentimentScore;
	}



	public void setSentimentScore(Number sentimentScore) {
		this.sentimentScore = sentimentScore;
	}



	public List<Map<String, Object>> getCustomPayloads() {
		return customPayloads;
	}



	public void setCustomPayload(List<Map<String, Object>> customPayload) {
		this.customPayloads = customPayload;
	}
	
	
	
	public void addPayloadData(Map<String,Object> payLoad) {
		
		if (this.customPayloads==null) {
			
			this.customPayloads=new ArrayList<Map<String,Object>>();
		}
		
		this.customPayloads.add(payLoad);
	}
	
	
	
	


	public List<KnowledgeAnswers> getKnowledgeAnswers() {
		return knowledgeAnswers;
	}



	public void setKnowledgeAnswers(List<KnowledgeAnswers> knowledgeAnswers) {
		this.knowledgeAnswers = knowledgeAnswers;
	}

	
	public void addKnowledgeAnswers(String question,String answer,Number confidenceScore) {
		
		if(CollectionUtils.isEmpty(this.knowledgeAnswers)) {
		    this.knowledgeAnswers = new ArrayList<KnowledgeAnswers>();
		}
		
		this.knowledgeAnswers.add(new KnowledgeAnswers(question,answer,confidenceScore));
	}


	public String getIntentName() {
		return intentName;
	}



	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}


	


	



	public Map<String, Object> getEntities() {
		return entities;
	}



	public void setEntities(Map<String, Object> entities) {
		this.entities = entities;
	}
	
	public void addEntity(String name,String value) {
		
		if(CollectionUtils.isEmpty(entities)) {
			entities=new HashMap<>();
		}
		
		entities.put(name, value);
		
	}



	public String getActionName() {
		return actionName;
	}



	public void setActionName(String actionName) {
		this.actionName = actionName;
	}



	public BotResponse() {
		super();
		
	}
	
	
	
	public BotResponse(String sessionId, String fullfillmentText) {
		super();
		this.sessionId = sessionId;
		this.fullfillmentText = fullfillmentText;
	}



	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getFullfillmentText() {
		return fullfillmentText;
	}
	public void setFullfillmentText(String fullfillmentText) {
		this.fullfillmentText = fullfillmentText;
	}
	
	

}
