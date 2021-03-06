package com.example;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
public class Reserve {

	@Autowired
	DiscoveryClient ds;
	
	//A REST TEMPLATE bean is no longer created via auto configuration in the new version of spring
	@Autowired
	RestTemplate restTemplate;
	
	//We are using this to submit rest calls in parallel
	ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	//
	@Bean
	public GracefulShutdown grace() {
		return new GracefulShutdown(executorService);
	}
	
	Logger logger = LoggerFactory.getLogger(Reserve.class);
	
	public String callReservationfallback() {
		logger.error("Sorry!! reservation service is not available");
		return "Sorry!! reservation service is not available";
	}
	
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller")
	public String callReservation() {
		System.out.println("in old");
		List<ServiceInstance> instances= this.ds.getInstances("reservation");
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> re = rt.getForEntity(instances.get(0).getUri()+"/reservation/greeting", String.class );
		logger.info("old " + re.getBody());
		return "From client and not direct " + re.getBody();
	}
	
	/*
	 * Working perfectly. In new version of spring we will not get the default RT.
	 * Create new and then add loadBalanced annotation to use ribbon load
	 * balancing capabilities
	 */
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller/new")
	public String callReservationNewWay() {
		logger.info("in new");
		ResponseEntity<String> re = restTemplate.getForEntity("http://reservation/reservation/greeting", String.class );
		return "From client and not direct new " + re.getBody();
	}
	
	
	/**
	 * In this version of the controller I am trying to call the service in parallel using my own executor service
	 * @return the result of the backend service call
	 */
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller/threads")
	public String callReservationExecutorWay() {
		logger.info("in Threads");
		Callable<String> pc1 = new ParallelCalls("Test1", restTemplate);
		Callable<String> pc2 = new ParallelCalls("Test2", restTemplate);
		Future<String> result1 = executorService.submit(pc1);
		logger.info("This is after first call");
		Future<String> result2 = executorService.submit(pc2);
		logger.info("This is after second call");
		
		try {
			StringBuilder response = new StringBuilder(result1.get(1000, TimeUnit.MILLISECONDS));
			response.append(result2.get(1000, TimeUnit.MILLISECONDS));
			return response.toString();  
		} 
		catch (InterruptedException | TimeoutException | ExecutionException e) {
			logger.error("Backend resservation is giving delays. Check ASAP");
			e.printStackTrace();
			return "Sorry huge traffic has bumped you. Please try again";
		}
	}
	
	/**
	 * This is for testing the @async out of the box features of Spring.
	 * Not sure if this is working lets revisit it sometime again.
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@HystrixCommand(fallbackMethod="callReservationfallback")
	@RequestMapping("/reservation-client/caller/async")
	public String callReservationAsync() throws ExecutionException, InterruptedException {
		Future<String> reservation = this.getReservation();
		logger.info("See now the service will be called in a new thread");
		while(!reservation.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return reservation.get();
	}
	
	@Async
	public Future<String> getReservation() {
		ResponseEntity<String> re = restTemplate.getForEntity("http://reservation/reservation/greeting", String.class );
		logger.info("Sending back the async response");
		return new AsyncResult<String>(re.getBody());
	}
}
