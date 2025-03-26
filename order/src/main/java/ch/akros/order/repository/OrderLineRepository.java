package ch.akros.order.repository;

import ch.akros.order.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
  List<OrderLine> findAllByOrderId(Integer orderId);

}
