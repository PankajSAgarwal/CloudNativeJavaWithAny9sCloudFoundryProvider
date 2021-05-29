package demo.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.junit.jupiter.api.Assertions.*;

@RestClientTest({ UserService.class })
@AutoConfigureWebClient(registerRestTemplate = true)
public class UserServiceTest {

    @Value("${user-service.host:user-service}")
    private String serviceHost;

    @Autowired
    private UserService userService;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void getAuthenticatedUserShouldReturnUser(){

        this.server.expect(
                MockRestRequestMatchers
                        .requestTo(String.format("http://%s/uaa/v1/me",serviceHost)))
                .andRespond(MockRestResponseCreators.withSuccess(
                        new ClassPathResource("user.json",getClass()), MediaType.APPLICATION_JSON));
        User user = userService.getAuthenticatedUser();

        Assertions.assertThat(user.getUsername()).isEqualTo("user");
        Assertions.assertThat(user.getFirstName()).isEqualTo("John");
        Assertions.assertThat(user.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(user.getCreatedAt()).isEqualTo(12345);
        Assertions.assertThat(user.getLastModified()).isEqualTo(12346);
        Assertions.assertThat(user.getId()).isEqualTo(0L);
    }



}