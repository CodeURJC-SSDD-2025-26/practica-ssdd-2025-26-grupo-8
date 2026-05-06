package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/** Partial-update payload. Wraps primitives as boxed types so {@code null} means "untouched". */
@Schema(description = "Payload for PUT /api/v1/classes/{id}. All fields optional.")
public record FitnessClassUpdateDto(
    @Schema(example = "Yoga Flow Avanzado") @Size(max = 200) String name,
    @Schema(example = "Updated description...") @Size(max = 2000) String description,
    @Schema(example = "Ana Martínez") String instructor,
    @Schema(example = "75") Integer duration,
    @Schema(example = "12") Integer capacity,
    @Schema(example = "Intermedio") String difficulty,
    @Schema(example = "Yoga") String category,
    @Schema(example = "Mar/Jue 18:00-19:15") String schedule,
    @Schema(example = "14.0") Double price,
    @Schema(example = "false") Boolean active) {}
