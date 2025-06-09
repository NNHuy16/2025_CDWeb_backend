package com.example.jobSeaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobSeachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSeachingApplication.class, args);
	}

}
