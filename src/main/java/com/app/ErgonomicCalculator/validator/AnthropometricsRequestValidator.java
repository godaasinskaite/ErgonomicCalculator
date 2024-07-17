package com.app.ErgonomicCalculator.validator;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
@Slf4j
public class AnthropometricsRequestValidator {
    public void validate(AnthropometricsRequestDto anthropometrics) throws InvalidDataException, IllegalAccessException {
        Field[] fieldsToValidate = anthropometrics.getClass().getDeclaredFields();
        for (Field field : fieldsToValidate) {
            field.setAccessible(true);
            validateField(anthropometrics, field);
        }
    }

    private static void validateField(AnthropometricsRequestDto anthropometrics, Field field) throws IllegalAccessException, InvalidDataException {
        Object value = field.get(anthropometrics);
        if (value instanceof String) {
            validateStringField(field, (String) value);
        } else if (field.getType() == Double.class) {
            validateDoubleField(field, value);
        }
    }

    private static void validateDoubleField(Field field, Object value) throws InvalidDataException {
        if (value == null || (Double) value == 0.0) {
            log.error("Validation failed. Field '" + field.getName() + "' is null or zero.");
            throw new InvalidDataException("Field '" + field.getName() + "' must not be null or zero.");
        }
    }

    private static void validateStringField(Field field, String value) throws InvalidDataException {
        if (value.trim().isEmpty()) {
            log.error("Validation failed. Field '" + field.getName() + "' is null or empty.");
            throw new InvalidDataException("Field '" + field.getName() + "' must not be null or empty.");
        }
    }
}
