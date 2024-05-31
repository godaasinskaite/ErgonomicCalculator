package com.app.ErgonomicCalculator.validator;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnthropometricsRequestValidatorTest {

    @InjectMocks
    private AnthropometricsRequestValidator validator;

    @Test
    void validate_WhenValidData() {
        var data = createAnthropometricRequestDto();
        assertDoesNotThrow(() -> validator.validate(data));
    }

    @Test
    void validate_WhenInvalidMetrics() {
        var data = createAnthropometricRequestDto();
        data.setShoulderHeight(0.0);
        assertThrows(InvalidDataException.class, () -> validator.validate(data));
    }

    @Test
    void validate_WhenEmptyEmail() {
        var data = createAnthropometricRequestDto();
        data.setPersonEmail("");
        assertThrows(InvalidDataException.class, () -> validator.validate(data));
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
}