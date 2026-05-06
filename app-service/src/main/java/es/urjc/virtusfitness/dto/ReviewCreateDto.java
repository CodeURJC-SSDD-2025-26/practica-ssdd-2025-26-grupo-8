package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a review on a class. The user is the authenticated caller.")
public record ReviewCreateDto(
    @Schema(example = "5", description = "1 to 5.") @Min(1) @Max(5) int rating,
    @Schema(example = "¡Genial!") @Size(max = 1000) String comment) {}
