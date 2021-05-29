package com.pankaj.cloudnative.SampleCloudFoundryApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class SampleCloudFoundryAppApplication {
	@Autowired
	private DataSource datasource;

	public static void main(String[] args) {
		SpringApplication.run(SampleCloudFoundryAppApplication.class, args);
	}

}
