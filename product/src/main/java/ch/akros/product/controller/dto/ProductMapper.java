package ch.akros.product.controller.dto;

import ch.akros.product.model.Category;
import ch.akros.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

  public Product mapProductRequestToProduct(ProductRequest request) {
    Product product = new Product();
    product.setId(request.id());
    product.setName(request.name());
    product.setPrice(request.price());
    Category category = new Category();
    category.setId(request.categoryId());
    product.setCategory(category);
    product.setDescription(request.description());
    product.setAvailableQuantity(request.availableQuantity());
    return product;
  }

  public ProductResponse mapProductToProductResponse(Product product) {
    return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
            product.getAvailableQuantity(), product.getPrice(), product.getCategory().getName(), product.getCategory().getId());
  }

  public ProductPurchaseResponse mapProductToPurchaseResponse(Product product, double quantity) {
    return new ProductPurchaseResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), quantity);
  }
}
