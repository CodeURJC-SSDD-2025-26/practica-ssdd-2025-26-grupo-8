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
        addClassFormAttributes(model, new FitnessClass(), false);
        return "class-form";
    }

    @GetMapping("/classes/{id}/edit")
    public String editClassForm(@PathVariable Long id, Model model) {
        FitnessClass fc = fitnessClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        addClassFormAttributes(model, fc, true);
        return "class-form";
    }

    private void addClassFormAttributes(Model model, FitnessClass fc, boolean editMode) {
        model.addAttribute("fitnessClass", fc);
        model.addAttribute("isEditMode", editMode);
        model.addAttribute("isNewMode", !editMode);
        model.addAttribute("fcId", fc.getId());
        model.addAttribute("fcName", fc.getName() != null ? fc.getName() : "");
        model.addAttribute("fcDescription", fc.getDescription() != null ? fc.getDescription() : "");
        model.addAttribute("fcInstructor", fc.getInstructor() != null ? fc.getInstructor() : "");
        model.addAttribute("fcSchedule", fc.getSchedule() != null ? fc.getSchedule() : "");
        model.addAttribute("fcCategory", fc.getCategory() != null ? fc.getCategory() : "");
        model.addAttribute("fcCapacityOrDefault", fc.getCapacityOrDefault());
        model.addAttribute("fcPriceOrDefault", fc.getPriceOrDefault());
    }

    @PostMapping("/classes/save")
    public String saveClass(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String instructor,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String capacity,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String schedule,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre de la clase es obligatorio");
            }

            FitnessClass fc;
            if (id != null) {
                fc = fitnessClassService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
            } else {
                fc = new FitnessClass();
            }

            fc.setName(name.trim());
            fc.setDescription(description);
            fc.setInstructor(instructor);
            fc.setDuration(parseSafeInt(duration, 45));
            fc.setCapacity(parseSafeInt(capacity, 20));
            fc.setDifficulty(difficulty);
            fc.setCategory(category);
            fc.setSchedule(schedule);
            fc.setPrice(parseSafeDouble(price, 0.0));
            fc.setActive("on".equalsIgnoreCase(active) || "true".equalsIgnoreCase(active));

            fitnessClassService.save(fc, image);
            redirectAttributes.addFlashAttribute("success", "Clase guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/classes";
    }

    private int parseSafeInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseSafeDouble(String value, double defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
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
