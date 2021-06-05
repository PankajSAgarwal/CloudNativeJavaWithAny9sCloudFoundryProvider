package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.*;

@Log
@SpringBootApplication
public class RedisApplication {
	private ApplicationRunner titledRunner(String title, ApplicationRunner rr){
		return args -> {
			log.info(title.toUpperCase());
			rr.run(args);
		};
	}

	@Bean
	ApplicationRunner geography(RedisTemplate<String,String> rt){

		return titledRunner("geography", args -> {
			GeoOperations<String, String> geo = rt.opsForGeo();
			geo.add("Sicily",new Point(13.361389,38.1155556),"Arigento");
			geo.add("Sicily",new Point(15.087269,37.502669),"Catania");
			geo.add("Sicily",new Point(13.583333,37.316667),"Palermo");

			Circle circle = new Circle(new Point(13.583333,37.316667),
					new Distance(100, RedisGeoCommands.DistanceUnit.KILOMETERS));

			GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = geo.radius("Sicily", circle);
			geoResults.getContent().forEach(c-> log.info(c.toString()));



		});

	}

	@Bean
	ApplicationRunner repositories(OrderRepository orderRepository,
								   LineItemRepository lineItemRepository){
		return titledRunner("repositories", args -> {
			Long orderId = generateId();
			List<LineItem> itemsList = Arrays.asList(new LineItem(orderId, generateId(), "plunger"),
					new LineItem(orderId, generateId(), "soup"),
					new LineItem(orderId, generateId(), "coffee mug"));

			itemsList
					.stream()
					.map(lineItemRepository::save)
					.forEach(li -> log.info(li.toString()));

			Order order = new Order(orderId, new Date(), itemsList);
			orderRepository.save(order);

			Collection<Order> found = orderRepository.findByWhen(order.getWhen());
			found.forEach(o->log.info("found: " + o.toString()));

		});
	}

	private Long generateId() {
		long tmp = new Random().nextLong();
		return Math.max(tmp, tmp * -1);
	}


	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

}

interface OrderRepository extends CrudRepository<Order,Long>{
	Collection<Order> findByWhen(Date d);
}


interface LineItemRepository extends CrudRepository<LineItem,Long>{}


@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("orders")
class Order implements Serializable{
	@Id
	private Long id;
	@Indexed
	private Date when;
	@Reference
	private List<LineItem> lineItems;
}

@RedisHash("lineItems")
@Data
@AllArgsConstructor
@NoArgsConstructor
class LineItem implements Serializable{
	@Indexed
	private Long orderId;
	@Id
	private Long id;
	private String description;

}
