package demo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CustomerProtobufRestController.class)
public class CustomerProtobufRestControllerTest {

    private MediaType protobuffMediaType = ProtobufHttpMessageConverter.PROTOBUF;
    private String rootPath = "/v1/protos/customers/";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerRepository customerRepository;

    private Customer wellKnownCustomer = new Customer(1L, "Pankaj", "Agarwal");

    @Test
    public void getCustomerById() throws Exception {
        Customer existing = this.wellKnownCustomer;

        BDDMockito.when(this.customerRepository.findById(existing.getId()))
                .thenReturn(Optional.of(existing));

        // System.out.println("this.customerRepository.findById(existing.getId()) = " + this.customerRepository.findById(existing.getId()));

        MvcResult mvcResult = this.mockMvc
                .perform(get(this.rootPath + existing.getId())
                        .accept(this.protobuffMediaType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(this.protobuffMediaType)).andReturn();
        CustomerProtos.Customer customerProtobuf = CustomerProtos.Customer.parseFrom(mvcResult.getResponse().getContentAsByteArray());
        assertCustomerEquals(customerProtobuf, existing);

    }

    @Test
    public void putCustomer() throws Exception {
        Customer existing = this.wellKnownCustomer;

        given(this.customerRepository.findById(this.wellKnownCustomer.getId()))
                .willReturn(Optional.of(this.wellKnownCustomer));

        String fn = "Pankaj", ln = "Agarwal";

        CustomerProtos.Customer customer
                = CustomerProtos.Customer
                .newBuilder()
                .setId(existing.getId())
                .setFirstName(fn)
                .setLastName(ln)
                .build();

        byte[] bytesForSerializedCustomer;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            customer.writeTo(byteArrayOutputStream);
            bytesForSerializedCustomer = byteArrayOutputStream.toByteArray();
        }

        Customer updated = new Customer(this.wellKnownCustomer.getId(), fn, ln);

        given(this.customerRepository.save(updated))
                .willReturn(updated);

        given(this.customerRepository.findById(this.wellKnownCustomer.getId()))
                .willReturn(Optional.of(updated));

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(this.rootPath + existing.getId())
                        .contentType(this.protobuffMediaType)
                        .content(bytesForSerializedCustomer))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.notNullValue()));

        this.mockMvc.perform(get(this.rootPath + existing.getId()))
                .andExpect(mvcResult -> {
                    byte[] response = mvcResult.getResponse().getContentAsByteArray();
                    CustomerProtos.Customer responseCustomer = CustomerProtos.Customer.parseFrom(response);
                });
    }

    @Test
    public void postCustomer() throws Exception {
        Customer old = new Customer("Pankaj", "Agarwal");
        Customer updated = new Customer(1L, "Pankaj", "Agarwal");

        given(this.customerRepository.save(old)).willReturn(updated);
        CustomerProtos.Customer customer
                = CustomerProtos.Customer
                .newBuilder()
                .setFirstName(updated.getFirstName()).setLastName(updated.getLastName())
                .build();
        byte[] bytesForSerializedCustomer;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            customer.writeTo(byteArrayOutputStream);
            bytesForSerializedCustomer = byteArrayOutputStream.toByteArray();
        }

        this.mockMvc
                .perform(post(this.rootPath)
                        .contentType(this.protobuffMediaType)
                        .content(bytesForSerializedCustomer))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.notNullValue()));

    }

    @Test
    public void getCustomers() throws Exception {

        List<Customer> customers = Arrays.asList(this.wellKnownCustomer,
                new Customer(this.wellKnownCustomer.getId() + 1,"A","B"));

        given(this.customerRepository.findAll()).willReturn(customers);
        MvcResult mvcResult = this.mockMvc.perform(get(this.rootPath).accept(this.protobuffMediaType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(this.protobuffMediaType)).andReturn();

        CustomerProtos.Customers customerProtobuf = CustomerProtos.Customers.parseFrom(mvcResult.getResponse().getContentAsByteArray());
        assertTrue(customerProtobuf.getCustomerList().size()>0);
        CustomerProtos.Customer customer = customerProtobuf.getCustomerList().stream().filter(c -> c.getId() == this.wellKnownCustomer.getId())
                .findFirst().get();

        assertCustomerEquals(customer,this.wellKnownCustomer);

    }

    private void assertCustomerEquals(CustomerProtos.Customer customerProtobuf, Customer jpaCustomer) {
        assertEquals(jpaCustomer.getFirstName(), customerProtobuf.getFirstName());
        assertEquals(jpaCustomer.getLastName(), customerProtobuf.getLastName());
        assertEquals((long) jpaCustomer.getId(), customerProtobuf.getId());
    }

    @Configuration
    @Import(Application.class)
    public static class RestClientConfiguration {

        @Bean
        RestTemplate pbRestTemplate(ProtobufHttpMessageConverter hmc) {
            return new RestTemplate(Collections.singletonList(hmc));
        }

        @Bean
        ProtobufHttpMessageConverter protobufHttpMessageConverter() {
            return new ProtobufHttpMessageConverter();
        }
    }


}