package ch.akros.order.controller.dto;

import ch.akros.order.model.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String CustomerId
) {
}
