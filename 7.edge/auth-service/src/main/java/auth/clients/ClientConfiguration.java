package auth.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


@Configuration
public class ClientConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public ClientConfiguration(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

//    @Bean
//    public ClientDetailsService clientDetailsService(ClientRepository clientRepository){
//        return clientId-> clientRepository
//                .findByClientId(clientId)
//                .map(client ->{
//                    BaseClientDetails details
//                            = new BaseClientDetails(client.getClientId(),
//                            null,
//                            client.getScopes(),
//                            client.getAuthorizedGrantTypes(),
//                            client.getAuthorities());
//
//                    details.setClientSecret(client.getSecret());
//
//                    //details.setAutoApproveScopes(Arrays.asList(client.getAutoApproveScopes().split(",")));
//
//                    String greetingsClientRedirectUri
//                            = Optional.ofNullable(this.loadBalancerClient.choose("greetings-client"))
//                            .map(si -> "http://" + si.getHost() + ":" + si.getPort() + "/")
//                            .orElseThrow(()-> new ClientRegistrationException("couldn't find and bind a greetings-client IP"));
//
//                    details.setRegisteredRedirectUri(Collections.singleton(greetingsClientRedirectUri));
//                    return details;
//                })
//                .orElseThrow(() -> new ClientRegistrationException(
//                        String.format("no client %s registered",clientId)));
//
//    }
}
