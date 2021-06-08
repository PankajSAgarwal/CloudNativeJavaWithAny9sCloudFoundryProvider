package edabatch.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Profile("production")
public class MashapeEmailValidationService
        implements EmailValidationService{

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

    @Override
    public boolean isEmailValid(String email) {
        UriComponents emailValidatedUri = UriComponentsBuilder.fromHttpUrl(uri).buildAndExpand(email);

        RequestEntity<Void> requestEntity
                = RequestEntity.get(emailValidatedUri.toUri())
                .header("X-Mashape-Key", mashapeKey)
                .build();

        ParameterizedTypeReference<Map<String, Boolean>> ptr
                = new ParameterizedTypeReference<>() {};

        ResponseEntity<Map<String, Boolean>> responseEntity = restTemplate.exchange(requestEntity, ptr);

        return responseEntity.getBody().get("isValid");

    }
}
