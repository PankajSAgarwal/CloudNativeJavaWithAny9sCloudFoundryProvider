package greetings;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Profile("secure")
@RestController
public class PrincipalRestController {
    @RequestMapping("/user")
    public Principal user(Principal principal){
        return principal;
    }
}
