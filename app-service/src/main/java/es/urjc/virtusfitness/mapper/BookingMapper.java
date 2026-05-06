package es.urjc.virtusfitness.mapper;

import es.urjc.virtusfitness.dto.BookingDto;
import es.urjc.virtusfitness.model.Booking;

public final class BookingMapper {

  private BookingMapper() {}

  public static BookingDto toDto(Booking b) {
    if (b == null) return null;
    return new BookingDto(
        b.getId(),
        b.getUser() != null ? b.getUser().getId() : null,
        b.getUser() != null ? b.getUser().getUsername() : null,
        b.getFitnessClass() != null ? b.getFitnessClass().getId() : null,
        b.getFitnessClass() != null ? b.getFitnessClass().getName() : null,
        b.getStatus(),
        b.getBookingDate());
  }
}
