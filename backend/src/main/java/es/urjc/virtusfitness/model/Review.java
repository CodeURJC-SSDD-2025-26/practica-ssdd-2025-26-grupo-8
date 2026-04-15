package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
}
