package es.urjc.virtusfitness.repository;

import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.Review;
import es.urjc.virtusfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFitnessClass(FitnessClass fitnessClass);
    List<Review> findByUser(User user);
    List<Review> findByFitnessClassOrderByDateDesc(FitnessClass fitnessClass);
}
