package ch.akros.ecommerce.controller;

import ch.akros.ecommerce.controller.dto.CustomerRequest;
import ch.akros.ecommerce.exception.CustomerAlreadyExistException;
import ch.akros.ecommerce.exception.CustomerNotFoundException;
import ch.akros.ecommerce.model.Address;
import ch.akros.ecommerce.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerRequest validRequest() {
        Address address = Address.builder()
                .street("Teststrasse")
                .houseNumber("12")
                .zip("12345")
                .build();

        return new CustomerRequest("id1", "Max", "Mustermann", "max@example.com", address);
    }

    @Test
    void shouldReturn400_whenEmailAlreadyExists() throws Exception {
        CustomerRequest request = validRequest();
        when(customerService.createCustomer(request)).thenThrow(
                new CustomerAlreadyExistException("Customer with email already exists"));

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Customer already exists in the database"))
                .andExpect(jsonPath("$.detail", containsString("already exists")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn404_whenCustomerNotFound() throws Exception {
        when(customerService.findCustomerById("id42")).thenThrow(
                new CustomerNotFoundException("Customer with id not found"));

        mockMvc.perform(get("/api/v1/customers/id42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Customer does not exist in the database"))
                .andExpect(jsonPath("$.detail", containsString("not found")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn400_whenValidationFails() throws Exception {
        CustomerRequest validRequest = validRequest();
        var inValidRequest = new CustomerRequest(validRequest.id(),
                validRequest.firstName(),
                validRequest.lastName(),
                "", // violates @NotBlank
                validRequest.address());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                //.andExpect(jsonPath("$.properties.email", containsString("must not be blank")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
