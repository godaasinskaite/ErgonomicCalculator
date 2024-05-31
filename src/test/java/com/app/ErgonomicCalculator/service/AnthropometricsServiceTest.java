package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDtoAfterAuth;
import com.app.ErgonomicCalculator.mapper.AnthropometricsMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.repository.AnthropometricsRepository;
import com.app.ErgonomicCalculator.validator.AnthropometricsRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnthropometricsServiceTest {

    @Mock
    private AnthropometricsRequestValidator anthropometricsRequestValidator;
    @Mock
    private AnthropometricsRepository anthropometricsRepository;
    @Mock
    private AnthropometricsMapper anthropometricsMapper;
    @Mock
    private PersonService personService;

    @InjectMocks
    AnthropometricsService anthropometricsService;


    @Test
    void updateAnthropometrics() {
        var requestDto = createAnthropometricRequestDto();
        var person = createPerson();
        var anthropometrics = createPersonAnthropometrics(person);

        assertNotEquals(requestDto.getHeight(), anthropometrics.getHeight());

        var result = anthropometricsService.updateAnthropometrics(requestDto, anthropometrics, person);

        assertEquals(result.getHeight(), anthropometrics.getHeight());
        assertEquals(result.getHeight(), requestDto.getHeight());
    }

    @Test
    void mapRequests() {

        AnthropometricsRequestDtoAfterAuth dtoAfterAuth = createAnthropometricsRequestDtoAfterAuth();
        String email = "goda@mail.com";
        AnthropometricsRequestDto expectedDto = createAnthropometricRequestDto();

        when(anthropometricsMapper.toAnthropometricsRequestDto(dtoAfterAuth)).thenReturn(expectedDto);

        AnthropometricsRequestDto result = anthropometricsService.mapRequests(dtoAfterAuth, email);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        assertEquals(email, result.getPersonEmail());
    }

    private AnthropometricsRequestDto createAnthropometricRequestDto() {
        return AnthropometricsRequestDto.builder()
                .height(170.5)
                .sittingHeight(95.2)
                .shoulderHeight(75.3)
                .lowerLegLength(60.7)
                .hipBreadth(59.5)
                .elbowHeight(30.2)
                .thighClearance(20.5)
                .eyeHeight(85.0)
                .shoulderBreadth(45.8)
                .kneeHeight(50.3)
                .personEmail("goda@mail.com")
                .eyeHeightStanding(160.0)
                .elbowHeightStanding(130.0)
                .build();
    }

    private PersonAnthropometrics createPersonAnthropometrics(Person person) {
        return PersonAnthropometrics.builder()
                .height(17.5)
                .sittingHeight(5.2)
                .shoulderHeight(5.3)
                .lowerLegLength(6.7)
                .hipBreadth(5.5)
                .elbowHeight(3.2)
                .thighClearance(2.5)
                .eyeHeight(8.0)
                .shoulderBreadth(4.8)
                .kneeHeight(5.3)
                .eyeHeightStanding(16.0)
                .elbowHeightStanding(13.0)
                .person(person)
                .build();
    }

    private AnthropometricsRequestDtoAfterAuth createAnthropometricsRequestDtoAfterAuth() {
        return AnthropometricsRequestDtoAfterAuth.builder()
                .height(170.5)
                .sittingHeight(95.2)
                .shoulderHeight(75.3)
                .lowerLegLength(60.7)
                .hipBreadth(59.5)
                .elbowHeight(30.2)
                .thighClearance(20.5)
                .eyeHeight(85.0)
                .shoulderBreadth(45.8)
                .kneeHeight(50.3)
                .eyeHeightStanding(160.0)
                .elbowHeightStanding(130.0)
                .build();
    }

    private Person createPerson() {
        return Person.builder()
                .email("goda@mail.com")
                .build();
    }
}