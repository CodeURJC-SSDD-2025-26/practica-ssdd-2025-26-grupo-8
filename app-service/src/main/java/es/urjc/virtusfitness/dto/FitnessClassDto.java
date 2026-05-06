package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Read-only projection of a fitness class.")
public record FitnessClassDto(
    @Schema(example = "1") Long id,
    @Schema(example = "CrossFit WOD") String name,
    @Schema(example = "Workout of the Day...") String description,
    @Schema(example = "Carlos Mendoza") String instructor,
    @Schema(example = "45") int duration,
    @Schema(example = "20") int capacity,
    @Schema(example = "Avanzado") String difficulty,
    @Schema(example = "CrossFit") String category,
    @Schema(example = "Lun/Mié/Vie 18:00-18:45") String schedule,
    @Schema(example = "15.0") double price,
    @Schema(example = "true") boolean active,
    @Schema(description = "True when an image has been uploaded.", example = "true")
        boolean hasImage,
    @Schema(description = "Spots still available based on confirmed bookings.", example = "5")
        int availableSpots,
    @Schema(description = "Average rating across all reviews (0.0 if none).", example = "4.5")
        double averageRating) {}
