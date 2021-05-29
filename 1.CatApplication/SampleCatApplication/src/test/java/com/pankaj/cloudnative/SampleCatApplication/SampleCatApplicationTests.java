package com.pankaj.cloudnative.SampleCatApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SampleCatApplicationTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private CatRepository catRepository;

	@BeforeEach
	public void before() throws Exception{
		Stream.of("Felix","Garfield","Whiskers")
				.forEach(n-> catRepository.save(new Cat(n)));
	}
	@Test
	public void catReflectedInRead() throws Exception {
		MediaType halJson = MediaType.parseMediaType("application/hal+json");
		this.mvc.perform(get("/cats"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(halJson))
				.andExpect(mvcResult -> {
					String contentAsString = mvcResult.getResponse().getContentAsString();
					System.out.println("contentAsString = " + contentAsString); // a json of all cats
					System.out.println("totalelements--->" + contentAsString
							.split("totalElements")[1]
							.split(":")[1].trim()
							.split(",")[0]); // output 3. totalElements is a field in the contentAsString json
					assertTrue(contentAsString
							.split("totalElements")[1]
							.split(":")[1].trim()
							.split(",")[0].equals("3"));
				});
	}

	@Test
	void contextLoads() {
	}

}
