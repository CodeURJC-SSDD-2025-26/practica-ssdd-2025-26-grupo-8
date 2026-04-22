package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.service.FitnessClassService;
import es.urjc.virtusfitness.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ImageController {

    private final FitnessClassService fitnessClassService;
    private final UserService userService;

    public ImageController(FitnessClassService fitnessClassService, UserService userService) {
        this.fitnessClassService = fitnessClassService;
        this.userService = userService;
    }

    @GetMapping("/images/class/{id}")
    public ResponseEntity<byte[]> getClassImage(@PathVariable Long id) {
        Optional<FitnessClass> optClass = fitnessClassService.findById(id);
        if (optClass.isPresent() && optClass.get().getImage() != null) {
            FitnessClass fc = optClass.get();
            String contentType = fc.getImageContentType() != null ? fc.getImageContentType() : "image/jpeg";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fc.getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/images/user/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        Optional<User> optUser = userService.findById(id);
        if (optUser.isPresent() && optUser.get().getAvatar() != null) {
            User user = optUser.get();
            String contentType = user.getAvatarContentType() != null ? user.getAvatarContentType() : "image/jpeg";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(user.getAvatar());
        }
        return ResponseEntity.notFound().build();
    }
}
