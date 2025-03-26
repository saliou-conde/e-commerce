package ch.akros.payment.controller.dto;

import ch.akros.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
  public Payment mapPaymentRequestToPayment(PaymentRequest request) {
    return Payment.builder()
            .id(request.id())
            .amount(request.amount())
            .orderId(request.orderId())
            .paymentMethod(request.paymentMethod())
            .build();
  }

}
