package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

public class LoadBalancedRestTemplateCLR implements CommandLineRunner {
    private final RestTemplate restTemplate;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public LoadBalancedRestTemplateCLR(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,String> variables = Collections.singletonMap("name","Cloud Natives");

        ResponseEntity<JsonNode> response =
                this.restTemplate.getForEntity("//greetings-service/hi/{name}",
                        JsonNode.class, variables);

        JsonNode body = response.getBody();
        String greeting = body.get("greeting").asText();
        log.info("greeting: " +greeting);

    }
}
