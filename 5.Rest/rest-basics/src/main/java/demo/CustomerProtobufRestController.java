package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value="/v1/protos/customers")
public class CustomerProtobufRestController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerProtobufRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<CustomerProtos.Customer> get(@PathVariable Long id){
        return this.customerRepository
                .findById(id)
                .map(this::fromEntityToProtobuf)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new CustomerNotFoundException(id));
    }

    @GetMapping
    ResponseEntity<CustomerProtos.Customers> getCollection(){
        List<Customer> all = this.customerRepository.findAll();
        CustomerProtos.Customers customers= this.fromCollectionToProtobuf(all);
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    ResponseEntity<CustomerProtos.Customer> post(@RequestBody CustomerProtos.Customer c){

        Customer customer = this.customerRepository.save(new Customer(c.getFirstName(), c.getLastName()));
        URI uri = MvcUriComponentsBuilder
                .fromController(getClass())
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.fromEntityToProtobuf(customer));
    }

    @PutMapping("/{id}")
    ResponseEntity<CustomerProtos.Customer> put(@PathVariable Long id,@RequestBody CustomerProtos.Customer c){

        return this.customerRepository.findById(id).map(existing -> {
            Customer customer = this.customerRepository
                    .save(new Customer(existing.getId(),
                            c.getFirstName(),
                            c.getLastName()));
            URI selfLink = URI.create(fromCurrentRequest().toUriString());
            return ResponseEntity.created(selfLink).body(fromEntityToProtobuf(customer));
        }).orElseThrow(()-> new CustomerNotFoundException(id));
    }

    private CustomerProtos.Customers fromCollectionToProtobuf(Collection<Customer> customers) {

        return CustomerProtos
                .Customers
                .newBuilder()
                .addAllCustomer(customers
                        .stream()
                        .map(this::fromEntityToProtobuf)
                        .collect(Collectors.toList()))
                .build();
    }


    private CustomerProtos.Customer fromEntityToProtobuf(Customer c) {
        return fromEntityToProtobuf(c.getId(),c.getFirstName(),c.getLastName());
    }

    private CustomerProtos.Customer fromEntityToProtobuf(Long id, String firstName, String lastName) {
        CustomerProtos.Customer.Builder builder = CustomerProtos.Customer.newBuilder();
        if(id !=null && id > 0){
            builder.setId(id);
        }
        return builder.setFirstName(firstName).setLastName(lastName).build();
    }


}
