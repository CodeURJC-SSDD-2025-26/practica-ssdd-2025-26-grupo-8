package es.urjc.virtusfitness.mapper;

import es.urjc.virtusfitness.dto.UserCreateDto;
import es.urjc.virtusfitness.dto.UserDto;
import es.urjc.virtusfitness.dto.UserUpdateDto;
import es.urjc.virtusfitness.model.User;

/** Pure mappers. No Spring bean needed: every method is static. */
public final class UserMapper {

  private UserMapper() {}

  public static UserDto toDto(User user) {
    if (user == null) return null;
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole(),
        user.getPlanType(),
        user.getAvatar() != null);
  }

  /**
   * Build a fresh {@link User} from a {@link UserCreateDto}. The caller is responsible for hashing
   * the password and persisting the entity.
   */
  public static User toEntity(UserCreateDto dto) {
    User u = new User();
    u.setUsername(dto.username().trim());
    u.setEmail(dto.email().trim().toLowerCase());
    u.setPassword(dto.password());
    u.setRole("ROLE_USER");
    if (dto.planType() != null && !dto.planType().isBlank()) {
      u.setPlanType(dto.planType());
    }
    return u;
  }

  /** Apply non-null fields of the update DTO onto the existing entity (PATCH-style merge). */
  public static void applyUpdate(UserUpdateDto dto, User target, boolean callerIsAdmin) {
    if (dto.username() != null && !dto.username().isBlank()) {
      target.setUsername(dto.username().trim());
    }
    if (dto.email() != null && !dto.email().isBlank()) {
      target.setEmail(dto.email().trim().toLowerCase());
    }
    if (dto.planType() != null && !dto.planType().isBlank()) {
      target.setPlanType(dto.planType());
    }
    // Only admins can change a user's role
    if (callerIsAdmin && dto.role() != null && !dto.role().isBlank()) {
      target.setRole(dto.role());
    }
  }
}
