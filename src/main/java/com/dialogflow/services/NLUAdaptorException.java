package com.dialogflow.services;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NLUAdaptorException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public NLUAdaptorException() {
		super();
		
	}

	public NLUAdaptorException(String message) {
		super(message);
		
	}

}
