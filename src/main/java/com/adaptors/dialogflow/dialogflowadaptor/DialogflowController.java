package com.adaptors.dialogflow.dialogflowadaptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dialogflow.services.ConfigException;
import com.dialogflow.services.DialogflowConfig;
import com.dialogflow.services.DialogflowService;
import com.dialogflow.services.KnowledgeResponse;
import com.dialogflow.services.NLUAdaptorException;
import com.dialogflow.services.RoutingResponse;
import com.dialogflow.services.DialogflowConfig.AgentConfig;

@RestController
public class DialogflowController {
	
	Logger log = LoggerFactory.getLogger(DialogflowController.class);

	@Autowired
	DialogflowService dialogflowService;
	
	@Autowired
	DialogflowConfig config;
	
	
	@GetMapping("/showConfig")
	public DialogflowConfig showConfig(){
		
		return config;
		
	}
	
	/**
	 * Use this API if you intend to use single agent with default agent configuration
	 * @param userRequest
	 * @return
	 */
	
	@PostMapping("/userQuery")
	public BotResponse handleUserQuery(@RequestBody UserRequest userRequest) {
		
		log.info("Recieved user request with session-id : {} and queryText : {} ", userRequest.getSessionId(),userRequest.getUserQuery());
		
		if(config.getDefaultAgent()==null) {
			throw new ConfigException("Default agent configuration not found");
		}
		
		try {
			return dialogflowService.detectIntentText(userRequest,config.getDefaultAgent());
		} catch (Exception e) {
			
			log.error("Encountered exception :",e);
			throw  new NLUAdaptorException(userRequest.getSessionId() +" : " +e.getMessage() );
		}
	}
	
	
	@PostMapping("/postEvent")
	public BotResponse handleEvent(@RequestBody UserRequest userRequest) {
		
		log.info("Recieved user request with session-id : {} and queryText : {} ", userRequest.getSessionId(),userRequest.getUserQuery());
		
		if(config.getDefaultAgent()==null) {
			throw new ConfigException("Default agent configuration not found");
		}
		
		try {
			return dialogflowService.detectEventText(userRequest,config.getDefaultAgent());
		} catch (Exception e) {
			
			log.error("Encountered exception :",e);
			throw  new NLUAdaptorException(userRequest.getSessionId() +" : " +e.getMessage() );
		}
	}
	
	
	
	

	@PostMapping("/{agentName}/userQueryByAgent")
	public BotResponse handleUserQueryByAgent(@PathVariable String agentName, @RequestBody UserRequest userRequest) {
		
		log.info("Recieved user request for agent {}  with session-id : {} and queryText : {} ", agentName,userRequest.getSessionId(),userRequest.getUserQuery());
		
		
		AgentConfig agentConfig=null;
		
		if(!CollectionUtils.isEmpty(config.getAgents())) {
			
			agentConfig=config.getAgents().stream().filter(c-> c.getAgentName().equalsIgnoreCase(agentName)).findAny().orElse(null);
			
			if(agentConfig==null) {
				
				throw new ConfigException("No Configuration for agent with name "+agentName + " found");
			}
		}
		
		try {
					return dialogflowService.detectIntentText(userRequest,agentConfig); 
		} catch (Exception e) {
			
			log.error("Encountered exception :",e);
			
			throw  new NLUAdaptorException(userRequest.getSessionId() +" : " +e.getMessage() );
		}
		
		
		
	}
	
	
	
	@PostMapping("/routing")
	public RoutingResponse handleRoutingRequest(@RequestBody UserRequest userRequest) {
		
		log.info("Recieved user request with session-id : {} and queryText : {} ", userRequest.getSessionId(),userRequest.getUserQuery());
		
		if(config.getRoutingAgent()==null) {
			throw new ConfigException("Routing agent configuration not found");
		}
		
		try {
			
			
			return RoutingResponse.mapToRoutingResponse( dialogflowService.detectIntentText(userRequest,config.getDefaultAgent()));
			
			
			
		} catch (Exception e) {
			
			log.error("Encountered exception :",e);
			throw  new NLUAdaptorException(userRequest.getSessionId() +" : " +e.getMessage() );
		}
	}
	
	
	
}
