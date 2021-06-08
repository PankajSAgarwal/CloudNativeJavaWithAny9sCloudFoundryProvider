package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class StreamsProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamsProcessorApplication.class, args);
	}
}

@EnableBinding(Processor.class)
@MessageEndpoint
class CoffeeTransformer{

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	Coffee transform(Coffee inCoffee){
		Coffee outCoffee = new Coffee(inCoffee.getId(),
				inCoffee.getRoast().toLowerCase(),
				inCoffee.getName().toUpperCase(),
				inCoffee.getModifier().toLowerCase());
		System.out.println(outCoffee);

		return outCoffee;
	}

}

@Data
@AllArgsConstructor
class Coffee{
	private final String id, roast, name, modifier;
}