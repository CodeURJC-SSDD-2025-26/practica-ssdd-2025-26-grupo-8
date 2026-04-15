package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) MultipartFile avatar,
            RedirectAttributes redirectAttributes) {

        // Frontend validation is backed by server-side checks
        if (username == null || username.trim().length() < 3) {
            redirectAttributes.addFlashAttribute("error", "Username must be at least 3 characters long.");
            return "redirect:/register";
        }
        if (password == null || password.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long.");
            return "redirect:/register";
        }

        try {
            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(password);
            userService.registerUser(user, avatar);
            redirectAttributes.addFlashAttribute("success", "Account created successfully. You can now log in!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
