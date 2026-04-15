package es.urjc.virtusfitness.service;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.repository.FitnessClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FitnessClassService {

    private final FitnessClassRepository fitnessClassRepository;

    public FitnessClassService(FitnessClassRepository fitnessClassRepository) {
        this.fitnessClassRepository = fitnessClassRepository;
    }

    public List<FitnessClass> findAll() {
        return fitnessClassRepository.findAll();
    }

    public List<FitnessClass> findActive() {
        return fitnessClassRepository.findByActiveTrueOrderByIdAsc();
    }

    public Optional<FitnessClass> findById(Long id) {
        return fitnessClassRepository.findById(id);
    }

    public FitnessClass save(FitnessClass fitnessClass, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            fitnessClass.setImage(image.getBytes());
            fitnessClass.setImageContentType(image.getContentType());
        }
        return fitnessClassRepository.save(fitnessClass);
    }

    public FitnessClass saveWithBytes(FitnessClass fitnessClass, byte[] imageBytes, String contentType) {
        if (imageBytes != null && imageBytes.length > 0) {
            fitnessClass.setImage(imageBytes);
            fitnessClass.setImageContentType(contentType);
        }
        return fitnessClassRepository.save(fitnessClass);
    }

    public void delete(Long id) {
        fitnessClassRepository.deleteById(id);
    }

    public long countAll() {
        return fitnessClassRepository.count();
    }

    public long countActive() {
        return fitnessClassRepository.findByActiveTrue().size();
    }
}
