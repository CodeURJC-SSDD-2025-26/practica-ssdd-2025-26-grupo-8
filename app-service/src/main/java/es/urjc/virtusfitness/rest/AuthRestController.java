package es.urjc.virtusfitness.rest;

import es.urjc.virtusfitness.dto.LoginRequestDto;
import es.urjc.virtusfitness.dto.LoginResponseDto;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.security.JwtTokenService;
import es.urjc.virtusfitness.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication endpoints.")
public class AuthRestController {

  private final UserService userService;
  private final JwtTokenService jwtTokenService;
  private final PasswordEncoder passwordEncoder;

  public AuthRestController(
      UserService userService,
      JwtTokenService jwtTokenService,
      PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.jwtTokenService = jwtTokenService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  @Operation(
      summary = "Log in and obtain a JWT token.",
      description = "Send email and password in the request body. Returns a Bearer JWT token.")
  @ApiResponse(responseCode = "200", description = "Login successful. Returns JWT token.")
  @ApiResponse(responseCode = "401", description = "Invalid credentials.")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
    try {
      User user = userService.findByEmail(dto.email());
      if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      String token = jwtTokenService.generateToken(user.getEmail(), user.getRole());
      return ResponseEntity.ok(new LoginResponseDto("Bearer", token, user.getEmail(), user.getRole()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
