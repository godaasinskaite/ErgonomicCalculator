package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.model.WorkspaceMetrics;
import com.app.ErgonomicCalculator.repository.WorkspaceMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceMetricsService {

    private final WorkspaceMetricsRepository workspaceMetricsRepository;

    public WorkspaceMetrics createNewWorkplace(PersonAnthropometrics personAnthropometrics) throws IOException {
        if (personAnthropometrics.getPerson().getWorkspaceMetrics() != null) {
            updateWorkplaceIfExists(personAnthropometrics.getPerson().getWorkspaceMetrics());
        }

        WorkspaceMetrics workspaceMetrics =  WorkspaceMetrics.builder()
                .tableHeightSeated(personAnthropometrics.getElbowHeight() + personAnthropometrics.getLowerLegLength())
                .tableHeightStanding(personAnthropometrics.getElbowHeightStanding())
                .tableWidth(personAnthropometrics.getThighClearance())
                .displayHeightSeated(personAnthropometrics.getEyeHeight())
                .displayHeightStanding(personAnthropometrics.getEyeHeightStanding())
                .chairSeatHeight(personAnthropometrics.getLowerLegLength())
                .chairSeatWidth(personAnthropometrics.getHipBreadth())
                .armRestHeight(personAnthropometrics.getElbowHeight() + personAnthropometrics.getSittingHeight())
                .chairBackSupportHeight(personAnthropometrics.getLowerLegLength() + personAnthropometrics.getShoulderHeight())
                .chairBackSupportWidth(personAnthropometrics.getShoulderBreadth())
                .chairHeadSupportHeight(personAnthropometrics.getLowerLegLength() + personAnthropometrics.getSittingHeight())
                .person(personAnthropometrics.getPerson())
                .build();

        workspaceMetricsRepository.save(workspaceMetrics);
        log.info("Workspace data created and saved.");
        return workspaceMetrics;
    }

    @Transactional
    private void updateWorkplaceIfExists(WorkspaceMetrics workspaceMetrics) throws IOException {
        log.info("Updating existing WorkspaceMetrics");
        Files.deleteIfExists(Path.of(workspaceMetrics.getImagePath()));
        workspaceMetricsRepository.deleteById(workspaceMetrics.getId());

        log.info("WorkspaceMetrics deleted where id = " + workspaceMetrics.getId());
    }
}
