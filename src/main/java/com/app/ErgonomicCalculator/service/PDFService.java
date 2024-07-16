package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.model.WorkspaceMetrics;
import com.app.ErgonomicCalculator.repository.WorkspaceMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * Service for creating and saving workspace PDF file.
 */
@Service
@RequiredArgsConstructor
public class PDFService {

    private final WorkspaceMetricsRepository workspaceMetricsRepository;

    /**
     * Method creates a PDF document for the provided workspace metrics.
     * Updates workspace entity with generated value of unique name, saves to resources and repository.
     *
     * @param workspaceMetrics object containing the metrics for the workspace.
     */
    public void createWorkspacePDF(final WorkspaceMetrics workspaceMetrics) {
        try {
            final File file = new File("src/main/resources/workspace-template.png");
            final PDDocument document = new PDDocument();
            final PDImageXObject image = PDImageXObject.createFromFileByContent(file, document);
            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(image, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
            addPlaceholders(contentStream, workspaceMetrics);
            contentStream.close();

            final String generatedPdfPath = generatePdfPath();
            document.save(generatedPdfPath);
            document.close();

            workspaceMetrics.setImagePath(generatedPdfPath);
            workspaceMetricsRepository.save(workspaceMetrics);
        } catch (IOException e) {
            System.err.println("Error while adding background image to PDF: " + e.getMessage());
        }
    }

    /**
     * Adds multiple placeholder texts to the content stream based on the provided WorkspaceMetrics.
     *
     * @param contentStream the PDPageContentStream where the placeholders will be added.
     * @param metrics       the WorkspaceMetrics containing the data to be displayed in the placeholders.
     * @throws IOException if an I/O error occurs while adding placeholder texts to the content stream.
     */
    private void addPlaceholders(final PDPageContentStream contentStream, final WorkspaceMetrics metrics) throws IOException {
        addEmailPlaceholder(contentStream, metrics.getPerson().getEmail() + " workspace!");
        addPlaceholder(contentStream, String.valueOf(metrics.getTableHeightSeated()), 427, 412);
        addPlaceholder(contentStream, "Desk Height when Standing: " + metrics.getTableHeightStanding(), 351, 670);
        addPlaceholder(contentStream, String.valueOf(metrics.getTableWidth()), 495, 483);
        addPlaceholder(contentStream, String.valueOf(metrics.getDisplayHeightSeated()), 545, 434);
        addPlaceholder(contentStream, "Display Height when Standing: " + metrics.getDisplayHeightStanding(), 351, 640);
        addPlaceholder(contentStream, String.valueOf(metrics.getChairSeatHeight()), 300, 388);
        addPlaceholder(contentStream, String.valueOf(metrics.getChairSeatWidth()), 202, 461);
        addPlaceholder(contentStream, String.valueOf(metrics.getArmRestHeight()), 381, 412);
        addPlaceholder(contentStream, String.valueOf(metrics.getChairBackSupportHeight()), 84, 590);
        addPlaceholder(contentStream, String.valueOf(metrics.getChairBackSupportWidth()), 188, 568);
        addPlaceholder(contentStream, String.valueOf(metrics.getChairHeadSupportHeight()), 17, 590);
    }

    /**
     * Method adds a placeholder text label on a PDF page.
     *
     * @param contentStream the PDPageContentStream object for adding content to the PDF page.
     * @param label         the label text to be displayed as a placeholder for the text.
     * @param x             the x-coordinate position where the text will be placed on the PDF page.
     * @param y             the y-coordinate position where the text will be placed on the PDF page.
     * @throws IOException if an I/O error occurs while adding the placeholder text to the PDF page.
     */
    private void addPlaceholder(final PDPageContentStream contentStream, final String label, final float x, final float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 13);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();
    }

    /**
     * Method adds a placeholder text for an email label on a PDF page.
     *
     * @param contentStream the PDPageContentStream object for adding content to the PDF page.
     * @param label         the label text to be displayed as a placeholder for the text.
     * @throws IOException if an I/O error occurs while adding the placeholder text to the PDF page.
     */
    private void addEmailPlaceholder(final PDPageContentStream contentStream, final String label) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 20);
        contentStream.newLineAtOffset((float) 200, (float) 770);
        contentStream.showText(label);
        contentStream.endText();
    }

    /**
     * Saves the document to a unique file path and returns the generated PDF file path.
     *
     * @return the file path of the generated PDF.
     * @throws IOException if an I/O error occurs while saving the document.
     */
    private String generatePdfPath() throws IOException {
        final String uniqueFileName = "output_" + UUID.randomUUID() + ".pdf";
        return "src/main/resources/generatedPDFs/" + uniqueFileName;
    }
}
