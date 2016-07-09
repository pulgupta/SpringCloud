package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.cloud.client.ServiceInstance;

@RestController
public class Reserve {

	@Autowired
	DiscoveryClient ds;
	
	@RequestMapping("/reservation/caller")
	public String callReservation() {
		List<ServiceInstance> instances= this.ds.getInstances("reservation");
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> re = rt.getForEntity(instances.get(0).getUri()+"/greeting", String.class );
		System.out.println(re.getBody());
		return "From client and not direct " + re.getBody();
	}
}
