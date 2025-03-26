package ch.akros.product.repository;

import ch.akros.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

  List<Product> findAllByIdInOrderById(List<Integer> ids);
}
