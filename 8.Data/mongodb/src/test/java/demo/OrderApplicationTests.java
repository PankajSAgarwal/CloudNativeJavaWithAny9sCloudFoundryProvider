package demo;

import demo.address.Address;
import demo.invoice.Invoice;
import demo.invoice.InvoiceRepository;
import demo.order.LineItem;
import demo.order.Order;
import demo.order.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderApplication.class)
class OrderApplicationTests {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@BeforeEach
	@AfterEach
	public void reset(){
		orderRepository.deleteAll();
		invoiceRepository.deleteAll();
	}

	@Test
	public void orderTest(){
		Address shippingAddress = new Address("kukatpally",null,
				"telangana","hyderabad",
				"India",123456);

		Order order = new Order("12345",shippingAddress);
		order.addLineItem(new LineItem("Best. Cloud. Ever. (T-Shirt, Men's Large)","SKU-123",
				1,21.19,
				.06));

		order.addLineItem(new LineItem("Like a BOSH (T-Shirt, Women's Medium)",
				"SKU-34563", 3, 14.99, .06));
		order.addLineItem(new LineItem(
				"We're gonna need a bigger VM (T-Shirt, Women's Small)", "SKU-12464", 4,
				13.99, .06));
		order.addLineItem(new LineItem("cf push awesome (Hoodie, Men's Medium)",
				"SKU-64233", 2, 21.99, .06));

		 order = orderRepository.save(order);
		 //1
		 assertNotNull(order.getOrderId());
		 assertEquals(order.getLineItems().size(),4);

		 //2

		assertEquals(order.getLastModified(),order.getCreatedAt());
		orderRepository.save(order);
		assertNotEquals(order.getLastModified(),order.getCreatedAt());

		//3

		Address billingAddress = new Address("KPHB",null,
				"telengana","hyderabad",
				"india",123456);

		String acccountNumber = "123456789";

		Invoice invoice = new Invoice(acccountNumber,billingAddress);
		invoice.addOrder(order);

		invoice = invoiceRepository.save(invoice);
		assertEquals(invoice.getOrders().size(),1);

		//4
		assertEquals(invoiceRepository.findByBillingAddress(billingAddress),invoice);




	}


	@Test
	void contextLoads() {
	}

}
