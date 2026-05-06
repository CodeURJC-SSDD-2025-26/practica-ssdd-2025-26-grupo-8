package es.urjc.virtusfitness.utility.pdf;

import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/pdfs")
@Tag(name = "PDFs", description = "PDF generation endpoints.")
public class PdfRestController {

  private final PdfService pdfService;

  public PdfRestController(PdfService pdfService) {
    this.pdfService = pdfService;
  }

  @PostMapping(
      path = "/booking-receipts",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_PDF_VALUE)
  @Operation(
      summary = "Render a booking receipt as PDF.",
      description =
          "Returns the generated PDF as application/pdf bytes. The request body must contain "
              + "every field that ends up on the document - the utility-service has no database.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "PDF generated successfully.",
        content = @Content(mediaType = "application/pdf",
                schema = @Schema(type = "string", format = "binary"))),
    @ApiResponse(responseCode = "400", description = "Invalid input payload."),
    @ApiResponse(responseCode = "500", description = "Failure while rendering the PDF.")
  })
  public ResponseEntity<byte[]> renderBookingReceipt(@Valid @RequestBody BookingReceiptRequest req) {
    try {
      byte[] pdf = pdfService.generateBookingReceipt(req);
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"reserva-" + req.bookingId() + ".pdf\"")
          .contentType(MediaType.APPLICATION_PDF)
          .body(pdf);
    } catch (DocumentException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PDF rendering failed", e);
    }
  }
}
