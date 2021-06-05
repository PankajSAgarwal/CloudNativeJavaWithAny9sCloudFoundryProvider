package demo;

import com.mysql.cj.exceptions.AssertionFailedException;
import demo.account.Account;
import demo.address.Address;
import demo.address.AddressType;
import demo.creditcard.CreditCard;
import demo.creditcard.CreditCardType;
import demo.customer.Customer;
import demo.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest(classes = AccountApplication.class)
@ActiveProfiles(profiles = "test")
class AccountApplicationTests {
	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void customerTest(){
		Account account = new Account("12345");
		Customer customer = new Customer("Jane", "Doe", "jane.doe@gmail.com", account);

		CreditCard creditCard = new CreditCard("1234567890", CreditCardType.VISA);
		customer.getAccount().getCrediCards().add(creditCard);

		String street1 = "Nizampet Road";
		Address address = new Address(street1,null,
				"Telengana","Hyderabad",
				"INDIA",123456,
				AddressType.SHIPPING);

		customer.getAccount().getAddresses().add(address);

		customer = customerRepository.save(customer);

		Customer persistedResult = customerRepository.findById(customer.getId())
				.orElseThrow(()-> new AssertionFailedException(new RuntimeException("there is no customer for the id")));
		Assertions.assertNotNull(persistedResult.getAccount());
		Assertions.assertNotNull(persistedResult.getCreatedAt());
		Assertions.assertNotNull(persistedResult.getLastModified());

		Assertions.assertTrue(persistedResult.getAccount().getAddresses()
				.stream().anyMatch(addr-> addr.getStreet1().equalsIgnoreCase(street1)));  //3

		customerRepository.findByEmailContaining(customer.getEmail())
				.orElseThrow(()-> new AssertionFailedException(
						new RuntimeException("there's supposed to be a matching record")));
	}

	@Test
	void contextLoads() {
	}

}
