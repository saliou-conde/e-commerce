package ch.akros.ecommerce.service;

import ch.akros.ecommerce.controller.dto.CustomerMapper;
import ch.akros.ecommerce.controller.dto.CustomerRequest;
import ch.akros.ecommerce.controller.dto.CustomerResponse;
import ch.akros.ecommerce.exception.CustomerAlreadyExistException;
import ch.akros.ecommerce.exception.CustomerNotFoundException;
import ch.akros.ecommerce.model.Customer;
import ch.akros.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public String createCustomer(CustomerRequest request) {
    log.info("Starting createCustomer(CustomerRequest request)");
    var customerByEmail = repository.findByEmail(request.email());
    if(customerByEmail.isPresent()) {
      throw new CustomerAlreadyExistException(format("Customer already exists:: Customer found with the provided email:: %s", request.email()));
    }
    var customer = repository.save(mapper.toCustomer(request));
    log.info("End createCustomer(CustomerRequest request)");
    return customer.getId();
  }

  public void updateCustomer(CustomerRequest request) {
    log.info("Starting updateCustomer(CustomerRequest request)");
    var customer = getCustomerById(request.id(), "Cannot update customer:: No customer found with the provided Id:: %s");
    mergeCustomer(customer, request);
    log.info("End updateCustomer(CustomerRequest request)");
    repository.save(customer);
  }

  public List<CustomerResponse> findAllCustomers() {
    log.info("Starting findAllCustomers()");
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
