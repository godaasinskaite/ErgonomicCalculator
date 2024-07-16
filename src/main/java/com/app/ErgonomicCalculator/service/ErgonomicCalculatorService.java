package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDtoAfterAuth;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.ResourceNotFoundException;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.model.WorkspaceMetrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Service for managing operations related new PersonAnthropometric processing, new PersonWorkspace creation, retrieving PDFs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ErgonomicCalculatorService {

    private final WorkspaceMetricsService workspaceMetricsService;
    private final PDFService PDFService;
    private final AnthropometricsService anthropometricsService;
    private final PersonService personService;

    /**
     * Validates, saves or updates new person anthropometric data, creates a workspace based on the saved data,
     * and generates a PDF of the workspace metrics.
     *
     * @param anthropometricsRequestDto the Data Transfer Object containing person email and the anthropometric data to be validated and saved.
     * @throws InvalidDataException   if the provided anthropometrics data is invalid and does not pass validation.
     * @throws IllegalAccessException if there is an access violation during the creation of the workspace.
     * @throws IOException            if an I/O error occurs during the PDF creation process.
     */
    public void getNewPersonAnthropometricsAndCreateWorkspace(final AnthropometricsRequestDto anthropometricsRequestDto) throws InvalidDataException, IllegalAccessException, IOException {
        final PersonAnthropometrics anthropometrics = anthropometricsService.validateAndSaveAnthropometrics(anthropometricsRequestDto);
        final WorkspaceMetrics workspaceMetrics = workspaceMetricsService.createNewWorkplace(anthropometrics);
        PDFService.createWorkspacePDF(workspaceMetrics);
    }

    /**
     * Validates, saves or updates new person anthropometric data, creates a workspace based on the saved data,
     * and generates a PDF of the workspace metrics. This method is called when a person authenticates themselves and provides new anthropometrics data.
     *
     * @param dtoAfterAuth   the Data Transfer Object containing anthropometric data to be validated and saved.
     * @param authentication param to retrieve user email.
     * @throws InvalidDataException   if the provided anthropometrics data is invalid.
     * @throws IllegalAccessException if there is an access violation during the creation of the workspace.
     * @throws IOException            if an I/O error occurs during the PDF creation process.
     */
    public void updateOrCreateAnthropometricsAndWorkspace(final AnthropometricsRequestDtoAfterAuth dtoAfterAuth, final Authentication authentication) throws InvalidDataException, IllegalAccessException, IOException {
        final PersonDto personDto = (PersonDto) authentication.getPrincipal();
        AnthropometricsRequestDto dto = anthropometricsService.mapRequests(dtoAfterAuth, personDto.getEmail());
        final PersonAnthropometrics anthropometrics = anthropometricsService.validateAndSaveAnthropometrics(dto);
        final WorkspaceMetrics workspaceMetrics = workspaceMetricsService.createNewWorkplace(anthropometrics);
        PDFService.createWorkspacePDF(workspaceMetrics);
    }

    /**
     * Retrieves the PDF file for the workspace metrics associated with a person identified by their email.
     *
     * @param email the email address of the person whose workspace PDF is to be retrieved.
     * @return the File object representing the PDF file of the workspace metrics.
     */
    public File getWorkspacePDF(final String email) {
        final Person person = personService.findPersonByEmail(email);
        final String path = person.getWorkspaceMetrics().getImagePath();
        return new File(path);
    }

    /**
     * Retrieves the PDF of the workspace after Person is authenticated.
     *
     * @param authentication object containing user credentials to retrieve user email.
     * @return the File object representing the PDF file of the workspace metrics.
     */
    public File getWorkspacePDFAfterAuth(Authentication authentication) {
        final PersonDto personDto = (PersonDto) authentication.getPrincipal();
        return getWorkspacePDF(personDto.getEmail());
    }

    /**
     * Retrieves an InputStream for the PDF file of the workspace metrics associated with a person identified by their email.
     *
     * @param email the email address of the person whose workspace PDF is to be retrieved.
     * @return an InputStream for the PDF file of the workspace metrics.
     * @throws ResourceNotFoundException if the PDF file for the workspace metrics cannot be found.
     * @throws IOException               if an I/O error occurs while accessing the PDF file.
     */
    public InputStream getWorkspacePDFStream(final String email) throws ResourceNotFoundException, IOException {
        final Person person = personService.findPersonByEmail(email);
        final String path = person.getWorkspaceMetrics().getImagePath();

        final Resource resource = new FileSystemResource(path);
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource cannot be found.");
        }
        return resource.getInputStream();
    }

    /**
     * Retrieves an InputStream of the PDF file after person is authenticated.
     *
     * @param authentication object containing user credentials to retrieve user email.
     * @return an InputStream for the PDF file of the workspace metrics.
     * @throws IOException               if the PDF file for the workspace metrics cannot be found.
     * @throws ResourceNotFoundException if an I/O error occurs while accessing the PDF file.
     */
    public InputStream getWorkspacePDFStreamAfterAuth(final Authentication authentication) throws IOException, ResourceNotFoundException {
        final PersonDto personDto = (PersonDto) authentication.getPrincipal();
        return getWorkspacePDFStream(personDto.getEmail());
    }
}
