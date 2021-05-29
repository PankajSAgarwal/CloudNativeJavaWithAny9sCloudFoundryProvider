package demo;

import demo.actors.Actor;
import demo.actors.Movie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.config.RepositoryConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class RestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestClientApplication.class, args);
	}

	@Bean
	TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){
		return new TransactionTemplate(transactionManager);
	}


	@Configuration
	static class SimpleRepositoryRestMvcConfiguration implements RepositoryRestConfigurer {
		@Override
		public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
			config.exposeIdsFor(Movie.class, Actor.class);
		}
	}

}
