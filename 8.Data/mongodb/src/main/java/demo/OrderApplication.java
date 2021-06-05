package demo;

import org.joda.time.DateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
public class OrderApplication {
	MongoCustomConversions customConversions(){
		return new MongoCustomConversions(Collections.singletonList(new LongToDateTimeConverter()));
	}

	@ReadingConverter
	public static class LongToDateTimeConverter
			implements Converter<Long, DateTime> {

		@Override
		public DateTime convert(Long source) {
			return Optional.ofNullable(source)
					.map(DateTime::new)
					.orElse(null);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
