package com.packsendme.microservice.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class MicroserviceSMSServerApplication {

	public static void main(String[] args) {
		System.out.println("------------------------------------------------");
		System.out.println(" MicroserviceSMSServerApplication  ");
		System.out.println("------------------------------------------------");

		SpringApplication.run(MicroserviceSMSServerApplication.class, args);
	}
	
	/*
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	} */
}
