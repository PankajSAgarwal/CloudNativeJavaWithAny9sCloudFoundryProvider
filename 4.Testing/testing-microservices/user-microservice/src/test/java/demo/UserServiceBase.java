package demo;

import demo.user.AuthService;
import demo.user.User;
import demo.user.UserController;
import demo.user.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

import static org.mockito.BDDMockito.given;

public class UserServiceBase {
    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup(){
        User actual = new User("user", "Jack", "Frost", "jfrost@example.com");
        actual.setLastModified(12345L);
        actual.setCreatedAt(12345L);
        actual.setId(0L);

        given(this.userService.getUserByPrincipal(() -> "user")).willReturn(actual);

        given(this.authService.getAuthenticatedUser(null))
                .willReturn((UserPrincipal) () -> "user");

        RestAssuredMockMvc.standaloneSetup(new UserController(userService,authService));
    }

    public void assertThatRejectionReasonIsNull(Object rejectionReason) {
        assert rejectionReason == null;
    }
}
