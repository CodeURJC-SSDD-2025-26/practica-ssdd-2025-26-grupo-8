package es.urjc.virtusfitness.utility.pdf;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Self-contained payload for generating a booking-receipt PDF.
 *
 * <p>The utility-service has no database access, so app-service must send every field that ends up
 * on the document. Optional fields are nullable; required ones use bean validation.
 */
@Schema(description = "Data needed to render a booking-receipt PDF.")
public record BookingReceiptRequest(
    @Schema(description = "Identifier of the booking, used in the receipt header.", example = "42")
        @NotNull
        @Positive
        Long bookingId,
    @Schema(description = "Display name of the customer.", example = "maria")
        @NotBlank
        String userName,
    @Schema(description = "Email address of the customer.", example = "maria.garcia@email.com")
        @NotBlank
        String userEmail,
    @Schema(description = "Name of the booked fitness class.", example = "CrossFit WOD")
        @NotBlank
        String className,
    @Schema(description = "Instructor for the class.", example = "Carlos Mendoza") String instructor,
    @Schema(description = "Human-readable schedule for the class.", example = "Lun/Mié/Vie 18:00")
        String schedule,
    @Schema(description = "Booking status (CONFIRMADA, LISTA_ESPERA, CANCELADA).",
            example = "CONFIRMADA")
        @NotBlank
        String status,
    @Schema(description = "Booking creation timestamp formatted dd/MM/yyyy HH:mm.",
            example = "29/04/2026 18:30")
        String bookingDate) {}
