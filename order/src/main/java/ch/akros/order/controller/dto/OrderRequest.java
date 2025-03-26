package ch.akros.order.controller.dto;

import ch.akros.order.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal totalAmount,
        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,
        @NotBlank(message = "Customer should be present")
        String customerId,
        @NotEmpty(message = "You should at least purchase one product")
        List<PurchaseRequest> products
        ) {
}
