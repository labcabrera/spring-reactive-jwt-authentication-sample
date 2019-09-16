package com.github.labcabrera.jwt.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.labcabrera.jwt.sample.service.BootstrapService;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	@Autowired
	private BootstrapService bootstrapService;

	@Override
	public void run(String... args) {
		bootstrapService.initialize();
	}

}
