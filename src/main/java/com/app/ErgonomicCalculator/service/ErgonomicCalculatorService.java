package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.ResourceNotFoundException;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.model.WorkspaceMetrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErgonomicCalculatorService {

    private final WorkspaceMetricsService workspaceMetricsService;
    private final PDFService PDFService;
    private final AnthropometricsService anthropometricsService;
    private final PersonService personService;

    public void getNewPersonAnthropometricsAndCreateWorkspace(final AnthropometricsRequestDto anthropometricsRequestDto) throws InvalidDataException, IllegalAccessException, IOException {
        PersonAnthropometrics anthropometrics = anthropometricsService.validateAndSaveAnthropometrics(anthropometricsRequestDto);
        WorkspaceMetrics workspaceMetrics = workspaceMetricsService.createNewWorkplace(anthropometrics);
        PDFService.createWorkspacePDF(workspaceMetrics);
    }

    public File getWorkspacePDF(final String email) {
        Person person = personService.findPersonByEmail(email);
        String path = person.getWorkspaceMetrics().getImagePath();
        return new File(path);
    }

    public InputStream getWorkspacePDFStream(final String email) throws ResourceNotFoundException, IOException, IOException {
        Person person = personService.findPersonByEmail(email);
        String path = person.getWorkspaceMetrics().getImagePath();

        Resource resource = new FileSystemResource(path);
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource cannot be found.");
        }
        return resource.getInputStream();
    }
}
