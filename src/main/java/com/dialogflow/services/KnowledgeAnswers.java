package com.dialogflow.services;

public class KnowledgeAnswers {
	
	private String knowledgeQuestion;
	
	private String answer;
	private Number confidenceScore;

	public KnowledgeAnswers() {
		super();
	}
	
	
	public KnowledgeAnswers(String question,String answer, Number confidenceScore) {
		super();
		this.knowledgeQuestion=question;
		this.answer = answer;
		this.confidenceScore = confidenceScore;
	}
	
	public String getKnowledgeQuestion() {
		return knowledgeQuestion;
	}


	public void setKnowledgeQuestion(String knowledgeQuestion) {
		this.knowledgeQuestion = knowledgeQuestion;
	}
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Number getConfidenceScore() {
		return confidenceScore;
	}
	public void setConfidenceScore(Number confidenceScore) {
		this.confidenceScore = confidenceScore;
	}
	
	
	

}
