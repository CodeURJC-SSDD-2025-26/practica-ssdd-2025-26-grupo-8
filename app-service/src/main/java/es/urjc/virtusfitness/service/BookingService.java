package es.urjc.virtusfitness.service;

import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.model.FitnessClass;
import es.urjc.virtusfitness.model.User;
import es.urjc.virtusfitness.repository.BookingRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final FitnessClassService fitnessClassService;

    public BookingService(BookingRepository bookingRepository, UserService userService,
                          FitnessClassService fitnessClassService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.fitnessClassService = fitnessClassService;
    }

    public Booking createBooking(Long classId, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        FitnessClass fitnessClass = fitnessClassService.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        Optional<Booking> existing = bookingRepository.findByUserAndFitnessClass(user, fitnessClass);
        if (existing.isPresent()) {
            String status = existing.get().getStatus();
            if ("CONFIRMADA".equals(status)) {
                throw new IllegalStateException("You already have an active booking for this class");
            }
            if ("LISTA_ESPERA".equals(status)) {
                throw new IllegalStateException("You are already on the waiting list for this class");
            }
        }

        long confirmed = bookingRepository.countByFitnessClassAndStatus(fitnessClass, "CONFIRMADA");
        if (confirmed >= fitnessClass.getCapacity()) {
            // Class is full — add to waiting list
            Booking waitingBooking = new Booking(user, fitnessClass);
            waitingBooking.setBookingDate(LocalDateTime.now());
            waitingBooking.setStatus("LISTA_ESPERA");
            return bookingRepository.save(waitingBooking);
        }

        Booking booking = new Booking(user, fitnessClass);
        booking.setBookingDate(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, Principal principal) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User user = userService.findByEmail(principal.getName());
        if (!booking.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new SecurityException("You do not have permission to cancel this booking");
        }

        String previousStatus = booking.getStatus();
        booking.setStatus("CANCELADA");
        bookingRepository.save(booking);

        // If a confirmed spot was freed, promote the highest-priority user from the waiting list
        if ("CONFIRMADA".equals(previousStatus)) {
            promoteFromWaitingList(booking.getFitnessClass());
        }
    }

    /**
     * Promotes the highest-priority user from the waiting list to CONFIRMADA.
     * Priority is determined by:
     *   1. User seniority (lower user ID = registered earlier = higher priority)
     *   2. Attendance rate (confirmed bookings / total bookings — higher rate = higher priority)
     */
    private void promoteFromWaitingList(FitnessClass fitnessClass) {
        List<Booking> waitingList = bookingRepository
                .findByFitnessClassAndStatusOrderByBookingDateAsc(fitnessClass, "LISTA_ESPERA");
        if (waitingList.isEmpty()) return;

        waitingList.sort(Comparator
                // Lower user ID = older account = higher seniority
                .comparingLong((Booking b) -> b.getUser().getId())
                // Higher attendance rate = higher priority (reversed)
                .thenComparingDouble((Booking b) -> {
                    long confirmed = bookingRepository.countByUserAndStatus(b.getUser(), "CONFIRMADA");
                    long total = bookingRepository.findByUser(b.getUser()).size();
                    return total > 0 ? -(double) confirmed / total : 0.0;
                }));

        Booking promoted = waitingList.get(0);
        promoted.setStatus("CONFIRMADA");
        bookingRepository.save(promoted);
    }

    public List<Booking> getUserBookings(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public Booking findBookingById(Long id, Principal principal) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        User user = userService.findByEmail(principal.getName());
        if (!booking.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new SecurityException("You do not have permission to access this booking");
        }
        return booking;
    }

    public boolean hasActiveBooking(Long classId, Principal principal) {
        if (principal == null) return false;
        try {
            User user = userService.findByEmail(principal.getName());
            FitnessClass fc = fitnessClassService.findById(classId).orElse(null);
            if (fc == null) return false;
            Optional<Booking> b = bookingRepository.findByUserAndFitnessClass(user, fc);
            return b.isPresent() && "CONFIRMADA".equals(b.get().getStatus());
        } catch (Exception e) {
            return false;
        }
    }

    public long countConfirmedBookings(FitnessClass fitnessClass) {
        return bookingRepository.countByFitnessClassAndStatus(fitnessClass, "CONFIRMADA");
    }

    public Page<Booking> getUserBookings(Principal principal, Pageable pageable) {
        User user = userService.findByEmail(principal.getName());
        return bookingRepository.findByUserOrderByBookingDateDesc(user, pageable);
    }
}
