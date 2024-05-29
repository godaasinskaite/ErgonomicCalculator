package com.app.ErgonomicCalculator.controller;

import com.app.ErgonomicCalculator.config.PersonAuthProvider;
import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.exception.ResourceNotFoundException;
import com.app.ErgonomicCalculator.exception.ServiceException;
import com.app.ErgonomicCalculator.model.Person;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ergonomic")
@CrossOrigin(origins = "http://localhost:4200")
public class ErgonomicCalculatorController {

    private final ErgonomicCalculatorService service;

    @PostMapping("/new")
    public ResponseEntity<?> addNewAnthropometrics(@RequestBody final AnthropometricsRequestDto anthropometrics) throws InvalidDataException, IllegalAccessException, IOException {
        service.getNewPersonAnthropometricsAndCreateWorkspace(anthropometrics);
//        return ResponseEntity.status(HttpStatus.OK).body("Person Anthropometrics processed and Workspace Metrics calculated.");
        var response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Person Anthropometrics processed and Workspace Metrics calculated.");
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/download/{email}")
    public ResponseEntity<?> getPdf(@PathVariable final String email) throws FileNotFoundException {
        var pdfFile = service.getWorkspacePDF(email);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new InputStreamResource(new FileInputStream(pdfFile)));
    }

    @GetMapping("/openPdf/{email}")
    public ResponseEntity<?> viewPDF(@PathVariable final String email) throws IOException, ResourceNotFoundException {
        var data = service.getWorkspacePDFStream(email);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(data));
    }

    @GetMapping("/auth/pdf")
    public ResponseEntity<?> openPdfAfterAuth(final Authentication authentication) throws IOException, ResourceNotFoundException {
        var personDto = (PersonDto) authentication.getPrincipal();
        var data = service.getWorkspacePDFStream(personDto.getEmail());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(data));
    }

    @GetMapping("/auth/download")
    public ResponseEntity<?> downloadPdfAfterAuth(final Authentication authentication) throws FileNotFoundException {
        var personDto = (PersonDto) authentication.getPrincipal();
        var pdfFile = service.getWorkspacePDF(personDto.getEmail());

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new InputStreamResource(new FileInputStream(pdfFile)));
    }
}
