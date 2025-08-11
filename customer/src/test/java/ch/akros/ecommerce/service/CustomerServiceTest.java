package ch.akros.ecommerce.service;

import ch.akros.ecommerce.controller.dto.CustomerMapper;
import ch.akros.ecommerce.controller.dto.CustomerRequest;
import ch.akros.ecommerce.controller.dto.CustomerResponse;
import ch.akros.ecommerce.exception.CustomerAlreadyExistException;
import ch.akros.ecommerce.exception.CustomerNotFoundException;
import ch.akros.ecommerce.model.Address;
import ch.akros.ecommerce.model.Customer;
import ch.akros.ecommerce.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService service;
    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;
    private Address address;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .street("Hauptstrasse")
                .houseNumber("42a")
                .zip("8000")
                .build();
        openMocks(this);
    }

    @Test
    void shouldCreateCustomer_whenEmailNotExists() {
        //Given
        CustomerRequest request = new CustomerRequest("id1", "Max", "Mustermann", "max@example.com", address);
        Customer customer = new Customer();
        customer.setId("id1");

        when(repository.findByEmail("max@example.com")).thenReturn(Optional.empty());
        when(mapper.toCustomer(request)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);

        //When
        String resultId = service.createCustomer(request);

        //Then
        assertThat(resultId).isEqualTo("id1");

        //Verify
        verify(repository).save(customer);
    }

    @Test
    void shouldThrowException_whenEmailAlreadyExists() {
        //Given
        var invalidAddress = Address.builder()
                .street("")  // NotBlank verletzt
                .houseNumber("")
                .zip("")
                .build();
        CustomerRequest request = new CustomerRequest("id1", "Max", "Mustermann", "max@example.com", invalidAddress);

        when(repository.findByEmail("max@example.com"))
                .thenReturn(Optional.of(new Customer()));

        assertThatThrownBy(() -> service.createCustomer(request))
                .isInstanceOf(CustomerAlreadyExistException.class)
                .hasMessageContaining("Customer already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateCustomer_whenIdExists() {
        CustomerRequest request = new CustomerRequest("id1", "Max", "Mustermann", "max@example.com", address);
        Customer existingCustomer = new Customer();
        existingCustomer.setId("id1");

        when(repository.findById("id1")).thenReturn(Optional.of(existingCustomer));

        service.updateCustomer(request);

        assertThat(existingCustomer.getFirstName()).isEqualTo("Max");
        assertThat(existingCustomer.getLastName()).isEqualTo("Mustermann");
        assertThat(existingCustomer.getEmail()).isEqualTo("max@example.com");
        assertThat(existingCustomer.getAddress()).isEqualTo(address);

        verify(repository).save(existingCustomer);
    }

    @Test
    void shouldThrowException_whenUpdateCustomerNotFound() {
        CustomerRequest request = new CustomerRequest("id42", "Max", "Mustermann", "max@example.com", address);

        when(repository.findById("id42")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCustomer(request)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldReturnAllCustomers() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        when(repository.findAll()).thenReturn(List.of(customer1, customer2));

        CustomerResponse response1 = new CustomerResponse("id1", "Max", "Mustermann", "max@example.com", address);
        CustomerResponse response2 = new CustomerResponse("id2", "Anna", "Musterfrau", "anna@example.com", address);

        when(mapper.fromCustomer(customer1)).thenReturn(response1);
        when(mapper.fromCustomer(customer2)).thenReturn(response2);

        List<CustomerResponse> result = service.findAllCustomers();

        assertThat(result).containsExactly(response1, response2);
    }

    @Test
    void shouldReturnCustomerById() {
        Customer customer = new Customer();
        customer.setId("id1");

        CustomerResponse response = new CustomerResponse("id1", "Max", "Mustermann", "max@example.com", address);

        when(repository.findById("id1")).thenReturn(Optional.of(customer));
        when(mapper.fromCustomer(customer)).thenReturn(response);

        CustomerResponse result = service.findCustomerById("id1");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowException_whenCustomerByIdNotFound() {
        when(repository.findById("id42")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findCustomerById("id42")).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldDeleteCustomerById() {
        Customer customer = new Customer();
        customer.setId("id1");

        when(repository.findById("id1")).thenReturn(Optional.of(customer));

        service.deleteCustomerById("id1");

        verify(repository).delete(customer);
    }

    @Test
    void shouldThrowException_whenDeleteCustomerNotFound() {
        when(repository.findById("id99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteCustomerById("id99"))
                .isInstanceOf(CustomerNotFoundException.class);

        verify(repository, never()).delete(any());
    }
}
