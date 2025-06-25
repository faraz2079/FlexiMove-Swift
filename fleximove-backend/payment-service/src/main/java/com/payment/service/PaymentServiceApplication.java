package com.payment.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class PaymentServiceApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
}
