package es.urjc.virtusfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials for login.")
public record LoginRequestDto(
    @NotBlank @Email @Schema(example = "maria.garcia@email.com") String email,
    @NotBlank @Schema(example = "User1234!") String password) {}
