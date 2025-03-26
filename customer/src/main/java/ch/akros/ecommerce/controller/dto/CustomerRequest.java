package ch.akros.ecommerce.controller.dto;

import ch.akros.ecommerce.model.Address;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        String id,
        @NotBlank(message = "Customer firstName is required")
        String firstName,
        @NotBlank(message = "Customer lastName is required")
        String lastName,
        @NotBlank(message = "Customer email is not valid mail")
        String email,
        Address address
) {
}
