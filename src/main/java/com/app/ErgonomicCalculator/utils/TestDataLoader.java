package com.app.ErgonomicCalculator.utils;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.repository.PersonRepository;
import com.app.ErgonomicCalculator.service.ErgonomicCalculatorService;
import com.app.ErgonomicCalculator.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataLoader implements CommandLineRunner {

    private final ErgonomicCalculatorService ergonomicCalculatorService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Person person = Person.builder()
                .email("goda@mail.com")
                .firstName("name")
                .lastName("lastname")
                .password(passwordEncoder.encode("godagoda"))
                .build();

        personRepository.save(person);

        AnthropometricsRequestDto anthropometrics1 = AnthropometricsRequestDto.builder()
                .height(170.5)
                .sittingHeight(85.2)
                .shoulderHeight(145.3)
                .lowerLegLength(80.7)
                .hipBreadth(30.5)
                .elbowHeight(30.2)
                .thighClearance(20.5)
                .eyeHeight(160.0)
                .shoulderBreadth(45.8)
                .kneeHeight(50.3)
                .personEmail("goda@mail.com")
                .eyeHeightStanding(160.0)
                .elbowHeightStanding(150.0)
                .build();

        AnthropometricsRequestDto anthropometrics2 = AnthropometricsRequestDto.builder()
                .height(179.5)
                .sittingHeight(92.2)
                .shoulderHeight(148.3)
                .lowerLegLength(83.7)
                .hipBreadth(43.5)
                .elbowHeight(29.2)
                .thighClearance(20.5)
                .eyeHeight(165.0)
                .shoulderBreadth(45.8)
                .kneeHeight(50.3)
                .personEmail("pepethelongsocks@mail.com")
                .build();

        ergonomicCalculatorService.getNewPersonAnthropometricsAndCreateWorkspace(anthropometrics1);

//        ergonomicCalculatorService.getNewPersonAnthropometricsAndCreateWorkspace(anthropometrics1);
//        ergonomicCalculatorService.getNewPersonAnthropometricsAndCreateWorkspace(anthropometrics2);

    }
}
