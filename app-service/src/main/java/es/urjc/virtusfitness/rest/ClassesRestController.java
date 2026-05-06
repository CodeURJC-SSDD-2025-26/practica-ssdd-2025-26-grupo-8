package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.dto.FitnessClassCreateDto;
import es.urjc.virtusfitness.dto.FitnessClassDto;
import es.urjc.virtusfitness.dto.FitnessClassUpdateDto;
import es.urjc.virtusfitness.dto.PageResponse;
import es.urjc.virtusfitness.mapper.FitnessClassMapper;
import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.service.FitnessClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/classes")
@Tag(name = "Classes", description = "Fitness class management.")
public class ClassesRestController {

  private final FitnessClassService fitnessClassService;

  public ClassesRestController(FitnessClassService fitnessClassService) {
    this.fitnessClassService = fitnessClassService;
  }

  @GetMapping
  @Operation(summary = "List active classes. Filter by category with ?category=Yoga.")
  public ResponseEntity<PageResponse<FitnessClassDto>> list(
      @RequestParam(required = false) String category,
      @PageableDefault(size = 10) Pageable pageable) {
    Page<FitnessClass> page =
        category != null && !category.isBlank()
            ? fitnessClassService.findActiveByCategory(category, pageable)
            : fitnessClassService.findActive(pageable);
    return ResponseEntity.ok(PageResponse.from(page, FitnessClassMapper::toDto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a fitness class by ID.")
  @ApiResponse(responseCode = "404", description = "Class not found.")
  public ResponseEntity<FitnessClassDto> getById(@PathVariable Long id) {
    return fitnessClassService
        .findById(id)
        .map(fc -> ResponseEntity.ok(FitnessClassMapper.toDto(fc)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create a new class. Requires ROLE_ADMIN.")
  @ApiResponse(responseCode = "201", description = "Class created.")
  public ResponseEntity<FitnessClassDto> create(@Valid @RequestBody FitnessClassCreateDto dto) {
    FitnessClass fc = FitnessClassMapper.toEntity(dto);
    try {
      FitnessClass saved = fitnessClassService.save(fc, null);
      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(saved.getId())
              .toUri();
      return ResponseEntity.created(location).body(FitnessClassMapper.toDto(saved));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a class (partial update). Requires ROLE_ADMIN.")
  @ApiResponse(responseCode = "404", description = "Class not found.")
  public ResponseEntity<FitnessClassDto> update(
      @PathVariable Long id, @Valid @RequestBody FitnessClassUpdateDto dto) {
    FitnessClass fc = fitnessClassService.findById(id).orElse(null);
    if (fc == null) return ResponseEntity.notFound().build();
    FitnessClassMapper.applyUpdate(dto, fc);
    try {
      FitnessClass saved = fitnessClassService.save(fc, null);
      return ResponseEntity.ok(FitnessClassMapper.toDto(saved));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a class. Requires ROLE_ADMIN.")
  @ApiResponse(responseCode = "204", description = "Class deleted.")
  @ApiResponse(responseCode = "404", description = "Class not found.")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (fitnessClassService.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    fitnessClassService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
