package com.enri.mobileapi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class MobileApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileApiApplication.class, args);
	}

}
