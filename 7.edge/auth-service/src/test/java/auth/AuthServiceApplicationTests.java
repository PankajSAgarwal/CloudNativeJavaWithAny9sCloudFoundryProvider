package auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.boot.web.server.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


class AuthServiceApplicationTests {

	private static AtomicInteger PORT = new AtomicInteger();
	private final Log log = LogFactory.getLog(getClass());
	private ApplicationContext applicationContext;
	private RestTemplate restTemplate;
	private int port = 0;


	@BeforeEach
	public void setUp() throws Exception{
		this.restTemplate = new RestTemplate();
		this.applicationContext = SpringApplication.run(AuthConfig.class);
		this.port = PORT.get();
	}

	@Test
	public void generateToken() throws Exception{
		URI uri = URI.create("http://localhost:" + this.port + "/uaa/oauth/token");
		String username = "pankaj";
		String password ="spring";
		String clientSecret = "password";
		String client = "html5";
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>() {
			{
				add("client_secret",clientSecret);
				add("client_id",client);
				add("scope","openid");
				add("grant_type","password");
				add("username",username);
				add("password",password);
			}
		};

		String token = Base64Utils.encodeToString(
				(client + ":" +clientSecret).getBytes(Charset.forName("UTF-8"))
		);

		RequestEntity<LinkedMultiValueMap<String, String>> requestEntity
				= RequestEntity.post(uri)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic" + token)
				.body(map);

		ParameterizedTypeReference<Map<String, String>> type = new ParameterizedTypeReference<>() {
		};

		ResponseEntity<Map<String, String>> responseEntity = this.restTemplate.exchange(requestEntity, type);

		Map<String,String> body = responseEntity.getBody();

		log.info("access token: " + body.get("access_token"));
	}

	@Configuration
	@Import(AuthServiceApplication.class)
	public static class AuthConfig{
		@EventListener(WebServerInitializedEvent.class)
		public void ready(WebServerInitializedEvent event){
			PORT.set(event.getWebServer().getPort());
		}

	}

}
