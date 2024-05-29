package com.app.ErgonomicCalculator.repository;

import com.app.ErgonomicCalculator.model.WorkspaceMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceMetricsRepository extends JpaRepository<WorkspaceMetrics, Long> {
}
