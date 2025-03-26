package ch.akros.product.service;

import ch.akros.product.controller.dto.*;
import ch.akros.product.exception.ProductNotFoundException;
import ch.akros.product.exception.ProductProductPurchaseException;
import ch.akros.product.model.Product;
import ch.akros.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repository;
  private final ProductMapper mapper;

  public Integer createProduct(ProductRequest request) {
    var product = repository.save(mapper.mapProductRequestToProduct(request));
    return product.getId();
  }

  public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
    var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
    var storedProducts = repository.findAllByIdInOrderById(productIds);
    if (storedProducts.size() != request.size()) {
      throw new ProductProductPurchaseException(format("One or more products does not exists '%s'", productIds));
    }
    var sortedRequest = request.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();
    var responses = new ArrayList<ProductPurchaseResponse>();

    for (int i = 0; i < storedProducts.size(); i++) {
      var product = storedProducts.get(i);
      var productRequest = sortedRequest.get(i);
      if (product.getAvailableQuantity() < productRequest.quantity()) {
        throw new ProductProductPurchaseException(format("Insufficient stock quantity for product with ID:: '%s'", product.getId()));
      }
      var availableQuantity = product.getAvailableQuantity() - productRequest.quantity();
      product.setAvailableQuantity(availableQuantity);
      repository.save(product);
      responses.add(mapper.mapProductToPurchaseResponse(product, productRequest.quantity()));
    }
    return responses;
  }

  public ProductResponse getProductById(Integer id) {
    var product = findProductById(id, "Cannot find product:: No product found with the provided Id:: %s");
    return mapper.mapProductToProductResponse(product);
  }

  public List<ProductResponse> getAllProducts() {
    return repository.findAll().stream().map(mapper::mapProductToProductResponse).toList();
  }

  public ProductResponse updateProduct(ProductRequest request) {
    var findProductById = findProductById(request.id(), "Cannot update product:: No product found with the provided Id:: %s");
    mergeProduct(findProductById, request);
    var updatedProduct = repository.save(findProductById);
    return mapper.mapProductToProductResponse(updatedProduct);
  }

  public void deleteProduct(Integer id) {
    var findProductById = findProductById(id, "Cannot delete product:: No product found with the provided Id:: %s");
    repository.deleteById(findProductById.getId());
  }

  private void mergeProduct(Product product, ProductRequest request) {
    if (StringUtils.isNotBlank(request.name())) {
      product.setName(request.name());
    }
    if (StringUtils.isNotBlank(request.description())) {
      product.setDescription(request.description());
    }
    if (request.availableQuantity() != null) {
      product.setAvailableQuantity(request.availableQuantity());
    }
    if (request.price() != null) {
      product.setPrice(request.price());
    }
    if (request.categoryId() != null) {
      product.getCategory().setId(request.categoryId());
    }
  }

  private Product findProductById(Integer id, String errorMessage) {
    return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(format(errorMessage, id)));
  }
}
