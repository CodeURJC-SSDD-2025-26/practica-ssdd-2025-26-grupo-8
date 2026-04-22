package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.service.BookingService;
import es.urjc.virtusfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;
    private final BookingService bookingService;

    public ProfileController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Booking> bookings = bookingService.getUserBookings(principal);
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookings);
        model.addAttribute("bookingsCount", bookings.size());
        long completedCount = bookings.stream().filter(b -> "CONFIRMADA".equals(b.getStatus())).count();
        model.addAttribute("completedCount", completedCount);
        return "profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) String planType,
            @RequestParam(required = false) MultipartFile avatar,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            User updatedUser = new User();
            updatedUser.setUsername(username);
            updatedUser.setEmail(email);
            updatedUser.setPlanType(planType);
            userService.updateProfile(updatedUser, avatar, principal);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
