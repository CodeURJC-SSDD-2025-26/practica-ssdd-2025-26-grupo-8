package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.service.FitnessClassService;
import es.urjc.virtusfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FitnessClassService fitnessClassService;
    private final UserService userService;

    public AdminController(FitnessClassService fitnessClassService, UserService userService) {
        this.fitnessClassService = fitnessClassService;
        this.userService = userService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalClasses", fitnessClassService.countAll());
        model.addAttribute("activeClasses", fitnessClassService.countActive());
        model.addAttribute("totalUsers", userService.countAll());
        return "admin/index";
    }

    @GetMapping("/classes")
    public String adminClasses(Model model) {
        model.addAttribute("classes", fitnessClassService.findAll());
        model.addAttribute("totalClasses", fitnessClassService.countAll());
        model.addAttribute("activeClasses", fitnessClassService.countActive());
        return "admin/classes";
    }

    @GetMapping("/classes/new")
    public String newClassForm(Model model) {
        model.addAttribute("fitnessClass", new FitnessClass());
        model.addAttribute("formMode", "new");
        return "class-form";
    }

    @GetMapping("/classes/{id}/edit")
    public String editClassForm(@PathVariable Long id, Model model) {
        FitnessClass fc = fitnessClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        model.addAttribute("fitnessClass", fc);
        model.addAttribute("formMode", "edit");
        return "class-form";
    }

    @PostMapping("/classes/save")
    public String saveClass(
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String instructor,
            @RequestParam(defaultValue = "45") int duration,
            @RequestParam(defaultValue = "20") int capacity,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String schedule,
            @RequestParam(defaultValue = "0") double price,
            @RequestParam(defaultValue = "true") boolean active,
            @RequestParam(required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            FitnessClass fc;
            if (id != null) {
                fc = fitnessClassService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
            } else {
                fc = new FitnessClass();
            }
            fc.setName(name);
            fc.setDescription(description);
            fc.setInstructor(instructor);
            fc.setDuration(duration);
            fc.setCapacity(capacity);
            fc.setDifficulty(difficulty);
            fc.setCategory(category);
            fc.setSchedule(schedule);
            fc.setPrice(price);
            fc.setActive(active);

            fitnessClassService.save(fc, image);
            redirectAttributes.addFlashAttribute("success", "Clase guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/classes";
    }

    @PostMapping("/classes/{id}/delete")
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fitnessClassService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Clase eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/classes";
    }

    @GetMapping("/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("totalUsers", userService.countAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
