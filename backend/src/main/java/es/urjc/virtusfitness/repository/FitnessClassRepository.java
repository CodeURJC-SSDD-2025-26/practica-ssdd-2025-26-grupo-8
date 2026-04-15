package es.urjc.virtusfitness.repository;

import es.urjc.virtusfitness.model.FitnessClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessClassRepository extends JpaRepository<FitnessClass, Long> {
    List<FitnessClass> findByActiveTrue();
    List<FitnessClass> findByCategory(String category);
    List<FitnessClass> findByActiveTrueOrderByIdAsc();
}
