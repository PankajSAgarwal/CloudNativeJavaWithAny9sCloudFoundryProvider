package com.pankaj.cloudnative.SampleCatApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SpringBootApplication
public class SampleCatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleCatApplication.class, args);
	}
}
