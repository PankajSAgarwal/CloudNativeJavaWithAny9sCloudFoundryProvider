package demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

@Component
public class CustomerResourceAssembler
        implements RepresentationModelAssembler<Customer,EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        EntityModel<Customer> customerResource = new EntityModel<>(customer);

        URI photoUri = MvcUriComponentsBuilder
                .fromMethodCall(
                        MvcUriComponentsBuilder.on(CustomerProfilePhotoRestController.class).read(
                                customer.getId())).buildAndExpand().toUri();

        URI selfUri = MvcUriComponentsBuilder
                .fromMethodCall(
                        MvcUriComponentsBuilder.on(CustomerHyperMediaRestController.class).get(
                                customer.getId())).buildAndExpand().toUri();

        customerResource.add(new Link(selfUri.toString(), "self"));
        customerResource.add(new Link(photoUri.toString(), "profile-photo"));
        return customerResource;

    }

    @Override
    public CollectionModel<EntityModel<Customer>> toCollectionModel(Iterable<? extends Customer> entities) {
        return null;
    }
}
