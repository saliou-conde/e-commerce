package ch.akros.payment.service;

import ch.akros.payment.controller.dto.PaymentMapper;
import ch.akros.payment.controller.dto.PaymentRequest;
import ch.akros.payment.kafka.PaymentNotificationRequest;
import ch.akros.payment.kafka.PaymentProducer;
import ch.akros.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository repository;
  private final PaymentMapper mapper;
  private final PaymentProducer producer;

  public Integer createPayment(PaymentRequest request) {
    var payment = repository.save(mapper.mapPaymentRequestToPayment(request));
    producer.sendNotification(new PaymentNotificationRequest(
            request.orderReference(),
            request.amount(),
            request.paymentMethod(),
            request.customer().firstName(),
            request.customer().lastName(),
            request.customer().email()
    ));
    return payment.getId();
  }
}
