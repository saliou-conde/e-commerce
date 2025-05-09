package ch.akros.ecommerce.handler;

import ch.akros.ecommerce.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandling {

  private static final String CUSTOMER_NOT_FOUND_BY_ID = "Customer does not exist in the database";

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleCustomerNotFoundException(CustomerNotFoundException ex) {
    return generateProblemDetail( ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    var errors = new HashMap<String, Object>();
    exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return generateProblemDetail(exception.getBody().getDetail(), errors);
  }

  private ResponseEntity<ProblemDetail> generateProblemDetail( String message) {
    ProblemDetail problemDetail = generateProblemDetail(NOT_FOUND, message, CUSTOMER_NOT_FOUND_BY_ID, null);
    return new ResponseEntity<>(problemDetail, NOT_FOUND);
  }

  private ResponseEntity<ProblemDetail> generateProblemDetail(String message, Map<String, Object> errors) {
    ProblemDetail problemDetail = generateProblemDetail(HttpStatus.BAD_REQUEST, message, null, errors);
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  private ProblemDetail generateProblemDetail(HttpStatus status, String message, String title, Map<String, Object> properties) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
    problemDetail.setTitle(Optional.ofNullable(title).orElse(status.getReasonPhrase()));
    problemDetail.setProperties(Optional.ofNullable(properties).orElseGet(HashMap::new));
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }
}
