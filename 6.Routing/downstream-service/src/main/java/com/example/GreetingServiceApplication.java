package com.example;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@EnableDiscoveryClient
@SpringBootApplication
public class GreetingServiceApplication {

	private Log log = LogFactory.getLog(getClass());

	@Bean
	RouterFunction<ServerResponse> routes(){
		return route(GET("/hi/{name}"),
				req -> ok().body(
						fromObject(Collections.singletonMap("greeting",
								String.format("Hello, %s!",req.pathVariable("name"))))));
	}

	private void debug(ServerRequest request){
		String str = ToStringBuilder.reflectionToString(request);
		log.info("request: " + str);
		request.headers().asHttpHeaders().forEach((k,v)->log.info(k+'='+v));
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}
}

@RestController
class GreetingsRestController{

	@RequestMapping(method = RequestMethod.GET,value="/hi/{name}")
	Map<String,String> hi(@PathVariable String name,
						  @RequestHeader(value="X-CNJ-Name", required = false)
								  Optional<String> cn){

		String resolvedName = cn.orElse(name);
		return Collections.singletonMap("greeting","Hello, " + resolvedName + "!");
	}
}
