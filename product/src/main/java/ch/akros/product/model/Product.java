package ch.akros.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
  @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
  private Integer id;
  private String name;
  private String description;
  private Double availableQuantity;
  private BigDecimal price;
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

}
