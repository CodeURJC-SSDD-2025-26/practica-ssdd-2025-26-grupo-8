package es.urjc.virtusfitness.utility.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

/**
 * Generates Virtus Fitness PDF receipts using OpenPDF.
 *
 * <p>Pure rendering logic: no database, no network, no template engine. Receives a {@link
 * BookingReceiptRequest} that already contains every field required by the document.
 */
@Service
public class PdfService {

  private static final Color HEADER_BLACK = new Color(0, 0, 0);
  private static final Color SUBTITLE_GRAY = new Color(80, 80, 80);
  private static final Color FOOTER_GRAY = new Color(120, 120, 120);

  public byte[] generateBookingReceipt(BookingReceiptRequest req) throws DocumentException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, out);
    document.open();

    Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, HEADER_BLACK);
    Paragraph title = new Paragraph("Virtus Fitness", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    Font subtitleFont = new Font(Font.HELVETICA, 14, Font.NORMAL, SUBTITLE_GRAY);
    Paragraph subtitle = new Paragraph("Confirmación de Reserva", subtitleFont);
    subtitle.setAlignment(Element.ALIGN_CENTER);
    subtitle.setSpacingAfter(24);
    document.add(subtitle);

    document.add(new Paragraph(" "));

    Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
    Font valueFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

    addField(document, "Nº de Reserva:", "#" + req.bookingId(), labelFont, valueFont);
    addField(document, "Alumno:", req.userName(), labelFont, valueFont);
    addField(document, "Email:", req.userEmail(), labelFont, valueFont);
    addField(document, "Clase:", req.className(), labelFont, valueFont);
    addField(document, "Instructor:", req.instructor(), labelFont, valueFont);
    addField(document, "Horario:", req.schedule(), labelFont, valueFont);
    addField(document, "Estado:", req.status(), labelFont, valueFont);
    addField(document, "Fecha de reserva:", req.bookingDate(), labelFont, valueFont);

    document.add(new Paragraph(" "));
    Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC, FOOTER_GRAY);
    Paragraph footer =
        new Paragraph("Virtus Fitness — Todos los derechos reservados © 2026", footerFont);
    footer.setAlignment(Element.ALIGN_CENTER);
    document.add(footer);

    document.close();
    return out.toByteArray();
  }

  private static void addField(
      Document doc, String label, String value, Font labelFont, Font valueFont)
      throws DocumentException {
    Phrase phrase = new Phrase();
    phrase.add(new Chunk(label + " ", labelFont));
    phrase.add(new Chunk(value != null && !value.isBlank() ? value : "-", valueFont));
    Paragraph p = new Paragraph(phrase);
    p.setSpacingAfter(10);
    doc.add(p);
  }
}
