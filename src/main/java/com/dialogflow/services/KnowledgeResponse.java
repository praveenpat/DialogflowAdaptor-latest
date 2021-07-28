package com.dialogflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class KnowledgeResponse {
	
	private String sessionId;
	private String userQuery;
	private String intentName;
	private String fullfillmentText;
	private List<KnowledgeAnswers> knowledgeAnswers;
	
	
	
	public KnowledgeResponse() {
		super();
		
	}
	
	
	
	
	public KnowledgeResponse(String sessionId, String fullfillmentText) {
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
	public String getFullfillmentText() {
		return fullfillmentText;
	}
	public void setFullfillmentText(String fullfillmentText) {
		this.fullfillmentText = fullfillmentText;
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
	
	

}
