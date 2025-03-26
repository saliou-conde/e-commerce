package ch.akros.ecommerce.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {
  @NotBlank(message = "Customer street is required")
  private String street;
  @NotBlank(message = "Customer houseNumber is required")
  private String houseNumber;
  @NotBlank(message = "Customer zip code is required")
  private String zip;
}
