package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Partial-update payload for an existing user.
 *
 * <p>Every field is optional; null means "leave the existing value untouched". Password is updated
 * via a dedicated endpoint to avoid leaking it in audit logs alongside profile changes.
 */
@Schema(description = "Payload for PUT /api/v1/users/{id}. All fields optional.")
public record UserUpdateDto(
    @Schema(example = "maria") @Size(min = 3, max = 50) String username,
    @Schema(example = "maria.garcia@email.com") @Email String email,
    @Schema(example = "Premium") String planType,
    @Schema(description = "Only honored for callers with ROLE_ADMIN.", example = "ROLE_USER")
        String role) {}
