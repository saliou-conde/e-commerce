package ch.akros.order.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

  private static final String ORDER_TOPIC = "order-topic";
  private final Logger log = LoggerFactory.getLogger(OrderProducer.class);

  private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

  public OrderProducer(KafkaTemplate<String, OrderConfirmation> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendOrderConfirmation(final OrderConfirmation confirmation) {
    log.info("Sending confirmation: {}", confirmation);

    final Message<OrderConfirmation> message = MessageBuilder.withPayload(confirmation)
            .setHeader(KafkaHeaders.TOPIC, ORDER_TOPIC)
            .build();

    kafkaTemplate.send(message).whenComplete((sendResult, throwable) -> {

      if (throwable != null) {
        log.error("Unable to send message due to: {}", throwable.getMessage());
        log.info("Started sendOrderConfirmation failed");
        return;
      }

      log.info("#######################################################################");
      recordObjectMetadata(confirmation, sendResult, ORDER_TOPIC);
      log.info("#######################################################################");
      log.info("Started sendOrderConfirmation successful");
    });

  }


  private void recordObjectMetadata(final Object orderConfirmation, final SendResult<String, ?> sendResult, final String topic) {
    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
    log.info("OrderConfirmation Message sent to topic: [{}] ", topic);
    log.info("OrderConfirmation Message sent successfully: [{}] ", orderConfirmation);
    log.info("OrderConfirmation Message sent to the partition [{}] ", recordMetadata.partition());
    log.info("OrderConfirmation Message sent to the offset: [{}]", recordMetadata.offset());
    log.info("OrderConfirmation Started sendOrderConfirmation successful");
  }
}
