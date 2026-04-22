package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.Review;
import es.urjc.virtusfitness.service.BookingService;
import es.urjc.virtusfitness.service.FitnessClassService;
import es.urjc.virtusfitness.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ClassController {

    private final FitnessClassService fitnessClassService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    public ClassController(FitnessClassService fitnessClassService,
                           BookingService bookingService,
                           ReviewService reviewService) {
        this.fitnessClassService = fitnessClassService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    @GetMapping("/classes")
    public String classes(Model model) {
        model.addAttribute("classes", fitnessClassService.findActive());
        return "classes";
    }

    @GetMapping("/classes/{id}")
    public String classDetail(@PathVariable Long id, Model model, Principal principal) {
        FitnessClass fitnessClass = fitnessClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada: " + id));

        List<Review> reviews = reviewService.getClassReviews(id);
        boolean hasBooking = bookingService.hasActiveBooking(id, principal);

        double avgRating = reviews.isEmpty() ? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        model.addAttribute("fitnessClass", fitnessClass);
        model.addAttribute("reviews", reviews);
        model.addAttribute("hasBooking", hasBooking);
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("reviewCount", reviews.size());
        model.addAttribute("availableSpots", fitnessClass.getAvailableSpots());
        return "class-detail";
    }

    @PostMapping("/classes/{id}/book")
    public String bookClass(@PathVariable Long id, Principal principal,
                            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            bookingService.createBooking(id, principal);
            redirectAttributes.addFlashAttribute("success", "¡Reserva realizada con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/classes/" + id;
    }
}
