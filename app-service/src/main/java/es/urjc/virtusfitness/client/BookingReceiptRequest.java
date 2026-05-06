package es.urjc.virtusfitness.client;

/**
 * Wire-format payload sent to utility-service to render a booking-receipt PDF.
 *
 * <p>Intentionally redefined here (instead of sharing a module with utility-service) so the two
 * services stay decoupled - they communicate over HTTP only and each one owns its own DTOs.
 */
public record BookingReceiptRequest(
    Long bookingId,
    String userName,
    String userEmail,
    String className,
    String instructor,
    String schedule,
    String status,
    String bookingDate) {}
