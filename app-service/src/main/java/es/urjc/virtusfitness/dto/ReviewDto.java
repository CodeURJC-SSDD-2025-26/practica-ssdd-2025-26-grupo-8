package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Read-only projection of a class review.")
public record ReviewDto(
    @Schema(example = "10") Long id,
    @Schema(example = "2") Long userId,
    @Schema(example = "maria") String userName,
    @Schema(example = "1") Long fitnessClassId,
    @Schema(example = "5", description = "Rating from 1 to 5.") int rating,
    @Schema(example = "Increíble clase. Carlos es un instructor fantástico.") String comment,
    @Schema(example = "2026-04-15T19:00:00") LocalDateTime date) {}
