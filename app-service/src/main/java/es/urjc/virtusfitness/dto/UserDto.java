package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Read-only projection of {@code User} returned by the REST API. Never includes the password. */
@Schema(description = "Public projection of a user. Password is never exposed.")
public record UserDto(
    @Schema(example = "1") Long id,
    @Schema(example = "maria") String username,
    @Schema(example = "maria.garcia@email.com") String email,
    @Schema(example = "ROLE_USER") String role,
    @Schema(example = "Premium") String planType,
    @Schema(description = "True when the user has uploaded an avatar image.", example = "true")
        boolean hasAvatar) {}
