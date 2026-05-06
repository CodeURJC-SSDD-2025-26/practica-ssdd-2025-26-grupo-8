package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

/**
 * JSON-friendly wrapper around Spring Data {@link Page}.
 *
 * <p>Spring's default {@code Page} serialization is verbose and bound to the {@code PageImpl}
 * shape; this record exposes only the fields a REST client needs and keeps them ordered.
 */
@Schema(description = "Paginated response wrapper.")
public record PageResponse<T>(
    @Schema(description = "Items on the current page.") List<T> content,
    @Schema(description = "Zero-based index of the current page.", example = "0") int page,
    @Schema(description = "Maximum number of items per page.", example = "10") int size,
    @Schema(description = "Total items across every page.", example = "42") long totalElements,
    @Schema(description = "Total number of pages.", example = "5") int totalPages,
    @Schema(description = "True when this is the last page.", example = "false") boolean last) {

  public static <E, D> PageResponse<D> from(Page<E> page, Function<E, D> mapper) {
    return new PageResponse<>(
        page.getContent().stream().map(mapper).toList(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast());
  }
}
