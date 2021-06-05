package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles(profiles = "test")
class RedisApplicationTests {
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(){
		flush();
	}

	private void flush(){
		redisTemplate.execute((RedisConnection connection) -> {
			connection.flushDb();
			return "OK";
		});

		userRepository.deleteAll();
	}

	@Test
	public void createUser() throws Exception{
		//Setup test data

		User expectedUser = new User("Jane","Doe");

		// test create user success

		User actualUser = objectMapper.readValue(this
				.mockMvc
				.perform(
						post("/users")
								.content(objectMapper.writeValueAsString(expectedUser))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString(),User.class);

		// Test create user conflict
		this.mockMvc
				.perform(
					post("/users")
					.content(objectMapper.writeValueAsString(expectedUser))
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());

		// cleanup
		userService.deleteUser(actualUser.getId());
	}

	@Test
	public void getUser() throws Exception{
		// Setup test data
		User expectedUser = new User("John", "Doe");

		expectedUser = userService.createUser(expectedUser);

		// Test get user success
		this.mockMvc.perform(get("/users/{id}", expectedUser.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

		// Clean up
		userService.deleteUser(expectedUser.getId());

		// Test get user not found

		this.mockMvc.perform(get("/users/{id}", expectedUser.getId()))
				.andExpect(status().isNotFound());


	}

	@Test
	public void updateUser() throws Exception{
		flush();
		// Setup test data
		User expectedUser = new User("Johnny","Appleseed");
		// Test get user not found
		this.mockMvc.perform(get("/users/{id}",expectedUser.getId()))
				.andExpect(status().isNotFound());

		//Test re-create user for cache
		// invalidation

		expectedUser = userService.createUser(expectedUser);

		this.mockMvc.perform(get("/users/{id}", expectedUser.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

		// Change first name
		expectedUser.setFirstName("John");

		// Test update user for cache
		// invalidation
		this.mockMvc.perform(
				put("/users/{id}", expectedUser.getId()).content(
						objectMapper.writeValueAsString(expectedUser)).contentType(
						MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

		// Test that user was updated
		this.mockMvc.perform(get("/users/{id}", expectedUser.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

		// Clean up
		userService.deleteUser(expectedUser.getId());

	}

	@Test
	public void deleteUser() throws Exception{
		// Setup test data
		User expectedUser = new User("Sally", "Ride");
		expectedUser = userService.createUser(expectedUser);

		// Test getting the user to put into
		// cache
		this.mockMvc.perform(get("/users/{id}", expectedUser.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

		// Delete the user and remove from
		// cache
		this.mockMvc.perform(delete("/users/{id}", expectedUser.getId())).andExpect(
				status().isNoContent());

		// Test get user not found
		this.mockMvc.perform(get("/users/{id}", expectedUser.getId())).andExpect(
				status().isNotFound());

	}

	@Test
	void contextLoads() {
	}

}
