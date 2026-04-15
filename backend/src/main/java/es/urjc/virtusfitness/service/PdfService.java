package es.urjc.virtusfitness.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import es.urjc.virtusfitness.model.Booking;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateBookingPdf(Booking booking) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, new Color(0, 0, 0));
        Paragraph title = new Paragraph("Virtus Fitness", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Font subtitleFont = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(80, 80, 80));
        Paragraph subtitle = new Paragraph("Confirmación de Reserva", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(24);
        document.add(subtitle);

        document.add(new Paragraph(" "));

        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        addField(document, "Nº de Reserva:", "#" + booking.getId(), labelFont, valueFont);
        addField(document, "Alumno:", booking.getUser().getUsername(), labelFont, valueFont);
        addField(document, "Email:", booking.getUser().getEmail(), labelFont, valueFont);
        addField(document, "Clase:", booking.getFitnessClass().getName(), labelFont, valueFont);
        addField(document, "Instructor:", booking.getFitnessClass().getInstructor(), labelFont, valueFont);
        addField(document, "Horario:", booking.getFitnessClass().getSchedule(), labelFont, valueFont);
        addField(document, "Estado:", booking.getStatus(), labelFont, valueFont);

        if (booking.getBookingDate() != null) {
            String date = booking.getBookingDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            addField(document, "Fecha de reserva:", date, labelFont, valueFont);
        }

        document.add(new Paragraph(" "));
        Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC, new Color(120, 120, 120));
        Paragraph footer = new Paragraph("Virtus Fitness — Todos los derechos reservados © 2026", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    private void addField(Document doc, String label, String value, Font labelFont, Font valueFont)
            throws DocumentException {
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(label + " ", labelFont));
        phrase.add(new Chunk(value != null ? value : "-", valueFont));
        Paragraph p = new Paragraph(phrase);
        p.setSpacingAfter(10);
        doc.add(p);
    }
}
