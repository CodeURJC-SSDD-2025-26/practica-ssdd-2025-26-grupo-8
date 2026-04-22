package es.urjc.virtusfitness.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fitness_classes")
public class FitnessClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private String instructor;

    private int duration; // minutes

    private int capacity;

    private String difficulty;

    private String category;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private String imageContentType;

    private boolean active = true;

    private String schedule;

    private double price;

    @OneToMany(mappedBy = "fitnessClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "fitnessClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Constructors
    public FitnessClass() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageContentType() { return imageContentType; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public int getActiveBookingsCount() {
        return (int) bookings.stream()
                .filter(b -> "CONFIRMADA".equals(b.getStatus()))
                .count();
    }

    public int getAvailableSpots() {
        return Math.max(0, capacity - getActiveBookingsCount());
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    // Mustache helpers
    public String getShortDescription() {
        if (description == null) return "";
        return description.length() > 80 ? description.substring(0, 80) + "..." : description;
    }

    public String getMediumDescription() {
        if (description == null) return "";
        return description.length() > 120 ? description.substring(0, 120) + "..." : description;
    }

    public boolean isHasImage() { return image != null; }
    public boolean isHasSchedule() { return schedule != null && !schedule.isEmpty(); }
    public boolean isHasInstructor() { return instructor != null && !instructor.isEmpty(); }
    public boolean isHasId() { return id != null; }
    public boolean isActiveOrNew() { return active || id == null; }

    public boolean isDuration30() { return duration == 30; }
    public boolean isDuration45() { return duration == 45 || duration == 0; }
    public boolean isDuration50() { return duration == 50; }
    public boolean isDuration55() { return duration == 55; }
    public boolean isDuration60() { return duration == 60; }
    public boolean isDuration90() { return duration == 90; }

    public boolean isDifficultyPrincipiante() { return "Principiante".equals(difficulty); }
    public boolean isDifficultyIntermedio() { return "Intermedio".equals(difficulty); }
    public boolean isDifficultyAvanzado() { return "Avanzado".equals(difficulty); }
    public boolean isDifficultyTodos() { return "Todos los niveles".equals(difficulty); }

    public int getCapacityOrDefault() { return capacity != 0 ? capacity : 20; }
    public double getPriceOrDefault() { return price != 0 ? price : 15.0; }
    public String getDurationText() { return duration + " min"; }

    public String getInstructorInitials() {
        String src = (instructor != null && !instructor.isEmpty()) ? instructor : "NA";
        return src.substring(0, Math.min(2, src.length())).toUpperCase();
    }
}
