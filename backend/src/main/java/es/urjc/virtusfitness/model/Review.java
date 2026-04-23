package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_class_id", nullable = false)
    private FitnessClass fitnessClass;

    @Column(nullable = false)
    private int rating; // 1-5

    @Column(length = 1000)
    private String comment;

    private LocalDateTime date;

    // Constructors
    public Review() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public FitnessClass getFitnessClass() { return fitnessClass; }
    public void setFitnessClass(FitnessClass fitnessClass) { this.fitnessClass = fitnessClass; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    @Transient
    private boolean canDelete;

    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }

    // Mustache helpers
    public String getFormattedDate() {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es")));
    }

    public String getUserInitials() {
        String name = user != null ? user.getUsername() : "US";
        if (name == null || name.isEmpty()) return "US";
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public List<StarItem> getStars() {
        List<StarItem> stars = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            stars.add(new StarItem(i <= rating));
        }
        return stars;
    }

    public static class StarItem {
        private final boolean filled;

        public StarItem(boolean filled) { this.filled = filled; }

        public boolean isFilled() { return filled; }
        public String getStarClass() { return filled ? "bi bi-star-fill" : "bi bi-star"; }
    }
}
