package ch.akros.notification.service;

import ch.akros.notification.model.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.akros.notification.service.EmailTemplate.ORDER_CONFIRMATION;
import static ch.akros.notification.service.EmailTemplate.PAYMENT_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;
  private final Map<String, Object> variables = new HashMap<>();

  @Async
  public void sendPaymentSuccessEmail(
          String fromEmail,
          String destinationEmail,
          String customerName,
          BigDecimal amount,
          String orderReference) throws MessagingException {

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
    mimeMessageHelper.setFrom(fromEmail);

    final String templateName = PAYMENT_CONFIRMATION.getTemplate();

    variables.put("totalAmount", amount);
    variables.put("customerName", customerName);
    variables.put("orderReference", orderReference);

    Context context = new Context();
    context.setVariables(variables);
    mimeMessageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());


    try {
      String htmlTemplate = templateEngine.process(templateName, context);
      mimeMessageHelper.setText(htmlTemplate, true);
      mimeMessageHelper.setTo(destinationEmail);
      mailSender.send(mimeMessage);
      log.info(String.format("Payment Email successfully sent to %s with the the template %s", destinationEmail, templateName));
    } catch (MessagingException e) {
      log.warn("Failed to send email to {}", destinationEmail, e);
    }
  }

  @Async
  public void sendOrderEmail(
          String fromEmail,
          String destinationEmail,
          String customerName,
          BigDecimal amount,
          String orderReference,
          List<Product> products
  ) throws MessagingException {

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
    mimeMessageHelper.setFrom(fromEmail);

    final String templateName = ORDER_CONFIRMATION.getTemplate();

    variables.put("totalAmount", amount);
    variables.put("customerName", customerName);
    variables.put("orderReference", orderReference);
    variables.put("products", products);

    Context context = new Context();
    context.setVariables(variables);
    mimeMessageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

    try {
      String htmlTemplate = templateEngine.process(templateName, context);
      mimeMessageHelper.setText(htmlTemplate, true);
      mimeMessageHelper.setTo(destinationEmail);
      mailSender.send(mimeMessage);
      log.info(String.format("Order Email successfully sent to %s with the the template %s", destinationEmail, templateName));
    } catch (MessagingException e) {
      log.warn("Failed to send email to {}", destinationEmail, e);
    }
  }
}
