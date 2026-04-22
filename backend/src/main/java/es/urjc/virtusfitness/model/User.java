package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    private String planType = "Básico";

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;

    private String avatarContentType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Constructors
    public User() {}

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }

    public String getAvatarContentType() { return avatarContentType; }
    public void setAvatarContentType(String avatarContentType) { this.avatarContentType = avatarContentType; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(this.role);
    }

    // Mustache helpers
    public boolean isHasAvatar() { return avatar != null; }
    public boolean isRoleAdmin() { return isAdmin(); }
    public boolean isPlanBasico() { return "Básico".equals(planType); }
    public boolean isPlanPremium() { return "Premium".equals(planType); }
    public boolean isPlanElite() { return "Elite".equals(planType); }

    public String getUserInitials() {
        if (username == null || username.isEmpty()) return "US";
        return username.substring(0, Math.min(2, username.length())).toUpperCase();
    }

    public String getRoleBadgeClass() {
        return isAdmin() ? "badge mt-2 bg-danger" : "badge mt-2 bg-secondary";
    }

    public String getRoleBadgeText() {
        return isAdmin() ? "Administrator" : "User";
    }
}
