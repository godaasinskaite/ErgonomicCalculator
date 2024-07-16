package com.app.ErgonomicCalculator.controller;

import com.app.ErgonomicCalculator.dto.*;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.ResourceNotFoundException;
import com.app.ErgonomicCalculator.service.ErgonomicCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;


/**
 * Controller for handling requests related to retrieving and processing anthropometric data, workspace metrics and PDFs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ergonomic")
@CrossOrigin(origins = "http://localhost:4200")
public class ErgonomicCalculatorController {

    private final ErgonomicCalculatorService service;

    /**
     * Method handles the processing of new anthropometric data provided in the request body.
     * It invokes the service to process that data and create workspace metrics.
     *
     * @param anthropometrics the Data Transfer Object containing the anthropometric information and person email.
     * @return ResponseEntity containing status and message indicating the result of the operation.
     * @throws InvalidDataException   if the provided in request body data is invalid.
     * @throws IllegalAccessException if there is an illegal access error during processing.
     * @throws IOException            if an input/output error occurs during processing.
     */
    @PostMapping("/new")
    public ResponseEntity<String> addNewAnthropometrics(@RequestBody final AnthropometricsRequestDto anthropometrics) throws InvalidDataException, IllegalAccessException, IOException {
        try {
            service.getNewPersonAnthropometricsAndCreateWorkspace(anthropometrics);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Person Anthropometrics processed and Workspace Metrics calculated.\"}");
        } catch (InvalidDataException | IllegalAccessException | IOException ex) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Person Anthropometrics can not be processed.\"}");
        }
    }

    /**
     * Method handles the processing of new anthropometric data when person authenticates themselves.
     * It invokes the service to process that data and create workspace metrics.
     *
     * @param anthropometrics the Data Transfer Object containing the anthropometric information.
     * @param authentication  the authentication information from the current person, used to recognize the user.
     * @return Response entity containing status and message indicating the result of operation.
     * @throws InvalidDataException   if the provided in request body data is invalid.
     * @throws IOException            if there is an illegal access error during processing.
     * @throws IllegalAccessException if an input/output error occurs during processing.
     */
    @PostMapping("/anthropometrics")
    public ResponseEntity<String> addAnthropometricsAfterAuth(@RequestBody final AnthropometricsRequestDtoAfterAuth anthropometrics, final Authentication authentication) throws InvalidDataException, IOException, IllegalAccessException {
        try {
            service.updateOrCreateAnthropometricsAndWorkspace(anthropometrics, authentication);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Person Anthropometrics processed and Workspace Metrics calculated.\"}");
        } catch (InvalidDataException | IOException | IllegalAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Person Anthropometrics can not be processed.\"}");
        }
    }

    /**
     * Downloads the workspace PDF for the specified email.
     * Retrieves the PDF file associated with the given email address and returns it as a downloadable
     * response. The response headers are set to indicate that the content is a PDF file and that it should be treated
     * as an attachment for download.
     *
     * @param email the email address associated with the workspace whose PDF file is to be downloaded.
     * @return ResponseEntity containing the PDF file as an attachment.
     * @throws FileNotFoundException if the PDF file corresponding to the specified email is not found.
     */
    @GetMapping("/download/{email}")
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable final String email) throws FileNotFoundException {
        var pdfFile = service.getWorkspacePDF(email);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new InputStreamResource(new FileInputStream(pdfFile)));
    }

    /**
     * Downloads the workspace PDF for the authenticated person.
     * Retrieves the PDF file associated with the retrieved email from the authentication and returns it as a downloadable response.
     * The response headers are set to indicate that the content is a PDF file and that it should be treated as an attachment for download.
     *
     * @param authentication the authentication information from the current person, used to recognize the user.
     * @return ResponseEntity containing the PDF file as an attachment.
     * @throws FileNotFoundException if the PDF file corresponding to the specified email is not found.
     */
    @GetMapping("/auth/download")
    public ResponseEntity<InputStreamResource> downloadPdfAfterAuth(final Authentication authentication) throws FileNotFoundException {
        var pdfFile = service.getWorkspacePDFAfterAuth(authentication);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new InputStreamResource(new FileInputStream(pdfFile)));
    }

    /**
     * Streams the workspace PDF for the specified email for viewing in the browser.
     * Retrieves a stream of the PDF file associated with the given email address and returns it in the
     * response with the appropriate headers set for viewing the PDF directly in the browser.
     *
     * @param email the email address associated with the workspace whose PDF file is to be streamed.
     * @return ResponseEntity containing the PDF file stream.
     * @throws IOException               if an input/output error occurs while retrieving the PDF file stream.
     * @throws ResourceNotFoundException if the PDF file corresponding to the specified email is not found.
     */
    @GetMapping("/openPdf/{email}")
    public ResponseEntity<InputStreamResource> viewPDF(@PathVariable final String email) throws IOException, ResourceNotFoundException {
        var data = service.getWorkspacePDFStream(email);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(data));
    }

    /**
     * Streams the workspace PDF for the authenticated person for viewing in the browser.
     * Retrieves a stream of the PDF file associated with the retrieved email from the authentication and returns it in the
     * response with the appropriate headers set for viewing the PDF directly in the browser.
     *
     * @param authentication the authentication information from the current person, used to recognize the user.
     * @return ResponseEntity containing the PDF file stream.
     * @throws IOException               if an input/output error occurs while retrieving the PDF file stream.
     * @throws ResourceNotFoundException if the PDF file corresponding to the specified email is not found.
     */
    @GetMapping("/auth/pdf")
    public ResponseEntity<InputStreamResource> openPdfAfterAuth(final Authentication authentication) throws IOException, ResourceNotFoundException {
        var data = service.getWorkspacePDFStreamAfterAuth(authentication);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(data));
    }
}
