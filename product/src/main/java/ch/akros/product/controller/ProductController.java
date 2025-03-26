package ch.akros.product.controller;

import ch.akros.product.controller.dto.ProductPurchaseRequest;
import ch.akros.product.controller.dto.ProductPurchaseResponse;
import ch.akros.product.controller.dto.ProductRequest;
import ch.akros.product.controller.dto.ProductResponse;
import ch.akros.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @PostMapping
  public ResponseEntity<Integer> createProduct(@Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(service.createProduct(request));
  }

  @PostMapping("/purchase")
  public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(@RequestBody List<ProductPurchaseRequest> request) {
    return ResponseEntity.ok(service.purchaseProducts(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.getProductById(id));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAllProductById() {
    return ResponseEntity.ok(service.getAllProducts());
  }

  @PutMapping
  public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(service.updateProduct(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
    service.deleteProduct(id);
    return ResponseEntity.accepted().build();
  }

}
