package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.client.UtilityServiceClient;
import es.urjc.virtusfitness.client.UtilityServiceException;
import es.urjc.virtusfitness.dto.BookingCreateDto;
import es.urjc.virtusfitness.dto.BookingDto;
import es.urjc.virtusfitness.dto.PageResponse;
import es.urjc.virtusfitness.mapper.BookingMapper;
import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Bookings", description = "Class booking management.")
public class BookingsRestController {

  private final BookingService bookingService;
  private final UtilityServiceClient utilityServiceClient;

  public BookingsRestController(
      BookingService bookingService, UtilityServiceClient utilityServiceClient) {
    this.bookingService = bookingService;
    this.utilityServiceClient = utilityServiceClient;
  }

  @GetMapping
  @Operation(summary = "List bookings for the authenticated user (paginated).")
  public ResponseEntity<PageResponse<BookingDto>> myBookings(
      Principal principal, @PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(
        PageResponse.from(
            bookingService.getUserBookings(principal, pageable), BookingMapper::toDto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a booking by ID. Requires ownership or ROLE_ADMIN.")
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "Booking not found.")
  public ResponseEntity<BookingDto> getById(@PathVariable Long id, Principal principal) {
    try {
      Booking booking = bookingService.findBookingById(id, principal);
      return ResponseEntity.ok(BookingMapper.toDto(booking));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  @Operation(summary = "Create a booking for the authenticated user.")
  @ApiResponse(responseCode = "201", description = "Booking created.")
  @ApiResponse(responseCode = "409", description = "Already booked or on waiting list.")
  public ResponseEntity<BookingDto> create(
      @Valid @RequestBody BookingCreateDto dto, Principal principal) {
    try {
      Booking booking = bookingService.createBooking(dto.classId(), principal);
      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(booking.getId())
              .toUri();
      return ResponseEntity.created(location).body(BookingMapper.toDto(booking));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Cancel a booking. Requires ownership or ROLE_ADMIN.")
  @ApiResponse(responseCode = "204", description = "Booking cancelled.")
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "Booking not found.")
  public ResponseEntity<Void> cancel(@PathVariable Long id, Principal principal) {
    try {
      bookingService.cancelBooking(id, principal);
      return ResponseEntity.noContent().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  @Operation(summary = "Download the PDF receipt for a booking.")
  @ApiResponse(
      responseCode = "200",
      description = "PDF file.",
      content = @Content(mediaType = "application/pdf",
          schema = @Schema(type = "string", format = "binary")))
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "Booking not found.")
  public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, Principal principal) {
    try {
      Booking booking = bookingService.findBookingById(id, principal);
      byte[] pdf = utilityServiceClient.renderBookingReceipt(booking);
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reserva-" + id + ".pdf\"")
          .contentType(MediaType.APPLICATION_PDF)
          .body(pdf);
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (UtilityServiceException e) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
