package ch.akros.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

  private static final String PAYMENT_TOPIC = "payment-topic";

  private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

  public void sendNotification(final PaymentNotificationRequest notificationRequest) {
    log.info("Sending notification with body: <{}>", notificationRequest);

    final Message<PaymentNotificationRequest> message = MessageBuilder.withPayload(notificationRequest)
            .setHeader(TOPIC, PAYMENT_TOPIC)
            .build();

    kafkaTemplate.send(message).whenComplete((sendResult, throwable) -> {

      if (throwable != null) {
        log.error("Unable to send message due to: {}", throwable.getMessage());
        log.info("Started sendMessageToTopic failed");
        return;
      }

      log.info("#######################################################################");
      recordObjectMetadata(notificationRequest, sendResult);
      log.info("#######################################################################");
      log.info("Started sendMessageToTopic successful");
    });

  }


  private void recordObjectMetadata(final Object paymentConfirmation, final SendResult<String, ?> sendResult) {
    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
    log.info("PaymentNotificationRequest Message sent to topic: [{}] ", PAYMENT_TOPIC);
    log.info("PaymentNotificationRequest Message sent successfully: [{}] ", paymentConfirmation);
    log.info("PaymentNotificationRequest Message sent to the partition [{}] ", recordMetadata.partition());
    log.info("PaymentNotificationRequest Message sent to the offset: [{}]", recordMetadata.offset());
    log.info("PaymentNotificationRequest Started sendMessageToTopic successful");
  }
}
