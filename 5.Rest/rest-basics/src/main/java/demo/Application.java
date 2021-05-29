package demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Arrays;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	public static class JsonpAdvice extends AbstractMappingJacksonResponseBodyAdvice{
		public JsonpAdvice() {
			super();
		}

		@Override
		protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
		}

		@Bean
		CommandLineRunner init(CustomerRepository r){
			return args -> Arrays.stream(
					("Mark,Fisher;Scott,Frederick;Brian,Dussault;"
							+ "Josh,Long;Kenny,Bastani;Dave,Syer;Spencer,Gibb").split(";"))
					.map(n->n.split(","))
					.map(tpl->r.save(new Customer(tpl[0],tpl[1])))
					.forEach(System.out::println);
		}
	}

}
