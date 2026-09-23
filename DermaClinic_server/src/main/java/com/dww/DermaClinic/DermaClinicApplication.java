package com.dww.DermaClinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DermaClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DermaClinicApplication.class, args);
	}

}
