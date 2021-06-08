package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;

@SpringBootApplication
public class StreamsSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamsSinkApplication.class, args);
	}

}

@EnableBinding(Sink.class)
@MessageEndpoint
class CoffeeDrinker{
	@StreamListener(Sink.INPUT)
	private void drink(Coffee coffee){
		System.out.println(coffee);
	}
}

@Data
@AllArgsConstructor
class Coffee{

	private final String id, roast,name,modifier;

}
