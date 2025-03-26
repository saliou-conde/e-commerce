package ch.akros.order.config;

import ch.akros.order.service.JwtTokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
  private final JwtTokenService jwtTokenService;

  public FeignClientConfig(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      String token = jwtTokenService.getJwtToken();
      requestTemplate.header("Authorization", "Bearer " + token);
    };
  }
}
