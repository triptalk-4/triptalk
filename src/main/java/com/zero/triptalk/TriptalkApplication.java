package com.zero.triptalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@SpringBootApplication
public class TriptalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriptalkApplication.class, args);
	}

}
