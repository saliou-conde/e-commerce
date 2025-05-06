package ch.akros.ecommerce.controller;

import ch.akros.ecommerce.controller.dto.CustomerRequest;
import ch.akros.ecommerce.controller.dto.CustomerResponse;
import ch.akros.ecommerce.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

  private final CustomerService customerService;

  @Operation(
          description = "Add Customer",
          summary = "A new Customer will be added into the database",
          responses = {
                  @ApiResponse(
                          description = "Created",
                          responseCode = "201"
                  ),
                  @ApiResponse(
                          description = "Bad Request",
                          responseCode = "400"
                  )
          }
  )
  @PostMapping
  public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {
    return ResponseEntity.ok(customerService.createCustomer(request));
  }

  @Operation(
          description = "Update Customer with the ID",
          summary = "Customer will be updated.",
          responses = {
                  @ApiResponse(
                          description = "OK",
                          responseCode = "200"
                  ),
                  @ApiResponse(
                          description = "Not Found",
                          responseCode = "404"
                  )
          }
  )
  @PutMapping
  public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request) {
    customerService.updateCustomer(request);
    return ResponseEntity.accepted().build();
  }

  @Operation(
          description = "Get all Customers",
          summary = "Display all Customers.",
          responses = {
                  @ApiResponse(
                          description = "Success",
                          responseCode = "200"
                  )
          }
  )
  @GetMapping
  public ResponseEntity<List<CustomerResponse>> getCustomers() {
    return ResponseEntity.ok(customerService.findAllCustomers());
  }

  @Operation(
          description = "Get Customer with the provided ID",
          summary = "The found Customer with the provided id will be displayed.",
          responses = {
                  @ApiResponse(
                          description = "Success",
                          responseCode = "200"
                  ),
                  @ApiResponse(
                          description = "Not Found",
                          responseCode = "404"
                  )
          }
  )
  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") String id) {
    return ResponseEntity.ok(customerService.findCustomerById(id));
  }

  @Operation(
          description = "Delete Customer with the provided ID",
          summary = "The found Customer will be deleted from the database.",
          responses = {
                  @ApiResponse(
                          description = "Success",
                          responseCode = "200"
                  ),
                  @ApiResponse(
                          description = "Not Found",
                          responseCode = "404"
                  )
          }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
    customerService.deleteCustomerById(id);
    return ResponseEntity.accepted().build();
  }
}
