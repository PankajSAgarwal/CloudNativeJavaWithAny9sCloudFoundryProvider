package processing.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class MashapeEmailValidationService implements EmailValidationService {

    private final String mashapeKey;
    private final RestTemplate restTemplate;
    private final String uri;

    public MashapeEmailValidationService(@Value("${mashape.key}") String key,
                                         @Value("${email-validator.uri}") String uri,
                                         RestTemplate restTemplate) {
        this.mashapeKey = key;
        this.restTemplate = restTemplate;
        this.uri = uri;
    }

public boolean isEmailValid(String email){
    UriComponents emailValidatedUri = UriComponentsBuilder
            .fromHttpUrl(uri)
            .buildAndExpand(email);

    RequestEntity<Void> requestEntity = RequestEntity.get(emailValidatedUri.toUri())
            .header("X-Mashape-key",mashapeKey).build();
    ParameterizedTypeReference<Map<String,Boolean>> ptr
            = new ParameterizedTypeReference<Map<String, Boolean>>() {
    };

    ResponseEntity<Map<String, Boolean>> responseEntity
            = restTemplate.exchange(requestEntity, ptr);

    return responseEntity.getBody().get("isValid");
}


}
