package com.trustai.mlm_rank_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trustai.*"})
public class MlmRankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MlmRankServiceApplication.class, args);
	}

}
