package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/** Payload to create a new fitness class. Required fields mirror the admin form. */
@Schema(description = "Payload for POST /api/v1/classes (admin only).")
public record FitnessClassCreateDto(
    @Schema(example = "Yoga Flow") @NotBlank @Size(max = 200) String name,
    @Schema(example = "Long description...") @Size(max = 2000) String description,
    @Schema(example = "Ana Martínez") String instructor,
    @Schema(example = "60") @Min(1) int duration,
    @Schema(example = "15") @Min(1) int capacity,
    @Schema(example = "Principiante") String difficulty,
    @Schema(example = "Yoga") String category,
    @Schema(example = "Mar/Jue 17:00-18:00") String schedule,
    @Schema(example = "12.0") @PositiveOrZero double price,
    @Schema(example = "true") boolean active) {}
