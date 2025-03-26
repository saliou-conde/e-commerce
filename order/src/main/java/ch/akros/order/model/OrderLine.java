package ch.akros.order.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class OrderLine {

  @Id
  @GeneratedValue
  private Integer id;
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  private Integer productId;
  private Double quantity;

  public OrderLine() {
  }

  public OrderLine(Integer id, Order order, Integer productId, Double quantity) {
    this.id = id;
    this.order = order;
    this.productId = productId;
    this.quantity = quantity;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderLine orderLine = (OrderLine) o;
    return Objects.equals(id, orderLine.id) && Objects.equals(order, orderLine.order) && Objects.equals(productId, orderLine.productId) && Objects.equals(quantity, orderLine.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, order, productId, quantity);
  }

  @Override
  public String toString() {
    return "OrderLine{" +
            "id=" + id +
            ", order=" + order +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
  }
}
