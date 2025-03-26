package ch.akros.order.service;

import ch.akros.order.exception.JwtTokenServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class JwtTokenService {

  @Value("${auth.server.url}")
  private String authServerUrl;

  @Value("${auth.client.id}")
  private String clientId;

  @Value("${auth.client.secret}") // Optional (only needed for confidential clients)
  private String clientSecret;

  @Value("${auth.username}")
  private String username;

  @Value("${auth.password}")
  private String password;

  @Value("${auth.grant.type}")
  private String grantType;

  private String cachedToken;
  private long tokenExpirationTime;

  public String getJwtToken() {
    // Check if the cached token is still valid
    if (cachedToken != null && System.currentTimeMillis() < tokenExpirationTime) {
      return cachedToken;
    }

    // Create headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<MultiValueMap<String, String>> request = getMultiValueMapHttpEntity(headers);

    // Send request using RestTemplate
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.exchange(authServerUrl, HttpMethod.POST, request, Map.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      cachedToken = (String) response.getBody().get("access_token");
      int expiresIn = (Integer) response.getBody().get("expires_in"); // Token expiration time in seconds
      tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000L) - 5000; // 5-second buffer
      return cachedToken;
    }

    throw new JwtTokenServiceException("Failed to obtain JWT token: " + response.getStatusCode());
  }

  private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(HttpHeaders headers) {
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", grantType);
    requestBody.add("client_id", clientId);
    requestBody.add("username", username);
    requestBody.add("password", password);

    // Add client secret if it's a confidential client
    if (clientSecret != null && !clientSecret.isEmpty()) {
      requestBody.add("client_secret", clientSecret);
    }

    // Create request entity
    return new HttpEntity<>(requestBody, headers);
  }
}
