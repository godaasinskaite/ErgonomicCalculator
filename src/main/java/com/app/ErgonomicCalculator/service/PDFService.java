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
            File file = new File("src/main/resources/workspace-template.png");
            PDDocument document = new PDDocument();
            PDImageXObject image = PDImageXObject.createFromFileByContent(file, document);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.drawImage(image, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());

            addEmailPlaceholder(contentStream, workspaceMetrics.getPerson().getEmail() + " workspace!", 200, 770);

            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getTableHeightSeated()), 427, 412);
            addPlaceholder(contentStream, "Desk Height when Standing: " + workspaceMetrics.getTableHeightStanding(), 351, 670);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getTableWidth()), 495, 483);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getDisplayHeightSeated()), 545, 434);
            addPlaceholder(contentStream, "Display Height when Standing: " + workspaceMetrics.getDisplayHeightStanding(), 351, 640);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairSeatHeight()), 300, 388);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairSeatWidth()), 202, 461);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getArmRestHeight()), 381, 412);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairBackSupportHeight()), 84, 590);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairBackSupportWidth()), 188, 568);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairHeadSupportHeight()), 17, 590);

            contentStream.close();

            String uniqueFileName = "output_" + UUID.randomUUID() + ".pdf";
            String generatedPdfPath = "src/main/resources/generatedPDFs/" + uniqueFileName;
            document.save(generatedPdfPath);
            document.close();

            workspaceMetrics.setImagePath(generatedPdfPath);
            workspaceMetricsRepository.save(workspaceMetrics);

            System.out.println("PDF with background image created successfully. File name: " + uniqueFileName);
        } catch (IOException e) {
            System.err.println("Error while adding background image to PDF: " + e.getMessage());
        }
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
     * @param x             the x-coordinate position where the text will be placed on the PDF page.
     * @param y             the y-coordinate position where the text will be placed on the PDF page.
     * @throws IOException if an I/O error occurs while adding the placeholder text to the PDF page.
     */
    private void addEmailPlaceholder(final PDPageContentStream contentStream, final String label, final float x, final float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 20);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();
    }
}
