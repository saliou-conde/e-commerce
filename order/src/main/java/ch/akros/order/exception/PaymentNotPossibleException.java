package ch.akros.order.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public class PaymentNotPossibleException extends RuntimeException {
  private final String message;
}
