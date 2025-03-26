package ch.akros.product.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public class ProductProductPurchaseException extends RuntimeException {
  private final String message;
}
