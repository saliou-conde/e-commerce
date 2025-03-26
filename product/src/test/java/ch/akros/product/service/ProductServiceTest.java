package ch.akros.product.service;

import ch.akros.product.controller.dto.*;
import ch.akros.product.exception.ProductNotFoundException;
import ch.akros.product.exception.ProductProductPurchaseException;
import ch.akros.product.model.Category;
import ch.akros.product.model.Product;
import ch.akros.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceTest {

  @InjectMocks
  private ProductService productService;
  @Mock
  private ProductRepository repository;
  @Mock
  private ProductMapper mapper;

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void testCreateProduct() {
    //Given
    ProductRequest request = new ProductRequest(1,
            "Wireless Compact Keyboard 1",
            "Wireless compact keyboard",
            1000.0,
            BigDecimal.valueOf(79.99),
            1
    );

    Category category = new Category();
    category.setId(1);
    category.setName("Wireless Compact Keyboard 1");
    category.setDescription("Wireless compact keyboard");
    Product product = Product.builder()
            .id(request.id())
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(category)
            .build();

    when(mapper.mapProductRequestToProduct(request)).thenReturn(product);
    when(repository.findById(anyInt())).thenReturn(null);
    when(repository.save(any())).thenReturn(product);

    //When
    Integer responseDto = productService.createProduct(request);

    //Then
    assertThat(responseDto).isNotNull().isEqualTo(1);

    //Verify
    verify(repository, times(1)).save(any());

  }

  @Test
  void testPurchaseProductsSuccess() {
    //Given
    ProductPurchaseRequest purchaseRequest1 = new ProductPurchaseRequest(
            1, 1.0
    );
    ProductPurchaseRequest purchaseRequest2 = new ProductPurchaseRequest(2, 2.);
    List<ProductPurchaseRequest> purchaseRequests = List.of(purchaseRequest1, purchaseRequest2);

    Product product1 = Product.builder()
            .id(purchaseRequest1.productId())
            .availableQuantity(10.)
            .description("Product description 1")
            .name("Product name 1")
            .price(BigDecimal.valueOf(24.99))
            .category(new Category(
                    purchaseRequest1.productId(),
                    "Product description 1",
                    "Product name 1",
                    List.of()

            ))
            .build();

    Product product2 = Product.builder()
            .id(purchaseRequest1.productId())
            .availableQuantity(10.)
            .description("Product description 2")
            .name("Product name 2")
            .price(BigDecimal.valueOf(34.99))
            .category(new Category(
                    purchaseRequest1.productId(),
                    "Product description 2",
                    "Product name 2",
                    List.of()

            ))
            .build();

    when(repository.findAllByIdInOrderById(anyList()))
            .thenReturn(List.of(product1, product2));
    when(repository.save(product1))
            .thenReturn(product1);
    when(repository.save(product2))
            .thenReturn(product2);
    when(mapper.mapProductToPurchaseResponse(product1, purchaseRequest1.quantity()))
            .thenReturn(new ProductPurchaseResponse(
                    purchaseRequest1.productId(),
                    product1.getName(),
                    product1.getDescription(),
                    product1.getPrice(),
                    purchaseRequest1.quantity()

            ));
    when(mapper.mapProductToPurchaseResponse(product2, purchaseRequest2.quantity()))
            .thenReturn(new ProductPurchaseResponse(
                    purchaseRequest2.productId(),
                    product2.getName(),
                    product2.getDescription(),
                    product2.getPrice(),
                    purchaseRequest2.quantity()

            ));

    //When
    var response = productService.purchaseProducts(purchaseRequests);

    //Then
    assertThat(response).isNotNull().hasSize(2);
  }

  @Test
  void testPurchaseProductsWithOneOrMoreProductsDoesNotExists() {
    //Given
    ProductPurchaseRequest purchaseRequest1 = new ProductPurchaseRequest(
            1, 1.0
    );
    ProductPurchaseRequest purchaseRequest2 = new ProductPurchaseRequest(2, 2.);
    List<ProductPurchaseRequest> purchaseRequests = List.of(purchaseRequest1, purchaseRequest2);

    Product product1 = Product.builder()
            .id(purchaseRequest1.productId())
            .availableQuantity(10.)
            .description("Product description 1")
            .name("Product name 1")
            .price(BigDecimal.valueOf(24.99))
            .category(new Category(
                    purchaseRequest1.productId(),
                    "Product description 1",
                    "Product name 1",
                    List.of()

            ))
            .build();

    when(repository.findAllByIdInOrderById(anyList()))
            .thenReturn(List.of(product1));

    //When
    var response = assertThrows(ProductProductPurchaseException.class, () -> productService.purchaseProducts(purchaseRequests));

    //Then
    assertThat(response).isNotNull();
    assertThat(response.getMessage()).isEqualTo("One or more products does not exists '[1, 2]'");

  }

  @Test
  void testPurchaseProductsWithInsufficientStockQuantity() {
    //Given
    ProductPurchaseRequest purchaseRequest1 = new ProductPurchaseRequest(
            1, 100.0
    );
    ProductPurchaseRequest purchaseRequest2 = new ProductPurchaseRequest(
            2, 200.
    );
    List<ProductPurchaseRequest> purchaseRequests = List.of(purchaseRequest1, purchaseRequest2);

    Product product1 = Product.builder()
            .id(purchaseRequest1.productId())
            .availableQuantity(10.)
            .description("Product description 1")
            .name("Product name 1")
            .price(BigDecimal.valueOf(24.99))
            .category(new Category(
                    purchaseRequest1.productId(),
                    "Product description 1",
                    "Product name 1",
                    List.of()

            ))
            .build();

    Product product2 = Product.builder()
            .id(purchaseRequest1.productId())
            .availableQuantity(10.)
            .description("Product description 2")
            .name("Product name 2")
            .price(BigDecimal.valueOf(34.99))
            .category(new Category(
                    purchaseRequest1.productId(),
                    "Product description 2",
                    "Product name 2",
                    List.of()

            ))
            .build();

    when(repository.findAllByIdInOrderById(anyList()))
            .thenReturn(List.of(product1, product2));

    //When
    var response = assertThrows(ProductProductPurchaseException.class, () -> productService.purchaseProducts(purchaseRequests));

    //Then
    assertThat(response).isNotNull();
    assertThat(response.getMessage())
            .isEqualTo("Insufficient stock quantity for product with ID:: '1'");

  }

  @Test
  void testGetProductByIdSuccess() {
    //Given
    ProductRequest request = getProductRequest();

    //When
    var responseDto = productService.getProductById(request.id());

    //Then
    assertThat(responseDto).isNotNull();

    //Verify
    verify(repository, times(1)).findById(request.id());
  }

  @Test
  void testGetProductByIdWithNonExistingProduct() {
    //Given
    ProductRequest request = getProductRequest();

    //When
    var responseDto = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(2));

    //Then
    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getMessage()).isEqualTo("Cannot find product:: No product found with the provided Id:: 2");

    //Verify
    verify(repository, times(0)).findById(request.id());
  }

  @Test
  void testGetAllProducts() {
    //Given
    ProductRequest request1 = new ProductRequest(1,
            "Wireless Compact Keyboard 1",
            "Wireless compact keyboard",
            1000.0,
            BigDecimal.valueOf(79.99),
            1
    );

    ProductRequest request2 = new ProductRequest(1,
            "Wireless Compact Keyboard 1",
            "Wireless compact keyboard",
            1000.0,
            BigDecimal.valueOf(79.99),
            1
    );

    Category category = new Category();
    category.setId(1);
    category.setName("Wireless Compact Keyboard 1");
    category.setDescription("Wireless compact keyboard");
    Product product1 = Product.builder()
            .id(request1.id())
            .name(request1.name())
            .description(request1.description())
            .price(request1.price())
            .category(category)
            .build();

    Product product2 = Product.builder()
            .id(request2.id())
            .name(request2.name())
            .description(request2.description())
            .price(request2.price())
            .category(category)
            .build();

    ProductResponse productResponse1 = new ProductResponse(
            request1.id(),
            request1.name(),
            request1.description(),
            request1.availableQuantity(),
            request1.price(),
            category.getName(),
            category.getId()
    );

    ProductResponse productResponse2 = new ProductResponse(
            request1.id(),
            request1.name(),
            request1.description(),
            request1.availableQuantity(),
            request1.price(),
            category.getName(),
            category.getId()
    );

    when(mapper.mapProductToProductResponse(product1)).thenReturn(productResponse1);
    when(mapper.mapProductToProductResponse(product2)).thenReturn(productResponse2);
    when(repository.findAll()).thenReturn(List.of(product1, product2));

    //When
    var productResponse = productService.getAllProducts();

    //Then
    assertThat(productResponse).isNotNull().hasSize(2);
  }

  @Test
  void testUpdateProductSuccess() {
    //Given
    ProductRequest request = getProductRequest();

    //When
    var responseDto = productService.updateProduct(request);

    //Then
    assertThat(responseDto).isNotNull();

    //Verify
    verify(repository, times(1)).save(any());
  }

  @Test
  void testUpdateProductWithNonExistingProduct() {
    //Given
    ProductRequest request = getProductRequest();
    ProductRequest request2 = new ProductRequest(
            2,
            request.name(),
            request.description(),
            request.availableQuantity(),
            request.price(),
            request.categoryId()
    );

    //When
    var responseDto = assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(request2));

    //Then
    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getMessage())
            .isEqualTo("Cannot update product:: No product found with the provided Id:: 2");

    //Verify
    verify(repository, times(0)).save(any());
  }

  @Test
  void testDeleteProduct() {
    //Given
    ProductRequest request = getProductRequest();

    //When
    productService.deleteProduct(request.id());

    //Verify
    verify(repository, times(1)).deleteById(request.id());
  }

  @Test
  void testDeleteProductWithNonExistingId() {
    //Given
    ProductRequest request = getProductRequest();

    //When
    var responseDto = assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(2));

    //Then
    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getMessage())
            .isEqualTo("Cannot delete product:: No product found with the provided Id:: 2");

    //Verify
    verify(repository, times(0)).findById(request.id());
  }

  private ProductRequest getProductRequest() {
    ProductRequest request = new ProductRequest(1,
            "Wireless Compact Keyboard 1",
            "Wireless compact keyboard",
            1000.0,
            BigDecimal.valueOf(79.99),
            1
    );

    Category category = new Category();
    category.setId(1);
    category.setName("Wireless Compact Keyboard 1");
    category.setDescription("Wireless compact keyboard");
    Product product = Product.builder()
            .id(request.id())
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(category)
            .build();

    ProductResponse productResponse = new ProductResponse(
            request.id(),
            request.name(),
            request.description(),
            request.availableQuantity(),
            request.price(),
            category.getName(),
            category.getId()
    );

    when(mapper.mapProductRequestToProduct(request)).thenReturn(product);
    when(repository.findById(request.id())).thenReturn(Optional.of(product));
    when(repository.save(any())).thenReturn(product);
    when(mapper.mapProductToProductResponse(product)).thenReturn(productResponse);
    return request;
  }
}