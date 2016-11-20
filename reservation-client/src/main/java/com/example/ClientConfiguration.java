package com.example;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * To so that you call know rest template is thread safe so its perfectly fine
 * to pass it on to different threads and see the benefits of successive calls
 * @author pulgupta
 *
 */
@Configuration
public class ClientConfiguration {

	@Bean
	@LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}