package com.app.ErgonomicCalculator.repository;

import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnthropometricsRepository extends JpaRepository<PersonAnthropometrics, Long> {
}
