package ch.akros.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaPayLoadConverterConfig {

  @Bean
  public StringJsonMessageConverter converter() {
    return new StringJsonMessageConverter();
  }
}
