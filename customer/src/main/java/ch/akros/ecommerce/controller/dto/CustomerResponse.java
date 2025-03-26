package ch.akros.ecommerce.controller.dto;

import ch.akros.ecommerce.model.Address;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
