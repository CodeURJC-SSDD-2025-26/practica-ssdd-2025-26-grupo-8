package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.service.FitnessClassService;
import es.urjc.virtusfitness.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@Tag(name = "Images", description = "Image retrieval endpoints.")
public class ImagesRestController {

  private final FitnessClassService fitnessClassService;
  private final UserService userService;

  public ImagesRestController(FitnessClassService fitnessClassService, UserService userService) {
    this.fitnessClassService = fitnessClassService;
    this.userService = userService;
  }

  @GetMapping("/classes/{id}")
  @Operation(summary = "Get the image of a fitness class.")
  @ApiResponse(responseCode = "200", description = "Image returned.")
  @ApiResponse(responseCode = "404", description = "Class or image not found.")
  public ResponseEntity<byte[]> classImage(@PathVariable Long id) {
    return fitnessClassService
        .findById(id)
        .filter(fc -> fc.getImage() != null)
        .map(
            fc -> {
              String ct = fc.getImageContentType() != null ? fc.getImageContentType() : "image/jpeg";
              return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(ct))
                  .body(fc.getImage());
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{id}")
  @Operation(summary = "Get the avatar of a user.")
  @ApiResponse(responseCode = "200", description = "Avatar returned.")
  @ApiResponse(responseCode = "404", description = "User or avatar not found.")
  public ResponseEntity<byte[]> userAvatar(@PathVariable Long id) {
    return userService
        .findById(id)
        .filter(u -> u.getAvatar() != null)
        .map(
            u -> {
              String ct = u.getAvatarContentType() != null ? u.getAvatarContentType() : "image/jpeg";
              return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(ct))
                  .body(u.getAvatar());
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
