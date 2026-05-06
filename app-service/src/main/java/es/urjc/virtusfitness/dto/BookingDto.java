package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Read-only projection of a booking. Embeds enough user/class context to "
    + "render listings without follow-up requests.")
public record BookingDto(
    @Schema(example = "42") Long id,
    @Schema(example = "2") Long userId,
    @Schema(example = "maria") String userName,
    @Schema(example = "1") Long fitnessClassId,
    @Schema(example = "CrossFit WOD") String fitnessClassName,
    @Schema(example = "CONFIRMADA",
            description = "One of CONFIRMADA, LISTA_ESPERA, CANCELADA, PENDIENTE.")
        String status,
    @Schema(example = "2026-04-29T18:30:00") LocalDateTime bookingDate) {}
