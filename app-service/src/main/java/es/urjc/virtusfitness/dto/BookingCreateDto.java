package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Payload for POST /api/v1/bookings.
 *
 * <p>The user is taken from the authenticated principal — clients cannot book on someone else's
 * behalf, so {@code userId} is intentionally absent.
 */
@Schema(description = "Payload to create a booking for the authenticated user.")
public record BookingCreateDto(
    @Schema(description = "ID of the fitness class to book.", example = "1")
        @NotNull
        @Positive
        Long classId) {}
