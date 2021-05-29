package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class DiscoveryClientRunner implements ApplicationRunner {
    private final Log log = LogFactory.getLog(getClass());
    private final DiscoveryClient discoveryClient;

    public DiscoveryClientRunner(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private void logServiceInstance(ServiceInstance si){
        String msg = String.format("host=%s,port=%s,service ID = %s",
                si.getHost(),
                si.getPort(),
                si.getServiceId());
        log.info(msg);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //this.log.info("localserviceinstance");
        //this.logServiceInstance(this.discoveryClient.get);
        String serviceId="greetings-service";
        this.log.info(String.format("registered instances of '%s'",serviceId));
        this.discoveryClient.getInstances(serviceId)
                .forEach(this::logServiceInstance);

    }

//    @Override
//    public void run(String... args) throws Exception {
//        this.log.info("localserviceinstance");
//        this.logServiceInstance(this.discoveryClient.getLocalServiceInstance());
//          String serviceId="greetings-service";
//          this.log.info(String.format("registered instances of '%s'",serviceId));
//          this.discoveryClient.getInstances(serviceId)
//            .forEach(this::logServiceInstance);
//
//    }
}
