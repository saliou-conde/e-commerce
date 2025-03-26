package ch.akros.order.controller;

import ch.akros.order.controller.dto.OrderRequest;
import ch.akros.order.controller.dto.OrderResponse;
import ch.akros.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService service;

  @PostMapping
  public ResponseEntity<Integer> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
    return ResponseEntity.ok(service.createOrder(orderRequest));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> findOrderById(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(service.findOrderById(id));
  }

}
