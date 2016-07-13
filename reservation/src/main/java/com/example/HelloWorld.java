package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope // To refresh in case the configuration changes
@RestController
public class HelloWorld {

	@Value("${message.greeting}")
	String name="Gargi";
	
	@RequestMapping("/reservation/greeting")
	public String helloWorld() {
		return "Hello " + name;
	}
}
