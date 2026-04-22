package es.urjc.virtusfitness.repository;

import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByFitnessClass(FitnessClass fitnessClass);
    Optional<Booking> findByUserAndFitnessClass(User user, FitnessClass fitnessClass);
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    long countByFitnessClassAndStatus(FitnessClass fitnessClass, String status);
    long countByUserAndStatus(User user, String status);
    List<Booking> findByFitnessClassAndStatusOrderByBookingDateAsc(FitnessClass fitnessClass, String status);
}
