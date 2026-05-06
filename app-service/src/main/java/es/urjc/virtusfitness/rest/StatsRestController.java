package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.repository.BookingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@Tag(name = "Stats", description = "Aggregated statistics for charts.")
public class StatsRestController {

  private final BookingRepository bookingRepository;

  public StatsRestController(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  @GetMapping("/bookings-per-class")
  @Operation(
      summary = "Bookings per fitness class.",
      description = "Returns labels (class names) and data (booking counts) for a bar chart.")
  public ResponseEntity<ChartDto> bookingsPerClass() {
    List<Object[]> rows = bookingRepository.countBookingsPerClass();
    List<String> labels = rows.stream().map(r -> (String) r[0]).toList();
    List<Long> data = rows.stream().map(r -> (Long) r[1]).toList();
    return ResponseEntity.ok(new ChartDto(labels, data));
  }

  public record ChartDto(List<String> labels, List<Long> data) {}
}
