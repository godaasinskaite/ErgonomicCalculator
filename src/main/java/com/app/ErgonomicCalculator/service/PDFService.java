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

@Service
@RequiredArgsConstructor
public class PDFService {

    private final WorkspaceMetricsRepository workspaceMetricsRepository;

    public void createWorkspacePDF(WorkspaceMetrics workspaceMetrics) {
        try {
            File file = new File("src/main/resources/brown-shipping-and-packing-square-box.jpg");
            PDDocument document = new PDDocument();
            PDImageXObject image = PDImageXObject.createFromFileByContent(file, document);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Add image to the background
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(image, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());

            addPlaceholder(contentStream, "Table Height Seated:", 50, 700);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getTableHeightSeated()), 250, 700);

            addPlaceholder(contentStream, "Table Height Standing:", 50, 650);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getTableHeightStanding()), 250, 650);

            addPlaceholder(contentStream, "Table Width:", 50, 600);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getTableWidth()), 250, 600);

            addPlaceholder(contentStream, "Display Height Seated:", 50, 550);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getDisplayHeightSeated()), 250, 550);

            addPlaceholder(contentStream, "Display Height Standing:", 50, 500);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getDisplayHeightStanding()), 250, 500);

            addPlaceholder(contentStream, "Chair Seat Height:", 50, 450);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairSeatHeight()), 250, 450);

            addPlaceholder(contentStream, "Chair Seat Width:", 50, 400);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairSeatWidth()), 250, 400);

            addPlaceholder(contentStream, "Armrest Height:", 50, 350);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getArmRestHeight()), 250, 350);

            addPlaceholder(contentStream, "Chair Back Support Height:", 50, 300);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairBackSupportHeight()), 250, 300);

            addPlaceholder(contentStream, "Chair Back Support Width:", 50, 250);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairBackSupportWidth()), 250, 250);

            addPlaceholder(contentStream, "Chair Head Support Height:", 50, 200);
            addPlaceholder(contentStream, String.valueOf(workspaceMetrics.getChairHeadSupportHeight()), 250, 200);

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

    private void addPlaceholder(PDPageContentStream contentStream, String label, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();
    }
}
