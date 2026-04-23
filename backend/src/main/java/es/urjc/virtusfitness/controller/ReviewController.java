package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/classes/{id}/reviews")
    public String addReview(
            @PathVariable Long id,
            @RequestParam int rating,
            @RequestParam String comment,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }
        try {
            reviewService.addReview(id, rating, comment, principal);
            redirectAttributes.addFlashAttribute("success", "¡Reseña publicada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/classes/" + id;
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long classId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }
        try {
            reviewService.deleteReview(reviewId, principal);
            redirectAttributes.addFlashAttribute("success", "Reseña eliminada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/classes/" + classId;
    }
}
