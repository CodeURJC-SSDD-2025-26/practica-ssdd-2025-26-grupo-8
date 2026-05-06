package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.dto.PageResponse;
import es.urjc.virtusfitness.dto.ReviewCreateDto;
import es.urjc.virtusfitness.dto.ReviewDto;
import es.urjc.virtusfitness.mapper.ReviewMapper;
import es.urjc.virtusfitness.model.Review;
import es.urjc.virtusfitness.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "Reviews", description = "Class review management.")
public class ReviewsRestController {

  private final ReviewService reviewService;

  public ReviewsRestController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @GetMapping("/api/v1/classes/{classId}/reviews")
  @Operation(summary = "List reviews for a class (paginated). Public access.")
  @ApiResponse(responseCode = "404", description = "Class not found.")
  public ResponseEntity<PageResponse<ReviewDto>> list(
      @PathVariable Long classId, @PageableDefault(size = 10) Pageable pageable) {
    try {
      return ResponseEntity.ok(
          PageResponse.from(reviewService.getClassReviews(classId, pageable), ReviewMapper::toDto));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/api/v1/classes/{classId}/reviews")
  @Operation(summary = "Add a review to a class. Requires authentication.")
  @ApiResponse(responseCode = "201", description = "Review created.")
  @ApiResponse(responseCode = "404", description = "Class not found.")
  public ResponseEntity<ReviewDto> create(
      @PathVariable Long classId,
      @Valid @RequestBody ReviewCreateDto dto,
      Principal principal) {
    try {
      Review review = reviewService.addReview(classId, dto.rating(), dto.comment(), principal);
      URI location =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/v1/reviews/{id}")
              .buildAndExpand(review.getId())
              .toUri();
      return ResponseEntity.created(location).body(ReviewMapper.toDto(review));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/api/v1/reviews/{id}")
  @Operation(summary = "Delete a review. Requires ownership or ROLE_ADMIN.")
  @ApiResponse(responseCode = "204", description = "Review deleted.")
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "Review not found.")
  public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
    try {
      reviewService.deleteReview(id, principal);
      return ResponseEntity.noContent().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
