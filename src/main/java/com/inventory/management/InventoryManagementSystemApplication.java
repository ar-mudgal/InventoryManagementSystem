package com.inventory.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients   feign client is not in use for now.
public class InventoryManagementSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementSystemApplication.class, args);
	}

}
