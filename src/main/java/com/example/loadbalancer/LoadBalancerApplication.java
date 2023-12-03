package com.example.loadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.loadbalancer")
@EntityScan("com.example.loadbalancer.entity")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class LoadBalancerApplication {

	public static void main(String[] args) {
//		com.ebanking.service1.config.checkTables ct = new com.ebanking.service1.config.checkTables();
//		ct.run();

		SpringApplication.run(LoadBalancerApplication.class, args);
	}
}
