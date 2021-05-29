package com.pankaj.cloudnative.SampleCloudFoundryApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class CustomerRestcontroller {
    private final CustomerService customerService;

    public CustomerRestcontroller(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public Collection<Customer> readAll(){
        return this.customerService.findAll();
    }
}
