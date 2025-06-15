package de.fhdo.spring.user.context;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient

@EnableFeignClients(basePackages = "de.fhdo.spring.user.context.clients")
 public class UserContextServiceApp {

    
	public static void main(String[] args) {
		SpringApplication.run(UserContextServiceApp.class, args);
	}

}
