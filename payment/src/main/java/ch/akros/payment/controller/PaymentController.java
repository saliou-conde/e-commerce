package ch.akros.payment.controller;

import ch.akros.payment.controller.dto.PaymentRequest;
import ch.akros.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService service;

  @PostMapping
  public ResponseEntity<Integer> createPayment(@RequestBody @Valid PaymentRequest payment) {
    return ResponseEntity.ok(service.createPayment(payment));
  }
}
