package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.dto.PageResponse;
import es.urjc.virtusfitness.dto.UserCreateDto;
import es.urjc.virtusfitness.dto.UserDto;
import es.urjc.virtusfitness.dto.UserUpdateDto;
import es.urjc.virtusfitness.mapper.UserMapper;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User registration and management.")
public class UsersRestController {

  private final UserService userService;

  public UsersRestController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "List all users (paginated). Requires ROLE_ADMIN.")
  public ResponseEntity<PageResponse<UserDto>> list(@PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(PageResponse.from(userService.findAll(pageable), UserMapper::toDto));
  }

  @GetMapping("/me")
  @Operation(summary = "Get the currently authenticated user.")
  public ResponseEntity<UserDto> me(Authentication auth) {
    User user = userService.findByEmail(auth.getName());
    return ResponseEntity.ok(UserMapper.toDto(user));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a user by ID. Requires ROLE_ADMIN or own account.")
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "User not found.")
  public ResponseEntity<UserDto> getById(@PathVariable Long id, Authentication auth) {
    boolean isAdmin =
        auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    User caller = userService.findByEmail(auth.getName());
    if (!isAdmin && !caller.getId().equals(id)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return userService
        .findById(id)
        .map(u -> ResponseEntity.ok(UserMapper.toDto(u)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Register a new user (public).")
  @ApiResponse(responseCode = "201", description = "User created.")
  @ApiResponse(responseCode = "409", description = "Email already in use.")
  public ResponseEntity<UserDto> register(@Valid @RequestBody UserCreateDto dto) {
    try {
      User newUser = UserMapper.toEntity(dto);
      User saved = userService.registerUser(newUser, null);
      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(saved.getId())
              .toUri();
      return ResponseEntity.created(location).body(UserMapper.toDto(saved));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a user. Requires ROLE_ADMIN or own account.")
  @ApiResponse(responseCode = "403", description = "Access denied.")
  @ApiResponse(responseCode = "404", description = "User not found.")
  public ResponseEntity<UserDto> update(
      @PathVariable Long id, @Valid @RequestBody UserUpdateDto dto, Authentication auth) {
    boolean isAdmin =
        auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    User caller = userService.findByEmail(auth.getName());
    if (!isAdmin && !caller.getId().equals(id)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    User target = userService.findById(id).orElse(null);
    if (target == null) return ResponseEntity.notFound().build();
    UserMapper.applyUpdate(dto, target, isAdmin);
    try {
      User saved = userService.save(target);
      return ResponseEntity.ok(UserMapper.toDto(saved));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a user. Requires ROLE_ADMIN.")
  @ApiResponse(responseCode = "204", description = "User deleted.")
  @ApiResponse(responseCode = "404", description = "User not found.")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (userService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
