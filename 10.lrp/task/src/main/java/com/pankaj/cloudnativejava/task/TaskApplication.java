package com.pankaj.cloudnativejava.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.repository.TaskExplorer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Stream;

@EnableTask
@SpringBootApplication
public class TaskApplication {
	private Log log = LogFactory.getLog(getClass());



	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@Bean
	CommandLineRunner runAndExplore(TaskExplorer taskExplorer){
		return args -> {
			Stream.of(args).forEach(log::info);
			taskExplorer.findAll(PageRequest.of(0,1))
					.forEach(taskExecution -> log.info(taskExecution.toString()));
		};
	}

}
