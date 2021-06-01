package greetings;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Profile({ "default", "insecure" })
@RestController
@RequestMapping(method = RequestMethod.GET,value = "/greet/{name}")
public class DefaultGreetingsRestController {
    @RequestMapping
    Map<String,String> hi(@PathVariable String name){
        return Collections.singletonMap("greetings","Hello, " + name + " !");
    }
}
