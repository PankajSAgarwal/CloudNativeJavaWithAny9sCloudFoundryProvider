package com.pankaj.cloudnative.configserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigurationServerApplicationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void defaultConfigurationAvailable() {
        ResponseEntity<Environment> entity
				= restTemplate.getForEntity("http://localhost:" + port + "/application/default",
				Environment.class);

		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(
				entity.getBody()
						.getPropertySources()
						.get(0)
						.getSource()
						.get("greeting"))
				.isEqualTo("Hello Cloud");


    }

}
