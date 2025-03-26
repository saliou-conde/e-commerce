package ch.akros.payment.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Payment {

  @Id
  @GeneratedValue
  private Integer id;
  private BigDecimal amount;
  @Enumerated(STRING)
  private PaymentMethod paymentMethod;
  private Integer orderId;
  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDate createdDate;
  @LastModifiedDate
  @Column(insertable = false)
  private LocalDate lastModifiedDate;

}
