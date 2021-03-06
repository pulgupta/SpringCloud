package com.example;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ParallelCalls implements Callable<String>{

	Logger logger = LoggerFactory.getLogger(ParallelCalls.class);
	String dummyData;
	
	RestTemplate restTemplate;
	
	//This is only to allow spring to create a bean for this class
	public ParallelCalls() {}

	public ParallelCalls(String data, RestTemplate restTemplate) {
		//This data is just for dummy purpose in case we have to send some parameters to the service.
		this.dummyData = data;
		this.restTemplate = restTemplate;
	}

	@Override
	public String call() {
		logger.info("Checking that rest template is null or not " + restTemplate.toString());
		logger.info("Calling backend service in a new call " + this.dummyData);
		ResponseEntity<String> re = restTemplate.getForEntity("http://reservation/reservation/greeting", String.class );
		logger.info("Output of the service is " + re.getBody());
		return re.getBody();
	}

}
