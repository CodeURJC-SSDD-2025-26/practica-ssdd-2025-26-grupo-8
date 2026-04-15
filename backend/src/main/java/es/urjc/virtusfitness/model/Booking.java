package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
}
