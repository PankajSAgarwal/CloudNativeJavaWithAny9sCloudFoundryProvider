package com.example;

import com.google.common.cache.RemovalListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class LoadBalancedClientRunner implements ApplicationRunner {
    private final Log log = LogFactory.getLog(getClass());
    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancedClientRunner(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LoadBalancerRequest<URI> loadBalancerRequest =
                server -> URI.create("http://" + server.getHost() + ":" + server.getPort() + "/");

        URI uri = this.loadBalancerClient.execute("greetings-service",loadBalancerRequest);
        log.info("resolved service " + uri.toString());
    }
}
