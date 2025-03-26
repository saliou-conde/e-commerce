package ch.akros.product.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(
        @NotBlank(message = "ProductId is required")
        Integer productId,
        @Positive(message = "quantity should be positive")
        Double quantity
) {
}
