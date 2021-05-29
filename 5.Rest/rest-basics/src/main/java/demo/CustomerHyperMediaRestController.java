package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//1
@RestController
@RequestMapping(value="/v2", produces = "application/hal+json")
public class CustomerHyperMediaRestController {
    private final CustomerResourceAssembler customerResourceAssembler;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerHyperMediaRestController(CustomerResourceAssembler customerResourceAssembler, CustomerRepository customerRepository) {
        this.customerResourceAssembler = customerResourceAssembler;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    ResponseEntity<CollectionModel<Object>> root(){
        CollectionModel<Object> objects = new CollectionModel<>(Collections.emptyList());
        URI uri= MvcUriComponentsBuilder
                .fromMethodCall(MvcUriComponentsBuilder.on(getClass()).getCollection())
                .build().toUri();
        Link link = new Link(uri.toString(), "customers");
        objects.add(link);
        return ResponseEntity.ok(objects);

    }

    @GetMapping("/customers")
    ResponseEntity<CollectionModel<EntityModel<Customer>>> getCollection() {
        List<EntityModel<Customer>> collect = this.customerRepository.findAll().stream()
                .map(customerResourceAssembler::toModel)
                .collect(Collectors.<EntityModel<Customer>>toList());
        CollectionModel<EntityModel<Customer>> resources = new CollectionModel<>(collect);
        URI self = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        resources.add(new Link(self.toString(), "self"));
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.HEAD, HttpMethod.OPTIONS,
                        HttpMethod.PUT, HttpMethod.DELETE).build();
    }

    @GetMapping(value = "/customers/{id}")
    ResponseEntity<EntityModel<Customer>> get(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(c -> ResponseEntity.ok(this.customerResourceAssembler.toModel(c)))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PostMapping(value = "/customers")
    ResponseEntity<EntityModel<Customer>> post(@RequestBody Customer c) {
        Customer customer = this.customerRepository.save(new Customer(c
                .getFirstName(), c.getLastName()));
        URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/customers/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(
                this.customerResourceAssembler.toModel(customer));
    }

    @DeleteMapping(value = "/customers/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        return this.customerRepository.findById(id).map(c -> {
            customerRepository.delete(c);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.HEAD)
    ResponseEntity<?> head(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(exists -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PutMapping("/customers/{id}")
    ResponseEntity<EntityModel<Customer>> put(@PathVariable Long id,
                                           @RequestBody Customer c) {
        Customer customer = this.customerRepository.save(new Customer(id, c
                .getFirstName(), c.getLastName()));
        EntityModel<Customer> customerResource = this.customerResourceAssembler
                .toModel(customer);
        URI selfLink = URI.create(ServletUriComponentsBuilder.fromCurrentRequest()
                .toUriString());
        return ResponseEntity.created(selfLink).body(customerResource);
    }
}
