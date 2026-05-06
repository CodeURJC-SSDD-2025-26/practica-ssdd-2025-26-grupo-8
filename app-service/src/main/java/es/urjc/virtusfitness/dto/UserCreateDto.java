package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Payload to register a new user. Password requirements mirror the web form (min 8 chars). */
@Schema(description = "Payload for POST /api/v1/users (user registration).")
public record UserCreateDto(
    @Schema(example = "maria") @NotBlank @Size(min = 3, max = 50) String username,
    @Schema(example = "maria.garcia@email.com") @NotBlank @Email String email,
    @Schema(example = "Secret1234!") @NotBlank @Size(min = 8, max = 100) String password,
    @Schema(description = "Subscription plan. Defaults to 'Básico' when omitted.",
            example = "Premium")
        String planType) {}
