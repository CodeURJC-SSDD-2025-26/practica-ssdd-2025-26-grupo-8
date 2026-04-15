package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.service.FitnessClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final FitnessClassService fitnessClassService;

    public HomeController(FitnessClassService fitnessClassService) {
        this.fitnessClassService = fitnessClassService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<FitnessClass> allActive = fitnessClassService.findActive();
        List<FitnessClass> featured = allActive.stream().limit(3).toList();
        model.addAttribute("featuredClasses", featured);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/pricing")
    public String pricing() {
        return "pricing";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("classes", fitnessClassService.findActive());
        return "schedule";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
