package com.dialogflow.services;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.adaptors.dialogflow.dialogflowadaptor.BotResponse;
import com.adaptors.dialogflow.dialogflowadaptor.UserRequest;
import com.dialogflow.services.DialogflowConfig.AgentConfig.KnowledgeBaseConfig;
import com.google.api.gax.core.FixedCredentialsProvider;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.DetectIntentRequest;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.EventInput;
import com.google.cloud.dialogflow.v2beta1.KnowledgeAnswers;
import com.google.cloud.dialogflow.v2beta1.KnowledgeBaseName;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.QueryParameters;
import com.google.cloud.dialogflow.v2beta1.QueryResult;
import com.google.cloud.dialogflow.v2beta1.SentimentAnalysisRequestConfig;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.cloud.dialogflow.v2beta1.KnowledgeAnswers.Answer;
import com.google.cloud.dialogflow.v2beta1.TextInput.Builder;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.MapEntry;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.Value.KindCase;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Service
public class DialogflowService {
	
	

	
	
static Logger log = LoggerFactory.getLogger(DialogflowService.class);

	
	
	
	
	
@HystrixCommand(fallbackMethod = "sendFallbackResponse" , commandKey="detectIntentText", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
//commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="6000")}) 
public BotResponse detectIntentText(UserRequest request,DialogflowConfig.AgentConfig agentConfig) throws Exception {
		
		
		SessionsSettings sessionsSettings =
				    SessionsSettings.newBuilder()
				         .setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(agentConfig.getCredentialfile()))))
				        .build();
		try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
		      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
			    SessionName session = SessionName.of(agentConfig.getProjectId(), request.getSessionId());
			    log.info("Session Path: {}", session.toString());
			    
		        Builder textInput = TextInput.newBuilder()
		        		                         .setText(request.getUserQuery())
		        		                         .setLanguageCode(!StringUtils.isEmpty(request.getLanguageCode())?request.getLanguageCode():agentConfig.getLanguageCode());

		        //EventInput.newBuilder().setName(null).setLanguageCode(null)
		        
		        // Build the query with the TextInput
		        
		        QueryParameters queryParameters=null;
		        
		        com.google.cloud.dialogflow.v2beta1.QueryParameters.Builder newBuilder = QueryParameters.newBuilder();
		        
		        if(!CollectionUtils.isEmpty(agentConfig.getKnowledgeBases())) {
		        	      
		        	      
		        	      
		        	      agentConfig.getKnowledgeBases().forEach(kb->{
		        	    	  
		        	    	  KnowledgeBaseName knowledgeBaseName= KnowledgeBaseName.of(agentConfig.getProjectId(), kb.getName());
		        	    	  newBuilder.addKnowledgeBaseNames(knowledgeBaseName.toString());
		        	    	  log.info("Knowledge base name {} ",knowledgeBaseName.toString());
		        	    	  
		        	      });
		        	      
		        	     
		        		                		
		        }
		        
		        SentimentAnalysisRequestConfig sentimentAnalysisRequestConfig =
		                SentimentAnalysisRequestConfig.newBuilder().setAnalyzeQueryTextSentiment(true).build();
		        
		         queryParameters=newBuilder.setSentimentAnalysisRequestConfig(sentimentAnalysisRequestConfig).build();
		        
		        QueryInput queryInput = QueryInput.newBuilder()
		        		                              .setText(textInput).build();
		        
		        
		        DetectIntentRequest detectIntentRequest =
			            DetectIntentRequest.newBuilder()
			                .setSession(session.toString())
			                .setQueryInput(queryInput)
			                .setQueryParams(queryParameters)
			                .build();

		        // Performs the detect intent request
		        DetectIntentResponse response = sessionsClient.detectIntent(detectIntentRequest);

		        // Display the query result
		        QueryResult queryResult = response.getQueryResult();

		        
		        log.info("Query Text:{}", queryResult.getQueryText());
		        log.info("Detected Intent: {} (confidence: {})\n",
		            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
		        log.info("Fulfillment Text: {}", queryResult.getFulfillmentMessagesList());
		        
		        
		        BotResponse botResponse= new BotResponse();
		        botResponse.setSessionId(request.getSessionId());
		        botResponse.setUserQuery(request.getUserQuery());
		        botResponse.setFullfillmentText(queryResult.getFulfillmentText());
		        botResponse.setActionName(queryResult.getAction());
		        botResponse.setIntentName(queryResult.getIntent().getDisplayName());
		        botResponse.setEndInteraction(queryResult.getIntent().getEndInteraction());
		        botResponse.setSentimentScore(queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore());
		        
		        Struct parameters = queryResult.getParameters();
		        
		        Map<String,Object> ctxParams= new HashMap<String, Object>();
		        queryResult.getOutputContextsList().forEach(ctx->{
		        	
		        	extractParameters(ctxParams, ctx.getParameters());
		        });
		        	botResponse.setContextParameters(ctxParams);
		        
		        
		        
		     
		        
		        Map<FieldDescriptor, Object> fields = parameters.getAllFields();
		        
		        fields.forEach((k,v)->{
		        	
		        	log.info("Key : {}  , value : {}" ,k,v);
		        	
		        	
		        	if(k.isMapField()) {
		        		
		        	 if (v instanceof List) {
							List<MapEntry<String,Value>> entityList = (List) v;
							
							entityList.forEach(val->{
								
								Value value = val.getValue();
								
								log.info("Descriptor"+ value.getDescriptorForType().getFullName());
								
								if(value.getKindCase().equals(Value.KindCase.STRUCT_VALUE) ) {
									log.info("Struct found");
									Struct structValue = value.getStructValue();
									Map<String, Value> fieldsMap = structValue.getFieldsMap();
									
									log.info(""+fieldsMap.isEmpty());
									
									Set<Entry<String,Value>> entrySet = fieldsMap.entrySet();
									
									entrySet.forEach((es)->{
										
										log.info(es.getKey());
										log.info(es.getValue().getStringValue());
										botResponse.addEntity(val.getKey()+"."+es.getKey(),es.getValue().getStringValue());
									});
									
									log.info(""+fieldsMap.isEmpty());
									//fieldsMap.forEach(null)
									
								}
								else {
									botResponse.addEntity(val.getKey(),val.getValue().getStringValue());
									log.info("Key : {}  , value : {} , is Map Field? {}" , val.getKey(), val.getValue().getStringValue(),k.isMapField());
								}
							});
				
		        	 
		        	 }
		        		
		       }
		        	
		        	
		        });
		        
		        
		        //Handle Custom Payloads in response messages
		        
		        if(queryResult.getFulfillmentMessagesCount()!=0) {
		        	
		        	         queryResult.getFulfillmentMessagesList().forEach(message -> {
		        	        	 
		        	        	if( message.hasText()) {
		        	        		
		        	        		if(message.getText().getTextCount()>0) {
		        	        			
		        	        			message.getText().getTextList().forEach(textmessage -> botResponse.addFullfillmentMessage(textmessage));
		        	        		}
		        	        		
		        	        		
		        	        	}else if (message.hasPayload()) {
		        	        	 botResponse.addPayloadData(buildPayload2(message.getPayload().getAllFields()));
		        	        	 
		        	        	}
		        	        	 
		        	      });
		        	
		      	
		        }  
		        
		        KnowledgeAnswers knowledgeAnswers = queryResult.getKnowledgeAnswers();
		        for (Answer answer : knowledgeAnswers.getAnswersList()) {
		          log.info(" Question {}- Answer: {}", answer.getFaqQuestion(),answer.getAnswer());
		          log.info(" - Confidence: {}", answer.getMatchConfidence());
		          
		          botResponse.addKnowledgeAnswers(answer.getFaqQuestion(),answer.getAnswer(), answer.getMatchConfidence());
		        }
		        
		        
		        
		        return botResponse;
		        
		       
		        
		      
		    }
		
		
	}
	

@HystrixCommand(fallbackMethod = "sendFallbackResponse" , commandKey="detectEventText", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
//commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="6000")}) 
public BotResponse detectEventText(UserRequest request,DialogflowConfig.AgentConfig agentConfig) throws Exception {
		
		
		SessionsSettings sessionsSettings =
				    SessionsSettings.newBuilder()
				         .setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(agentConfig.getCredentialfile()))))
				        .build();
		try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
		      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
			    SessionName session = SessionName.of(agentConfig.getProjectId(), request.getSessionId());
			    log.info("Session Path: {}", session.toString());
			    
		        //Builder textInput = TextInput.newBuilder()
		        		                        // .setText(request.getUserQuery())
		        		                        // .setLanguageCode(!StringUtils.isEmpty(request.getLanguageCode())?request.getLanguageCode():agentConfig.getLanguageCode());

			    Map<String,Value> params= new HashMap<String,Value>();
			    
			    if(request.getParameters()!=null) {
			    	
			    	request.getParameters().forEach((key,param)->{
			    		
			    		params.put(key,Value.newBuilder()
                                        .setStringValue(param)
                                        .build() );
			    	});
			    
			    	
			    }
			    
			    
			    
		        com.google.cloud.dialogflow.v2beta1.EventInput.Builder eventInput= EventInput.newBuilder().setName(request.getEventName()).setLanguageCode(!StringUtils.isEmpty(request.getLanguageCode())?request.getLanguageCode():agentConfig.getLanguageCode())
		        		.setParameters(Struct.newBuilder().putAllFields(params)
                        /*.putFields("firstname",
                                Value.newBuilder()
                                        .setStringValue("Praveen")
                                        .build())
                        .putFields("lastname",
                                Value.newBuilder()
                                        .setStringValue("Pattanshetti")
                                        .build())*/
                        .build());
		        
		        // Build the query with the TextInput
		        
		        
		        
		        com.google.cloud.dialogflow.v2beta1.QueryParameters.Builder newBuilder = QueryParameters.newBuilder();
		        
		        //QueryParameters queryParameters= newBuilder.add;
		        
		        //queryParameters=newBuilder.setSentimentAnalysisRequestConfig(sentimentAnalysisRequestConfig).build();
		        
		        QueryInput queryInput = QueryInput.newBuilder()
		        		                              .setEvent(eventInput).build();
		        
		        
		        DetectIntentRequest detectIntentRequest =
			            DetectIntentRequest.newBuilder()
			                .setSession(session.toString())
			                .setQueryInput(queryInput)
			                //.setQueryParams(queryParameters)
			                .build();

		        // Performs the detect intent request
		        DetectIntentResponse response = sessionsClient.detectIntent(detectIntentRequest);

		        // Display the query result
		        QueryResult queryResult = response.getQueryResult();

		        
		        log.info("Query Text:{}", queryResult.getQueryText());
		        log.info("Detected Intent: {} (confidence: {})\n",
		            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
		        log.info("Fulfillment Text: {}", queryResult.getFulfillmentMessagesList());
		        
		        
		        BotResponse botResponse= new BotResponse();
		        botResponse.setSessionId(request.getSessionId());
		        botResponse.setUserQuery(request.getUserQuery());
		        botResponse.setFullfillmentText(queryResult.getFulfillmentText());
		        botResponse.setActionName(queryResult.getAction());
		        botResponse.setIntentName(queryResult.getIntent().getDisplayName());
		        botResponse.setEndInteraction(queryResult.getIntent().getEndInteraction());
		        botResponse.setSentimentScore(queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore());
		        
		        Struct parameters = queryResult.getParameters();
		        
		        Map<String,Object> ctxParams= new HashMap<String, Object>();
		        queryResult.getOutputContextsList().forEach(ctx->{
		        	
		        	extractParameters(ctxParams, ctx.getParameters());
		        });
		        	botResponse.setContextParameters(ctxParams);
		        
		        
		        
		     
		        
		        Map<FieldDescriptor, Object> fields = parameters.getAllFields();
		        
		        fields.forEach((k,v)->{
		        	
		        	log.info("Key : {}  , value : {}" ,k,v);
		        	
		        	
		        	if(k.isMapField()) {
		        		
		        	 if (v instanceof List) {
							List<MapEntry<String,Value>> entityList = (List) v;
							
							entityList.forEach(val->{
								
								Value value = val.getValue();
								
								log.info("Descriptor"+ value.getDescriptorForType().getFullName());
								
								if(value.getKindCase().equals(Value.KindCase.STRUCT_VALUE) ) {
									log.info("Struct found");
									Struct structValue = value.getStructValue();
									Map<String, Value> fieldsMap = structValue.getFieldsMap();
									
									log.info(""+fieldsMap.isEmpty());
									
									Set<Entry<String,Value>> entrySet = fieldsMap.entrySet();
									
									entrySet.forEach((es)->{
										
										log.info(es.getKey());
										log.info(es.getValue().getStringValue());
										botResponse.addEntity(val.getKey()+"."+es.getKey(),es.getValue().getStringValue());
									});
									
									log.info(""+fieldsMap.isEmpty());
									//fieldsMap.forEach(null)
									
								}
								else {
									botResponse.addEntity(val.getKey(),val.getValue().getStringValue());
									log.info("Key : {}  , value : {} , is Map Field? {}" , val.getKey(), val.getValue().getStringValue(),k.isMapField());
								}
							});
				
		        	 
		        	 }
		        		
		       }
		        	
		        	
		        });
		        
		        
		        //Handle Custom Payloads in response messages
		        
		        if(queryResult.getFulfillmentMessagesCount()!=0) {
		        	
		        	         queryResult.getFulfillmentMessagesList().forEach(message -> {
		        	        	 
		        	        	if( message.hasText()) {
		        	        		
		        	        		if(message.getText().getTextCount()>0) {
		        	        			
		        	        			message.getText().getTextList().forEach(textmessage -> botResponse.addFullfillmentMessage(textmessage));
		        	        		}
		        	        		
		        	        		
		        	        	}else if (message.hasPayload()) {
		        	        	 botResponse.addPayloadData(buildPayload2(message.getPayload().getAllFields()));
		        	        	 
		        	        	}
		        	        	 
		        	      });
		        	
		      	
		        }  
		        
		        
		        
		        
		        
		        return botResponse;
		        
		       
		        
		      
		    }
		
		
	}
	

  private static void extractParameters(Map<String,Object> params,Struct ctxparameters ) {
	  
	  
	  Map<FieldDescriptor, Object> fields = ctxparameters.getAllFields();
      
      fields.forEach((k,v)->{
      	
      	log.info("Key : {}  , value : {}" ,k,v);
      	
      	
      	if(k.isMapField()) {
      		
      	 if (v instanceof List) {
					List<MapEntry<String,Value>> entityList = (List) v;
					
					entityList.forEach(val->{
						
						Value value = val.getValue();
						
						log.info("Descriptor"+ value.getDescriptorForType().getFullName());
						
						if(value.getKindCase().equals(Value.KindCase.STRUCT_VALUE) ) {
							log.info("Struct found");
							Struct structValue = value.getStructValue();
							Map<String, Value> fieldsMap = structValue.getFieldsMap();
							
							log.info(""+fieldsMap.isEmpty());
							
							Set<Entry<String,Value>> entrySet = fieldsMap.entrySet();
							
							entrySet.forEach((es)->{
								
								log.info(es.getKey());
								log.info(es.getValue().getStringValue());
								 params.put(val.getKey()+"."+es.getKey(),es.getValue().getStringValue());
							});
							
							log.info(""+fieldsMap.isEmpty());
							//fieldsMap.forEach(null)
							
						}
						else {
							params.put(val.getKey(),val.getValue().getStringValue());
							log.info("Key : {}  , value : {} , is Map Field? {}" , val.getKey(), val.getValue().getStringValue(),k.isMapField());
						}
					});
		
      	 
      	 }
      		
     }
      	
      	
      });
	  
	  
	  
	  
	  
  }
	
	
/*public Map<String,Object> buildPayload(Map<FieldDescriptor, Object> fields) {
	  
	Map<String,Object> responseMap=new HashMap<>();
	 
	fields.forEach((k,v)->{
    	
	        
    	//System.out.format("Key : %s  , value : %s , class of Val- %s \n" ,k,v,v.getClass() );
    	
    	
    	if(k.isMapField()) {
    		
    	 if (v instanceof List) {
					List<MapEntry<String,Value>> entityList = (List) v;
					
					entityList.forEach(val->{
					
						if(val.getValue().hasStructValue()) {
							
							System.out.format("Key : %s :",val.getKey());
							//printFields( val.getValue().getStructValue().getAllFields());
							responseMap.put(val.getKey(),this.buildPayload(val.getValue().getStructValue().getAllFields()));
							
							
						}else {
							
							responseMap.put(val.getKey(), val.getValue().getStringValue());
					 	
						System.out.format("Key : %s  , value : %s , is Map Field? %s \n" , val.getKey(), val.getValue().getStringValue(),k.isMapField());
						}
					});
		
    	 	}
    		
    	 }
    	
   
    	
    	
    });
	
	return responseMap;
}*/
	
	

public Map<String,Object> buildPayload2(Map<FieldDescriptor, Object> fields) {
	  
	Map<String,Object> responseMap=new HashMap<>();
	 
	fields.forEach((k,v)->{
    	
	        
   	System.out.format("********Key is map: %s   ",k.isMapField());
    	
    	
    	if(k.isMapField()) {
    		
    	 if (v instanceof List) {
					List<MapEntry<String,Value>> entityList = (List) v;
					
					entityList.forEach(val->{
					
						if(val.getValue().hasStructValue()) {
							
							System.out.format("Key : %s :",val.getKey());
							//printFields( val.getValue().getStructValue().getAllFields());
							responseMap.put(val.getKey(),this.buildPayload2(val.getValue().getStructValue().getAllFields()));
							
							
						}
						else if (val.getValue().hasListValue()) {
							
							//al.getValue().getListValue()
							
							System.out.format("Key : %s :",val.getKey());
							List<Value> valuesList = val.getValue().getListValue().getValuesList();
							
							List<Object> payloadList= new ArrayList<Object>();
							
							valuesList.forEach(value->{
								
							 if(value.hasStructValue()) {
								
								 payloadList.add(buildPayload2(value.getStructValue().getAllFields()));
							  }
							 
							});
							
							System.out.println(payloadList);
							responseMap.put(val.getKey(),payloadList);
							
							
							
						 }
						
						else {
							
							
							
							if(val.getValue().getKindCase()==KindCase.NUMBER_VALUE) {
								responseMap.put(val.getKey(), val.getValue().getNumberValue());
							}
							
							else {
								responseMap.put(val.getKey(), val.getValue().getStringValue());
							}
					 	
						//System.out.format("Key : %s  , value : %s , is Map Field? %s \n" , val.getKey(), val.getValue().getStringValue(),k.isMapField());
						}
					});
		
    	 	}
    		
    	 }
    	
    
    	
    	
    });
	
	return responseMap;
}
	
public BotResponse sendFallbackResponse(UserRequest request,DialogflowConfig.AgentConfig agentConfig) {
	
   BotResponse response = new BotResponse();
   
   response.setFullfillmentText("I am unable to complete the request..trying to connect you to an agent ");
   response.setApiTimeoutFallback(true);
   
   return response;



}
	
	
	
	
	
	
	
	
}
