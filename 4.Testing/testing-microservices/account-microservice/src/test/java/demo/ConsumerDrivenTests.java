package demo;

import demo.user.User;
import demo.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@AutoConfigureStubRunner(ids={"com.pankaj.cloudnativejava:user-microservice:+:stubs:8081"})
public class ConsumerDrivenTests {
    @Autowired
    private UserService userService;

    @Test
    public void shouldReturnAuthenticatedUser(){
        User actual = userService.getAuthenticatedUser();
        assertThat(actual).isNotNull();
        assertThat(actual.getUsername()).matches("[A-Za-z0-9]+");
        assertThat(actual.getFirstName()).matches("[A-Za-z]+");
        assertThat(actual.getLastName()).matches("[A-Za-z]+");
        assertThat(actual.getEmail()).matches(
                "[A-Za-z0-9]+\\@[A-Za-z0-9]+\\.[A-Za-z]+");


    }

}
