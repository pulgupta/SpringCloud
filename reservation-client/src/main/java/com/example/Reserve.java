package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController

public class Reserve {

	@Autowired
	DiscoveryClient ds;
	
	//A REST TEMPLATE bean is no longer created via auto configuration
	@Autowired
	RestTemplate restTemplate;
	
	public String callReservationfallback() {
		System.out.println("Sorry!! reservation service is not available");
		return "Sorry!! reservation service is not available";
	}
	
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller")
	public String callReservation() {
		System.out.println("in old");
		List<ServiceInstance> instances= this.ds.getInstances("reservation");
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> re = rt.getForEntity(instances.get(0).getUri()+"/reservation/greeting", String.class );
		System.out.println("old " + re.getBody());
		return "From client and not direct " + re.getBody();
	}
	/*
	 * Working perfectly. In new version we will not get the default RT.
	 * Create new and then add loadBalanced annotation to use ribbon load
	 * balancing capabilities
	 */
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller/new")
	public String callReservationNewWay() {
		System.out.println("in new");
		ResponseEntity<String> re = restTemplate.getForEntity("http://reservation/reservation/greeting", String.class );
		return "From client and not direct new " + re.getBody();
	}
	
}
