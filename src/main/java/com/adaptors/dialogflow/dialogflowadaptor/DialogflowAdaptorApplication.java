package com.adaptors.dialogflow.dialogflowadaptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan({"com.dialogflow.services","com.adaptors.dialogflow.dialogflowadaptor"})
@SpringBootApplication
@EnableCircuitBreaker
public class DialogflowAdaptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DialogflowAdaptorApplication.class, args);
	}

}
