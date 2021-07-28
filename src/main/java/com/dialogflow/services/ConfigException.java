package com.dialogflow.services;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConfigException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public ConfigException() {
		super();
		
	}

	public ConfigException(String message) {
		super(message);
		
	}

}
