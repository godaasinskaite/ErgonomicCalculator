package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.model.WorkspaceMetrics;
import com.app.ErgonomicCalculator.repository.WorkspaceMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Service for workspace related operations: creating, updating.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceMetricsService {

    private final WorkspaceMetricsRepository workspaceMetricsRepository;


    /**
     * Method creates workspace metrics from given person anthropometric data.
     * Updates existing workspace, saves if new.
     *
     * @param personAnthropometrics anthropometric data of person.
     * @return updated or created workspace metrics entity.
     */
    public WorkspaceMetrics createNewWorkplace(final PersonAnthropometrics personAnthropometrics) throws IOException {
        final WorkspaceMetrics newWorkspaceMetrics = createNewWorkspaceMetrics(personAnthropometrics);
        final WorkspaceMetrics metricsToUpdate = personAnthropometrics.getPerson().getWorkspaceMetrics();

        if (metricsToUpdate != null) {
            updateWorkplaceIfExists(metricsToUpdate, newWorkspaceMetrics);
            workspaceMetricsRepository.save(metricsToUpdate);
            log.info("Existing Workspace data updated and saved");
            return metricsToUpdate;
        }

        workspaceMetricsRepository.save(newWorkspaceMetrics);
        log.info("New Workspace data created and saved.");
        return newWorkspaceMetrics;
    }

    /**
     * Workspace creation from person anthropometric data.
     *
     * @param personAnthropometrics anthropometric measures of person.
     * @return calculated WorkspaceMetrics object.
     */
    private WorkspaceMetrics createNewWorkspaceMetrics(final PersonAnthropometrics personAnthropometrics) {
        return WorkspaceMetrics.builder()
                .tableHeightSeated(personAnthropometrics.getElbowHeight() + personAnthropometrics.getLowerLegLength())
                .tableHeightStanding(personAnthropometrics.getElbowHeightStanding())
                .tableWidth(personAnthropometrics.getThighClearance())
                .displayHeightSeated(personAnthropometrics.getEyeHeight() + personAnthropometrics.getLowerLegLength())
                .displayHeightStanding(personAnthropometrics.getEyeHeightStanding())
                .chairSeatHeight(personAnthropometrics.getLowerLegLength())
                .chairSeatWidth(personAnthropometrics.getHipBreadth())
                .armRestHeight(personAnthropometrics.getElbowHeight() + personAnthropometrics.getLowerLegLength())
                .chairBackSupportHeight(personAnthropometrics.getLowerLegLength() + personAnthropometrics.getShoulderHeight())
                .chairBackSupportWidth(personAnthropometrics.getShoulderBreadth())
                .chairHeadSupportHeight(personAnthropometrics.getLowerLegLength() + personAnthropometrics.getSittingHeight())
                .person(personAnthropometrics.getPerson())
                .build();
    }

    /**
     * Method to update existing workspace metrics with new data.
     *
     * @param metricsToUpdate earlier saved workspace metrics data.
     * @param newMetrics      newly create workspace metrics.
     * @throws IOException if earlier created PDF file is missing.
     */
    private void updateWorkplaceIfExists(final WorkspaceMetrics metricsToUpdate, final WorkspaceMetrics newMetrics) throws IOException {
        log.info("Updating existing WorkspaceMetrics");
        Long id = metricsToUpdate.getId();
        Files.deleteIfExists(Path.of(metricsToUpdate.getImagePath()));
        BeanUtils.copyProperties(newMetrics, metricsToUpdate);
        metricsToUpdate.setId(id);
    }
}
