package es.urjc.virtusfitness.mapper;

import es.urjc.virtusfitness.dto.ReviewDto;
import es.urjc.virtusfitness.model.Review;

public final class ReviewMapper {

  private ReviewMapper() {}

  public static ReviewDto toDto(Review r) {
    if (r == null) return null;
    return new ReviewDto(
        r.getId(),
        r.getUser() != null ? r.getUser().getId() : null,
        r.getUser() != null ? r.getUser().getUsername() : null,
        r.getFitnessClass() != null ? r.getFitnessClass().getId() : null,
        r.getRating(),
        r.getComment(),
        r.getDate());
  }
}
