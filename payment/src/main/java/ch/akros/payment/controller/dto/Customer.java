package ch.akros.payment.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        String id,
        @NotBlank(message = "Customer firstName is required")
        String firstName,
        @NotBlank(message = "Customer lastName is required")
        String lastName,
        @Email(message = "Email is not correctly formatted")
        @NotBlank(message = "Customer email is not valid mail")
        String email
) {
}
