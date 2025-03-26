package ch.akros.order.kafka;

import ch.akros.order.controller.dto.PurchaseResponse;
import ch.akros.order.customer.CustomerResponse;
import ch.akros.order.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
