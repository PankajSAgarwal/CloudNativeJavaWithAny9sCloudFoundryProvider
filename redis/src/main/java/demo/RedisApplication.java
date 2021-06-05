package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Log
@EnableCaching
@EnableRedisHttpSession
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

	private final String topic = "chat";
	@Bean
	ApplicationRunner pubSub(RedisTemplate<String,String> rt){
		return titledRunner("publish/subscribe",args -> {
			rt.convertAndSend(topic,"Hello, world @ " + Instant.now().toString());
		});
	}

	@Bean
	RedisMessageListenerContainer listenerContainer(RedisConnectionFactory rcf){
		MessageListener ml = (message, bytes) -> {
			String str = new String(message.getBody());
			log.info("message from " + topic + "': '" + str);
		};
		RedisMessageListenerContainer rmlc = new RedisMessageListenerContainer();
		rmlc.setConnectionFactory(rcf);
		rmlc.addMessageListener(ml,new PatternTopic(this.topic));
		return rmlc;
	}

	@Bean
	CacheManager redisCache(RedisConnectionFactory cf){
		return RedisCacheManager
				.builder(cf)
				.build();
	}

	private long measure(Runnable r){
		long start = System.currentTimeMillis();
		r.run();
		long stop = System.currentTimeMillis();
		return stop - start;
	}
	@Bean
	ApplicationRunner cache(OrderService orderService){
		return titledRunner("caching", a->{
			Runnable measure = () -> orderService.byId(1L);
			log.info(" first: " + measure(measure));
			log.info(" two: " + measure(measure));
			log.info(" three: " + measure(measure));
		});
	}


	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

}

@Log
@Service
class OrderService{
	@Cacheable("order-by-id")
	public Order byId(Long id){
		//@formatter:off
		try{
			Thread.sleep(1000 * 10);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		//@formatter:on
		return new Order(id, new Date(), Collections.emptyList());
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

class ShoppingCart implements Serializable{
	private final Collection<Order> orders = new ArrayList<>();

	public void addOrder(Order order){
		this.orders.add(order);
	}

	public Collection<Order> getOrders(){
		return this.orders;
	}
}
@Log
@Controller
@SessionAttributes("cart")
class CartSessionController{
	private final AtomicLong ids = new AtomicLong();

	@ModelAttribute("cart")
	ShoppingCart cart(){
		log.info("Creating new cart");
		return new ShoppingCart();
	}

	@GetMapping("/orders")
	String orders(@ModelAttribute("cart") ShoppingCart cart, Model model){

		cart.addOrder(new Order(ids.incrementAndGet(),new Date(),Collections.emptyList()));
		model.addAttribute("orders",cart.getOrders());
		return "orders";
	}
}
