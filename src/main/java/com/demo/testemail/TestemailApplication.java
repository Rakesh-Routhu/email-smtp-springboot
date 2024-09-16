package com.demo.testemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"com.demo.testmail", "com.demo.testemail"})
@Retryable
@EnableScheduling
public class TestemailApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestemailApplication.class, args);
	}

}
