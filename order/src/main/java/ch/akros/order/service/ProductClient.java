package ch.akros.order.service;

import ch.akros.order.controller.dto.PurchaseRequest;
import ch.akros.order.controller.dto.PurchaseResponse;
import ch.akros.order.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class ProductClient {

  @Value("${application.config.product-url}")
  private String productUrl;
  private final RestTemplate restTemplate;
  private final JwtTokenService jwtTokenService;

  public List<PurchaseResponse> purchaseProduct(List<PurchaseRequest> purchaseRequests) {
    String token = jwtTokenService.getJwtToken(); // Retrieve JWT token

    HttpHeaders headers = new HttpHeaders();
    headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    headers.setBearerAuth(token);
    HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(purchaseRequests, headers);
    ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<List<PurchaseResponse>>() {};
    ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
            productUrl+"/purchase",
            POST,
            requestEntity,
            responseType
    );

    if (responseEntity.getStatusCode().isError()) {
      throw new BusinessException("An error occurred while processing the product purchase: " + responseEntity.getStatusCode());
    }
    return responseEntity.getBody();
  }
}
