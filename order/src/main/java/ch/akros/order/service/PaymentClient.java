package ch.akros.order.service;

import ch.akros.order.controller.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {

  @PostMapping
  Integer requestOrder(@RequestBody PaymentRequest paymentRequest);
}
