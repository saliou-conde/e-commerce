package ch.akros.ecommerce.controller.dto;

import ch.akros.ecommerce.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
  public Customer toCustomer(CustomerRequest request) {
    return new Customer(request.id(), request.firstName(), request.lastName(), request.email(), request.address());
  }

  public CustomerResponse fromCustomer(Customer customer) {
    return new CustomerResponse(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getAddress());
  }
}
