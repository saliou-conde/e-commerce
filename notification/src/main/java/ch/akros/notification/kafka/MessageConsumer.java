package ch.akros.notification.kafka;

import ch.akros.notification.model.Notification;
import ch.akros.notification.model.order.OrderConfirmation;
import ch.akros.notification.model.payment.PaymentConfirmation;
import ch.akros.notification.repository.NotificationRepository;
import ch.akros.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static ch.akros.notification.constant.AppConstant.*;
import static ch.akros.notification.model.NotificationType.ORDER_CONFIRMATION;
import static ch.akros.notification.model.NotificationType.PAYMENT_CONFIRMATION;


@Service
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;
    @Value("${email.from}")
    private String emailFrom;

    @KafkaListener(topics = ORDER_TOPICS, groupId = ORDER_GROUP_ID)
    public void consumerOrderConfirmation(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("################################OrderConfirmation Consumer###############################################");
        log.info("consumer consumes the customer from the topic: {}", ORDER_TOPICS);
        Notification notification = new Notification(null, ORDER_CONFIRMATION, LocalDateTime.now(), orderConfirmation, null);
        repository.save(notification);
        var customer = orderConfirmation.customer();
        var customerName = customer.firstName() + " " + customer.lastName();
        emailService.sendOrderEmail(
                emailFrom,
                customer.email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
        log.info("Customer Message is: {}", orderConfirmation);
        log.info("################################OrderConfirmation Consumer###############################################");
    }


    @KafkaListener(topics = PAYMENT_TOPICS, groupId = PAYMENT_GROUP_ID)
    public void consumerPaymentConfirmation(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("################################PaymentConfirmation Customer Consumer#######################################");
        Notification notification = new Notification(null, PAYMENT_CONFIRMATION, LocalDateTime.now(), null, paymentConfirmation);
        repository.save(notification);
        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
                emailFrom,
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.totalAmount(),
                paymentConfirmation.orderReference()
        );
        log.info("consumer consumes the confirmation from the topic: {}", PAYMENT_TOPICS);
        log.info("PaymentConfirmation Message is: {}", paymentConfirmation);
        log.info("################################PaymentConfirmation Customer Consumer#######################################");
    }

}
