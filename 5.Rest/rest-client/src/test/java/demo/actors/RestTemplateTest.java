package demo.actors;

import com.fasterxml.jackson.databind.JsonNode;
import demo.RestClientApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestTemplateTest {
    private Log log = LogFactory.getLog(getClass());

    private URI baseUri;
    private ConfigurableApplicationContext server;
    private RestTemplate restTemplate;
    private MovieRepository movieRepository;

    private URI moviesUri;

    @BeforeEach
    public void setUp() throws Exception{
        this.server = new SpringApplicationBuilder()
                .properties(Collections.singletonMap("server.port","0"))
                .sources(RestClientApplication.class).run();

        int port = this.server.getEnvironment()
                .getProperty("local.server.port",Integer.class,8080);
        this.restTemplate = this.server.getBean(RestTemplate.class);
        this.baseUri = URI.create("http://localhost:" + port + "/");
        this.moviesUri = URI.create(this.baseUri.toString() + "movies");
        this.movieRepository = this.server.getBean(MovieRepository.class);
    }

    @AfterEach
    public void tearDown() throws Exception{
        if(null != this.server)
            this.server.close();

    }

    @Test
    public void testRestTemplate(){
        ResponseEntity<Movie> postMovieResponseEntity = this.restTemplate.postForEntity(moviesUri, new Movie("Forest Gump"), Movie.class);
        URI uriOfNewMovie = postMovieResponseEntity.getHeaders().getLocation();
        log.info("the new movie lives at: " + uriOfNewMovie);

        JsonNode mapForMovieRecord = this.restTemplate.getForObject(uriOfNewMovie, JsonNode.class);
        log.info("\t read as a Map.class : " + mapForMovieRecord);

        assertEquals(mapForMovieRecord.get("title").asText(),postMovieResponseEntity.getBody().title);

        Movie movieReference = this.restTemplate.getForObject(uriOfNewMovie, Movie.class);

        assertEquals(movieReference.title,postMovieResponseEntity.getBody().title);
        log.info("\t.. read as a Movie.class: " + movieReference);

        ResponseEntity<Movie> movieResponseEntity = this.restTemplate.getForEntity(uriOfNewMovie, Movie.class);
        assertEquals(movieResponseEntity.getStatusCode(), HttpStatus.OK);
        System.out.println("ContentType--->" + movieResponseEntity.getHeaders().getContentType());
        assertEquals(movieResponseEntity.getHeaders().getContentType(),
                MediaType.parseMediaType("application/json"));

        log.info("\t .. read as ResponseEntity<Movie>: " + movieResponseEntity);

        ParameterizedTypeReference<CollectionModel<Movie>> movies
                = new ParameterizedTypeReference<CollectionModel<Movie>>() {};

        ResponseEntity<CollectionModel<Movie>> moviesResponseEntity = this.restTemplate.exchange(this.moviesUri, HttpMethod.GET, null, movies);


        CollectionModel<Movie> movieResources = moviesResponseEntity.getBody();
        movieResources.forEach(this.log::info);
        assertEquals(movieResources.getContent().size(),this.movieRepository.count());
        System.out.println("count-->" + movieResources.getLinks().stream().filter(m->m.getRel().equals("self")).count());
        assertTrue(movieResources.getLinks().stream()
                .filter(m -> m.getRel().equals("self")).count() == 1);



    }



}
