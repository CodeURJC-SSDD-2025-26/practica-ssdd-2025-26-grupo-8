package es.urjc.virtusfitness.controller;

import es.urjc.virtusfitness.model.Booking;
import es.urjc.virtusfitness.service.BookingService;
import es.urjc.virtusfitness.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final PdfService pdfService;

    public BookingController(BookingService bookingService, PdfService pdfService) {
        this.bookingService = bookingService;
        this.pdfService = pdfService;
    }

    @PostMapping("/bookings/new")
    public String createBooking(@RequestParam Long classId, Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            bookingService.createBooking(classId, principal);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id, principal);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/bookings/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Booking booking = bookingService.findBookingById(id, principal);
            byte[] pdf = pdfService.generateBookingPdf(booking);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"reserva-" + id + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
