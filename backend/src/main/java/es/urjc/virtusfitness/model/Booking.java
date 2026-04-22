package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_class_id", nullable = false)
    private FitnessClass fitnessClass;

    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private String status = "CONFIRMADA"; // CONFIRMADA, CANCELADA, PENDIENTE

    // Constructors
    public Booking() {}

    public Booking(User user, FitnessClass fitnessClass) {
        this.user = user;
        this.fitnessClass = fitnessClass;
        this.bookingDate = LocalDateTime.now();
        this.status = "CONFIRMADA";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public FitnessClass getFitnessClass() { return fitnessClass; }
    public void setFitnessClass(FitnessClass fitnessClass) { this.fitnessClass = fitnessClass; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Mustache helpers
    public boolean isConfirmedStatus() { return "CONFIRMADA".equals(status); }
    public boolean isCancelledStatus() { return "CANCELADA".equals(status); }
    public boolean isWaitingListStatus() { return "LISTA_ESPERA".equals(status); }
    public boolean isCancellable() { return "CONFIRMADA".equals(status) || "LISTA_ESPERA".equals(status); }

    public String getProfileStatusClass() {
        return switch (status) {
            case "CANCELADA" -> "status-badge status-confirmed status-cancelled";
            case "PENDIENTE", "LISTA_ESPERA" -> "status-badge status-confirmed status-pending";
            default -> "status-badge status-confirmed";
        };
    }

    public String getProfileStatusText() {
        return switch (status) {
            case "CONFIRMADA" -> "Confirmed";
            case "CANCELADA" -> "Cancelled";
            case "LISTA_ESPERA" -> "Waiting List";
            default -> status;
        };
    }

    public String getAdminStatusClass() {
        return switch (status) {
            case "CONFIRMADA" -> "role-badge role-user";
            case "LISTA_ESPERA" -> "role-badge role-admin";
            default -> "role-badge";
        };
    }

    public String getAdminStatusStyle() {
        return "CANCELADA".equals(status) ? "background:#444;color:#aaa;" : "";
    }

    public String getAdminStatusText() {
        return switch (status) {
            case "CONFIRMADA" -> "Confirmed";
            case "CANCELADA" -> "Cancelled";
            case "LISTA_ESPERA" -> "Waiting List";
            default -> status;
        };
    }

    public String getFormattedDate() {
        if (bookingDate == null) return "";
        return bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
