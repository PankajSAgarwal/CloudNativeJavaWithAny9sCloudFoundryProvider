package demo.user;

import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

@Service
public class AuthService {
    public Principal getAuthenticatedUser(Principal principal) {
        // Retrieves a dummy user principal
        // for this example project
        return principal == null ? (UserPrincipal) () -> "user" : principal;
    }
}
