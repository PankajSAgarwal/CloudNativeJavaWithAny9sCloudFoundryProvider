package demo;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class VersionedRestController {
    public static final String V1_MEDIA_TYPE_VALUE = "application/vnd.bootiful.demo-v1+json";
    public static final String V2_MEDIA_TYPE_VALUE ="application/vnd.bootiful.demo-v2+json";

    private enum ApiVersion{
        v1,v2
    }

    public static class Greeting {
        private String how;
        private String version;

        public Greeting(String how, ApiVersion version){
            this.how=how;
            this.version=version.toString();
        }

        public String getHow() {
            return how;
        }

        public String getVersion() {
            return version;
        }
    }
    // Test it using curl http://localhost:8080/api/V2/hi

    @GetMapping(value="/{version}/hi",produces = APPLICATION_JSON_VALUE)
    Greeting greetWithPathVariable(@PathVariable ApiVersion version){
        return greet(version,"path-variable");
    }

    // Test it using curl -H "X -API-Version:V1" http://localhost:8080/api/hi
    @GetMapping(value="/hi", produces = APPLICATION_JSON_VALUE)
    Greeting greetWithHeader(@RequestHeader("X-API-Version") ApiVersion version){
        return this.greet(version,"header");
    }

    // Test it using curl -H "Accept:application/vnd.bootiful.demo-V1+json" http://localhost:8080/api/hi
    @GetMapping(value = "/hi", produces = V1_MEDIA_TYPE_VALUE)
    Greeting greetWithContentNegotiationV1(){
        return this.greet(ApiVersion.v1,"content-negotiation");
    }
    // Test it using curl -H "Accept:application/vnd.bootiful.demo-V2+json" http://localhost:8080/api/hi
    @GetMapping(value="/hi", produces = V2_MEDIA_TYPE_VALUE)
    Greeting greetWithContentNegotiationV2(){
        return this.greet(ApiVersion.v2,"content-negotiation");
    }

    private Greeting greet(ApiVersion version, String how) {
        return new Greeting(how,version);

    }


}
