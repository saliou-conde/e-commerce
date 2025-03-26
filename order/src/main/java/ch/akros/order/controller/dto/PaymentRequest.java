package ch.akros.order.controller.dto;

import ch.akros.order.customer.CustomerResponse;
import ch.akros.order.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
