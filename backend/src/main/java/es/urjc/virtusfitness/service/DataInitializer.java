package es.urjc.virtusfitness.service;

import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.Review;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.repository.BookingRepository;
import es.urjc.virtusfitness.repository.FitnessClassRepository;
import es.urjc.virtusfitness.repository.ReviewRepository;
import es.urjc.virtusfitness.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final FitnessClassRepository fitnessClassRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           FitnessClassRepository fitnessClassRepository,
                           BookingRepository bookingRepository,
                           ReviewRepository reviewRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fitnessClassRepository = fitnessClassRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized, skipping data loading.");
            return;
        }

        log.info("Initializing database with sample data...");

        // ---- Users ----
        User admin = createUser("admin", "admin@virtusfitness.com", "Admin1234!", "ROLE_ADMIN", "Elite");
        User maria = createUser("maria", "maria.garcia@email.com", "User1234!", "ROLE_USER", "Premium");
        User carlos = createUser("carlos", "carlos.ruiz@email.com", "User1234!", "ROLE_USER", "Básico");

        // ---- Fitness Classes ----
        FitnessClass crossfit = createClass(
                "CrossFit WOD",
                "Workout of the Day con movimientos funcionales de alta intensidad: levantamientos olímpicos, gimnasia y cardio en el Box.",
                "Carlos Mendoza", 45, 20, "Avanzado", "CrossFit",
                "Lun/Mié/Vie 18:00-18:45", 15.0, "static/images/crossfit.jpg");

        FitnessClass yoga = createClass(
                "Yoga Flow",
                "Secuencias fluidas de asanas que conectan movimiento y respiración. Mejora tu flexibilidad, equilibrio y bienestar mental.",
                "Ana Martínez", 60, 15, "Principiante", "Yoga",
                "Mar/Jue 17:00-18:00", 12.0, "static/images/yoga.jpg");

        FitnessClass cycling = createClass(
                "Indoor Cycling",
                "Ciclismo de alta energía con música motivadora. Quema hasta 700 kcal y mejora tu resistencia cardiovascular.",
                "Luis García", 50, 18, "Intermedio", "Cycling",
                "Lun/Mié/Vie 19:00-19:50", 13.0, "static/images/cycling.jpg");

        FitnessClass boxing = createClass(
                "Boxing Pro",
                "Técnica profesional de boxeo con trabajo de sacos y manoplas. Potencia, coordinación y descarga de estrés garantizados.",
                "Miguel Torres", 60, 16, "Todos los niveles", "Boxeo",
                "Mar/Jue/Sáb 18:30-19:30", 14.0, "static/images/boxing-class.jpg");

        FitnessClass pilates = createClass(
                "Pilates Reformer",
                "Entrenamiento en máquina reformer que fortalece el core, mejora la alineación postural y previene lesiones.",
                "Laura Sánchez", 55, 12, "Intermedio", "Pilates",
                "Lun/Mié 16:00-16:55", 16.0, "static/images/pilates.jpg");

        FitnessClass zumba = createClass(
                "Zumba Dance",
                "Fitness con ritmos latinos. Coreografías divertidas y energéticas para quemar calorías sin que parezca un entrenamiento.",
                "María Fernández", 50, 25, "Principiante", "Zumba",
                "Vie/Sáb 19:00-19:50", 10.0, "static/images/boxing-class.jpg");

        // ---- Bookings ----
        createBooking(maria, crossfit, "CONFIRMADA");
        createBooking(carlos, yoga, "CONFIRMADA");
        createBooking(maria, boxing, "CONFIRMADA");
        createBooking(carlos, cycling, "PENDIENTE");

        // ---- Reviews ----
        createReview(maria, crossfit, 5, "¡Increíble clase! Carlos es un instructor fantástico. La evolución ha sido notable desde el primer día.");
        createReview(carlos, yoga, 5, "El yoga aquí es transformador. Ana explica todo con mucha paciencia.");
        createReview(maria, boxing, 4, "Muy buena clase, la técnica que se enseña es de nivel profesional.");
        createReview(carlos, crossfit, 5, "Las clases de CrossFit son desafiantes pero adictivas. 100% recomendado.");

        log.info("Database initialized successfully with {} users and {} classes.",
                userRepository.count(), fitnessClassRepository.count());
    }

    private User createUser(String username, String email, String rawPassword, String role, String plan) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setPlanType(plan);
        return userRepository.save(user);
    }

    private FitnessClass createClass(String name, String description, String instructor,
                                      int duration, int capacity, String difficulty,
                                      String category, String schedule, double price,
                                      String imagePath) {
        FitnessClass fc = new FitnessClass();
        fc.setName(name);
        fc.setDescription(description);
        fc.setInstructor(instructor);
        fc.setDuration(duration);
        fc.setCapacity(capacity);
        fc.setDifficulty(difficulty);
        fc.setCategory(category);
        fc.setSchedule(schedule);
        fc.setPrice(price);
        fc.setActive(true);

        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                fc.setImage(resource.getInputStream().readAllBytes());
                String ext = imagePath.endsWith(".png") ? "image/png" : "image/jpeg";
                fc.setImageContentType(ext);
            }
        } catch (IOException e) {
            log.warn("Could not load image for class '{}': {}", name, imagePath);
        }

        return fitnessClassRepository.save(fc);
    }

    private void createBooking(User user, FitnessClass fc, String status) {
        Booking booking = new Booking(user, fc);
        booking.setBookingDate(LocalDateTime.now().minusDays((long)(Math.random() * 30)));
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    private void createReview(User user, FitnessClass fc, int rating, String comment) {
        Review review = new Review();
        review.setUser(user);
        review.setFitnessClass(fc);
        review.setRating(rating);
        review.setComment(comment);
        review.setDate(LocalDateTime.now().minusDays((long)(Math.random() * 60)));
        reviewRepository.save(review);
    }
}
