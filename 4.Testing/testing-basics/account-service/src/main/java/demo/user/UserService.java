package demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class UserService {
    private final String serviceHost;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate,
                       @Value("${user-service.host:user-service}") String serviceHost ) {
        this.serviceHost = serviceHost;
        this.restTemplate = restTemplate;
    }

    public User getAuthenticatedUser(){
        URI url = URI.create(String.format("http://%s/uaa/v1/me", serviceHost));
        RequestEntity<Void> request = RequestEntity.get(url).header(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE).build();
        return restTemplate.exchange(request,User.class).getBody();

    }
}
