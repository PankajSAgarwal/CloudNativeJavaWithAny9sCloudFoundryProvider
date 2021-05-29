package demo.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.attribute.UserPrincipal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @Test
    public void getUserShouldReturnUser() throws Exception {
        String content = "{\"username\": \"user\", \"firstName\": \"Jack\", \"lastName\": \"Frost\", \"email\": \"jfrost@example.com\"}";

        given(this.userService.getUserByPrincipal((UserPrincipal) () -> "user"))
                .willReturn(new User("user", "Jack", "Frost", "jfrost@example.com"));

        given(this.authService.getAuthenticatedUser(null)).willReturn(
                (UserPrincipal) () -> "user");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/uaa/v1/me")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(content));
    }


}