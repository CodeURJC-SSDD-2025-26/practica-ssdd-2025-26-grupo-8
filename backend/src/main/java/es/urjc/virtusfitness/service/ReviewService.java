package es.urjc.virtusfitness.service;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.Review;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final FitnessClassService fitnessClassService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService,
                         FitnessClassService fitnessClassService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.fitnessClassService = fitnessClassService;
    }

    public Review addReview(Long classId, int rating, String comment, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        FitnessClass fitnessClass = fitnessClassService.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));

        Review review = new Review();
        review.setUser(user);
        review.setFitnessClass(fitnessClass);
        review.setRating(Math.min(5, Math.max(1, rating)));
        review.setComment(comment);
        review.setDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getClassReviews(Long classId) {
        FitnessClass fitnessClass = fitnessClassService.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        return reviewRepository.findByFitnessClassOrderByDateDesc(fitnessClass);
    }

    public void deleteReview(Long id, Principal principal) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));
        User user = userService.findByEmail(principal.getName());
        if (!review.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new SecurityException("No tienes permiso para eliminar esta reseña");
        }
        reviewRepository.deleteById(id);
    }
}
