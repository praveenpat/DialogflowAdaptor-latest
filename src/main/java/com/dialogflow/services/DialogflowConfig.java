package com.dialogflow.services;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dialogflow")
public class DialogflowConfig {

	private String defaultLanguageCode;
	
	private List<AgentConfig> agents;
	
	private AgentConfig defaultAgent;
	
	private AgentConfig routingAgent;
	

	public AgentConfig getRoutingAgent() {
		return routingAgent;
	}

	public void setRoutingAgent(AgentConfig routingAgent) {
		this.routingAgent = routingAgent;
	}

	public AgentConfig getDefaultAgent() {
		return defaultAgent;
	}

	public void setDefaultAgent(AgentConfig defaultAgent) {
		this.defaultAgent = defaultAgent;
	}

	
	public List<AgentConfig> getAgents() {
		return agents;
	}

	public void setAgents(List<AgentConfig> agents) {
		this.agents = agents;
	}
	

	public String getDefaultLanguageCode() {
		return defaultLanguageCode;
	}

	public void setDefaultLanguageCode(String defaultLanguageCode) {
		this.defaultLanguageCode = defaultLanguageCode;
	}
	

	
	
	public static class AgentConfig{
		
		//logical name used in the program to lookup the right configuration
		private String agentName;
		private String languageCode;
	
		private String projectId;
		private String credentialfile;
		private List<KnowledgeBaseConfig> knowledgeBases;
		
		
		
		
		public String getAgentName() {
			return agentName;
		}




		public void setAgentName(String agentName) {
			this.agentName = agentName;
		}

		
		
		
		public String getLanguageCode() {
			return languageCode;
		}




		public void setLanguageCode(String languageCode) {
			this.languageCode = languageCode;
		}




		public String getProjectId() {
			return projectId;
		}




		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}




		public String getCredentialfile() {
			return credentialfile;
		}




		public void setCredentialfile(String credentialfile) {
			this.credentialfile = credentialfile;
		}




		public List<KnowledgeBaseConfig> getKnowledgeBases() {
			return knowledgeBases;
		}




		public void setKnowledgeBases(List<KnowledgeBaseConfig> knowledgeBases) {
			this.knowledgeBases = knowledgeBases;
		}




		public static class KnowledgeBaseConfig{
			
			private String name;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
			
		}
		
	}
	
}
