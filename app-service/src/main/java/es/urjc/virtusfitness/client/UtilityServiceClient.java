package es.urjc.virtusfitness.client;

import es.urjc.virtusfitness.model.Booking;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Synchronous HTTP client for utility-service.
 *
 * <p>Wraps Spring 6's {@link RestClient} with a stable, narrow surface (one method per remote
 * operation) so that callers do not depend on the wire format directly.
 */
@Component
public class UtilityServiceClient {

  private static final Logger log = LoggerFactory.getLogger(UtilityServiceClient.class);
  private static final DateTimeFormatter RECEIPT_DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private final RestClient restClient;

  public UtilityServiceClient(@Value("${utility-service.url:http://localhost:8080}") String baseUrl) {
    log.info("UtilityServiceClient configured against {}", baseUrl);
    this.restClient = RestClient.builder().baseUrl(baseUrl).build();
  }

  /**
   * Render a booking-receipt PDF by calling utility-service.
   *
   * @throws UtilityServiceException if the remote service is unreachable or returns an error.
   */
  public byte[] renderBookingReceipt(Booking booking) {
    BookingReceiptRequest payload = toRequest(booking);
    try {
      byte[] body =
          restClient
              .post()
              .uri("/api/v1/pdfs/booking-receipts")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_PDF)
              .body(payload)
              .retrieve()
              .body(byte[].class);
      if (body == null || body.length == 0) {
        throw new UtilityServiceException("utility-service returned an empty PDF body");
      }
      return body;
    } catch (RestClientException e) {
      throw new UtilityServiceException(
          "Failed to call utility-service for booking " + booking.getId(), e);
    }
  }

  private static BookingReceiptRequest toRequest(Booking booking) {
    String date =
        booking.getBookingDate() != null ? booking.getBookingDate().format(RECEIPT_DATE_FORMAT) : null;
    return new BookingReceiptRequest(
        booking.getId(),
        booking.getUser().getUsername(),
        booking.getUser().getEmail(),
        booking.getFitnessClass().getName(),
        booking.getFitnessClass().getInstructor(),
        booking.getFitnessClass().getSchedule(),
        booking.getStatus(),
        date);
  }
}
