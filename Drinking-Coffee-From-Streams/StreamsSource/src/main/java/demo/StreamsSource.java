package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class StreamsSource {

	public static void main(String[] args) {
		SpringApplication.run(StreamsSource.class, args);
	}

}

@EnableBinding(Source.class)
@EnableScheduling
@AllArgsConstructor
class Barista{
	private final Source source;
	private final CoffeeMachine machine;

	@Scheduled(fixedRate = 1000)
	private void serve(){
		source.output().send(MessageBuilder.withPayload(machine.pour()).build());

	}
}

@Component
class CoffeeMachine {
	private final String[] roasts = "Light roast,Dark roast,Medium Roast,Italian Roast,French Roast".split(",");
	private final String[] names = "Colombian,Ethiopian,Guatemalan,Kona,Brazilian,Turkish,Cuban,Java".split(",");
	private final String[] modifiers = "Select Batch, Black Label, Premium Blend, Limited Run, Platinum Edition".split(",");
	private final Random rnd = new Random();
	private int i=0,j=0,k=0;

	Coffee pour(){
		i= rnd.nextInt(5);
		j= rnd.nextInt(8);
		k= rnd.nextInt(5);

		return new Coffee(UUID.randomUUID().toString(),
				roasts[i],
				names[j],
				modifiers[k]);
	}

}
@Data
@AllArgsConstructor
class Coffee{
	private final String id,roast,name,modifier;

}
