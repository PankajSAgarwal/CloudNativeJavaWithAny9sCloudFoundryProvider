package greetings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
//1
@Profile("feign")
@RestController
@RequestMapping("/api")
public class FeignGreetingsClientApiGateway {
    private final GreetingsClient greetingsClient;

    @Autowired
    public FeignGreetingsClientApiGateway(GreetingsClient greetingsClient) {
        this.greetingsClient = greetingsClient;
    }
    //2
    @GetMapping("/feign/{name}")
    Map<String,String> feign(@PathVariable String name){
        return this.greetingsClient.greet(name);


    }
}
