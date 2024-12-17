package com.simplon.ttm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrouvetonmatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrouvetonmatchApplication.class, args);
	}

}
