package com.example;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReservationClientApplication.class)
@WebAppConfiguration
public class ReservationClientApplicationTests {

	@Autowired
	Reserve rs;
	
	@Test
	public void reserveCallSuccessTest() {
		assertEquals(rs.callReservation(), "From client and not direct Hello \"Reservation-UPDATED-Pulkit\""); 
	}
	@Test
	public void reserveNewCallSuccessTest() {
		assertEquals(rs.callReservationNewWay(), "From client and not direct new Hello \"Reservation-UPDATED-Pulkit\""); 
	}

}
