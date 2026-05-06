package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token returned after successful login.")
public record LoginResponseDto(
    @Schema(example = "Bearer") String type,
    @Schema(example = "eyJhbGci...") String token,
    @Schema(example = "maria.garcia@email.com") String email,
    @Schema(example = "ROLE_USER") String role) {}
