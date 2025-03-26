package ch.akros.order.controller.dto;

import ch.akros.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
  public Order mapOrderRequestToOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setId(orderRequest.id());
    order.setReference(orderRequest.reference());
    order.setCustomerId(orderRequest.customerId());
    order.setTotalAmount(orderRequest.totalAmount());
    order.setPaymentMethod(orderRequest.paymentMethod());
    return order;
  }

  public OrderResponse mapOrderToOrderResponse(Order order) {
    return new OrderResponse(
            order.getId(),
            order.getReference(),
            order.getTotalAmount(),
            order.getPaymentMethod(),
            order.getCustomerId()
    );
  }
}
