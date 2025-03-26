package ch.akros.ecommerce.service;

import ch.akros.ecommerce.controller.dto.CustomerMapper;
import ch.akros.ecommerce.controller.dto.CustomerRequest;
import ch.akros.ecommerce.controller.dto.CustomerResponse;
import ch.akros.ecommerce.exception.CustomerNotFoundException;
import ch.akros.ecommerce.model.Customer;
import ch.akros.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public String createCustomer(CustomerRequest request) {
    var customer = repository.save(mapper.toCustomer(request));
    return customer.getId();
  }

  public void updateCustomer(CustomerRequest request) {
    var customer = getCustomerById(request.id(), "Cannot update customer:: No customer found with the provided Id:: %s");
    mergeCustomer(customer, request);
    repository.save(customer);
  }

  public List<CustomerResponse> findAllCustomers() {
    return repository.findAll().stream().map(mapper::fromCustomer).toList();
  }

  public CustomerResponse findCustomerById(String id) {
    var customer = getCustomerById(id, "Cannot find customer:: No customer found with the provided Id:: %s");
    return mapper.fromCustomer(customer);
  }

  public void deleteCustomerById(String id) {
    var customer = getCustomerById(id, "Cannot delete customer:: No customer found with the provided Id:: %s");
    repository.delete(customer);
  }

  private void mergeCustomer(Customer customer, CustomerRequest request) {
    if (StringUtils.isNotBlank(request.firstName())) {
      customer.setFirstName(request.firstName());
    }
    if (StringUtils.isNotBlank(request.lastName())) {
      customer.setLastName(request.lastName());
    }
    if (StringUtils.isNotBlank(request.email())) {
      customer.setEmail(request.email());
    }
    if (request.address() != null) {
      customer.setAddress(request.address());
    }
  }

  private Customer getCustomerById(String id, String errorMessage) {
    return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(format(errorMessage, id)));
  }
}
