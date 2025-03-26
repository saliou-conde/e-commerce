package ch.akros.order.service;

import ch.akros.order.controller.dto.*;
import ch.akros.order.customer.CustomerClient;
import ch.akros.order.customer.CustomerResponse;
import ch.akros.order.exception.BusinessException;
import ch.akros.order.exception.OrderNotException;
import ch.akros.order.exception.PaymentNotPossibleException;
import ch.akros.order.kafka.OrderConfirmation;
import ch.akros.order.kafka.OrderProducer;
import ch.akros.order.model.Order;
import ch.akros.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final OrderRepository orderRepository;
  private final OrderLineService orderLineService;
  private final OrderMapper mapper;
  private final CustomerClient customerClient;
  private final ProductClient productClient;
  private final OrderProducer orderProducer;
  private final PaymentClient paymentClient;
  private Double totalPrice;

  public Integer createOrder(OrderRequest orderRequest) {

    var customerResponse = prepareCustomerResponse(orderRequest);

    var purchaseResponses = preparePurchaseResponse(orderRequest);

    var order = orderRepository.save(mapper.mapOrderRequestToOrder(orderRequest));

    saveOrderLines(order, orderRequest);

    requestOrder(orderRequest, customerResponse, order);

    orderProducer.sendOrderConfirmation(new OrderConfirmation(
            orderRequest.reference(),
            BigDecimal.valueOf(totalPrice),
            orderRequest.paymentMethod(),
            customerResponse,
            purchaseResponses
    ));

    return order.getId();
  }

  public List<OrderResponse> findAll() {
    var orders = orderRepository.findAll();
    return orders.stream().map(mapper::mapOrderToOrderResponse).toList();
  }

  public OrderResponse findOrderById(Integer id) {
    return orderRepository.findById(id)
            .map(mapper::mapOrderToOrderResponse)
            .orElseThrow(() ->
                    new OrderNotException(String.format("Cannot find order:: No order exists with the provided ID:: %s", id)));
  }

  private CustomerResponse prepareCustomerResponse(OrderRequest orderRequest) {
    try {
      return customerClient.findCustomerById(orderRequest.customerId())
              .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID" + orderRequest.customerId()));
    } catch (BusinessException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  private List<PurchaseResponse> preparePurchaseResponse(OrderRequest orderRequest) {
    var purchaseResponses = productClient.purchaseProduct(orderRequest.products());
    BigDecimal totalCost = purchaseResponses.stream()
            .map(p -> p.price().multiply(BigDecimal.valueOf(p.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    totalPrice = totalCost.doubleValue();
    log.info("Order price is {}", totalPrice);
    if(orderRequest.totalAmount().compareTo(totalCost) < 0) {
      throw new PaymentNotPossibleException("Order price is greater than total amount.");
    }
    return purchaseResponses;
  }

  private void saveOrderLines(Order order, OrderRequest orderRequest) {
    List<Integer> orderLineIds = new ArrayList<>();
    for (var purchaseRequest : orderRequest.products()) {
      OrderLineRequest orderLineRequest = new OrderLineRequest(
              null,
              order.getId(),
              purchaseRequest.productId(),
              purchaseRequest.quantity()

      );
      orderLineIds.add(orderLineService.saveOrderLine(orderLineRequest));
    }
    log.info("OrderLine Ids: {}", orderLineIds);
  }

  private void requestOrder(OrderRequest orderRequest, CustomerResponse customerResponse, Order order) {
    var paymentRequest = new PaymentRequest(
            BigDecimal.valueOf(totalPrice),
            orderRequest.paymentMethod(),
            order.getId(),
            orderRequest.reference(),
            customerResponse);
    Integer payRequestId =  paymentClient.requestOrder(paymentRequest);
    log.info("Payment Request Id: {}", payRequestId);
  }
}
