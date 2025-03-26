package ch.akros.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer_order")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

  @Id
  @GeneratedValue
  private Integer id;
  private String reference;
  private BigDecimal totalAmount;
  @Enumerated(STRING)
  private PaymentMethod paymentMethod;
  private String customerId;
  @OneToMany(mappedBy = "order")
  private List<OrderLine> orderLines;
  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDate createdDate;
  @LastModifiedDate
  @Column(insertable = false)
  private LocalDate lastModifiedDate;
}
