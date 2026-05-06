package es.urjc.virtusfitness.utility;

import static org.assertj.core.api.Assertions.assertThat;

import es.urjc.virtusfitness.utility.pdf.BookingReceiptRequest;
import es.urjc.virtusfitness.utility.pdf.PdfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilityServiceApplicationTests {

  @Autowired private PdfService pdfService;

  @Test
  void contextLoads() {
    assertThat(pdfService).isNotNull();
  }

  @Test
  void rendersBookingReceiptToBytes() throws Exception {
    BookingReceiptRequest req =
        new BookingReceiptRequest(
            42L,
            "maria",
            "maria.garcia@email.com",
            "CrossFit WOD",
            "Carlos Mendoza",
            "Lun/Mié/Vie 18:00-18:45",
            "CONFIRMADA",
            "29/04/2026 18:30");

    byte[] pdf = pdfService.generateBookingReceipt(req);

    assertThat(pdf).isNotEmpty();
    // Every PDF starts with the magic bytes "%PDF"
    assertThat(new String(pdf, 0, 4)).isEqualTo("%PDF");
  }
}
