package ch.akros.order.controller.dto;

import ch.akros.order.model.Order;
import ch.akros.order.model.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {
  public OrderLine mapOrderLineRequestToOrderLine(OrderLineRequest request) {
    OrderLine orderLine = new OrderLine();
    orderLine.setId(request.id());
    orderLine.setQuantity(request.quantity());
    orderLine.setProductId(request.productId());
    Order order = new Order();
    order.setId(request.orderId());
    orderLine.setOrder(order);
    return orderLine;
  }

  public OrderLineResponse mapOrderLineToOrderLineResponse(OrderLine orderLine) {
    return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
  }
}
