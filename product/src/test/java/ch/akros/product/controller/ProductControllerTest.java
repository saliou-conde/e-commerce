package ch.akros.product.controller;

import ch.akros.product.controller.dto.ProductPurchaseRequest;
import ch.akros.product.controller.dto.ProductPurchaseResponse;
import ch.akros.product.controller.dto.ProductRequest;
import ch.akros.product.controller.dto.ProductResponse;
import ch.akros.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.ACCEPTED;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  @Mock
  private ProductService service;

  @InjectMocks
  private ProductController controller;

  private ProductRequest productRequest;
  private ProductResponse productResponse;

  @BeforeEach
  void setUp() {
    productRequest = new ProductRequest(
            1,
            "Product Name",
            "Description",
            100.0,
            BigDecimal.valueOf(24.99),
            1
    );
    productResponse = new ProductResponse(
            1,
            "Product Name",
            "Description",
            100.0,
            productRequest.price(),
            "CategoryName",
            productRequest.categoryId()
    );
  }

  @Test
  void testCreateProduct() {
    //Given
    when(service.createProduct(productRequest)).thenReturn(1);

    //When
    var response = controller.createProduct(productRequest);

    //Then
    assertThat(response.getBody()).isEqualTo(1);

    //Verify
    verify(service, times(1)).createProduct(productRequest);
  }

  @Test
  void testPurchaseProducts() {
    //Given
    List<ProductPurchaseRequest> request = List.of(new ProductPurchaseRequest(1, 2.));
    List<ProductPurchaseResponse> responseList = List.of(new ProductPurchaseResponse(1,
            "Product Name",
            "Description",
            productRequest.price(),
            100.
            ));

    when(service.purchaseProducts(request)).thenReturn(responseList);

    //When
    var response = controller.purchaseProducts(request);

    //Then
    assertThat(response.getBody()).hasSize(1);

    //Verify
    verify(service, times(1)).purchaseProducts(request);
  }

  @Test
  void testGetProductById() {
    //Given
    when(service.getProductById(1)).thenReturn(productResponse);

    //When
    var response = controller.getProductById(1);

    //Then
    assertThat(response.getBody().id()).isEqualTo(1);
  }

  @Test
  void testGetAllProducts() {
    //Given
    List<ProductResponse> productList = Collections.singletonList(productResponse);
    when(service.getAllProducts()).thenReturn(productList);

    //When
    var response = controller.getAllProductById();

    //Then
    assertThat(response.getBody()).hasSize(1);

    //Verify
    verify(service, times(1)).getAllProducts();
  }

  @Test
  void testUpdateProduct() {
    //Given
    when(service.updateProduct(productRequest)).thenReturn(productResponse);

    //When
    var response = controller.updateProduct(productRequest);

    //Then
    assertThat(response.getBody().id()).isEqualTo(1);

    //Verify
    verify(service, times(1)).updateProduct(productRequest);
  }

  @Test
  void testDeleteProduct() {
    //Given
    Integer id = productResponse.id();
    doNothing().when(service).deleteProduct(id);

    //When
    var response = controller.deleteProduct(id);

    //Then
    assertThat(response.getStatusCode()).isEqualTo(ACCEPTED);

    //Verify
    verify(service, times(1)).deleteProduct(id);
  }
}