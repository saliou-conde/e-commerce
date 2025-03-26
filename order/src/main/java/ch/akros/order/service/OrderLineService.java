package ch.akros.order.service;

import ch.akros.order.controller.dto.OrderLineMapper;
import ch.akros.order.controller.dto.OrderLineRequest;
import ch.akros.order.controller.dto.OrderLineResponse;
import ch.akros.order.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {
  private final OrderLineRepository repository;
  private final OrderLineMapper mapper;

  public Integer saveOrderLine(OrderLineRequest request) {
    var orderLine = repository.save(mapper.mapOrderLineRequestToOrderLine(request));
    return orderLine.getId();
  }

  public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
    return repository.findAllByOrderId(orderId).stream().map(mapper::mapOrderLineToOrderLineResponse).toList();
  }
}
