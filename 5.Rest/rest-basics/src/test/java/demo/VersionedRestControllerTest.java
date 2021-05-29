package demo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = VersionedRestController.class)
public class VersionedRestControllerTest {
    @MockBean
    private CustomerRepository customerRepository;
    @Autowired
    private MockMvc mockMvc;

    private MediaType v1MediaType = MediaType.parseMediaType(VersionedRestController.V1_MEDIA_TYPE_VALUE);
    private MediaType v2MediaType = MediaType.parseMediaType(VersionedRestController.V2_MEDIA_TYPE_VALUE);

    @Test
    public void versioningApiWithPathVariables() throws Throwable{
        this.mockMvc.perform(get("/api/v2/hi"))
                .andExpect(jsonPath("$.version", is("v2")))
                .andExpect(jsonPath("$.how",is("path-variable")));

        this.mockMvc.perform(get("/api/v1/hi"))
                .andExpect(jsonPath("$.version",is("v1")))
                .andExpect(jsonPath("$.how",is("path-variable")));
    }

    @Test
    public void versioningApiWithContentNegotiation() throws Throwable{
        this.mockMvc.perform(get("/api/hi").accept(this.v2MediaType))
                .andExpect(jsonPath("$.version",is("v2")))
                .andExpect(jsonPath("$.how",is("content-negotiation")));

        this.mockMvc.perform(get("/api/hi").accept(this.v1MediaType))
                .andExpect(jsonPath("$.version",is("v1")))
                .andExpect(jsonPath("$.how",is("content-negotiation")));
    }

    @Test
    public void versioningApiWithHeader() throws Throwable{

        this.mockMvc.perform(get("/api/hi").header("X-Api-Version","v2"))
                .andExpect(jsonPath("$.version",is("v2")))
                .andExpect(jsonPath("$.how",is("header")));

        this.mockMvc.perform(get("/api/hi").header("X-Api-Version","v1"))
                .andExpect(jsonPath("$.version",is("v1")))
                .andExpect(jsonPath("$.how",is("header")));

    }

}