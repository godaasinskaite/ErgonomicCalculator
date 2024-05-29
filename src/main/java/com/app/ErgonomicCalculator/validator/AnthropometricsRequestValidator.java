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
            Object value = field.get(anthropometrics);

            if (value instanceof String) {
                if (((String) value).trim().isEmpty()) {
                    log.error("Validation failed. Field '" + field.getName() + "' is null or empty.");
                    throw new InvalidDataException("Field '" + field.getName() + "' must not be null or empty.");
                }
            }
            else if (field.getType() == Double.class) {
                if (value == null || (Double) value == 0.0) {
                    log.error("Validation failed. Field '" + field.getName() + "' is null or zero.");
                    throw new InvalidDataException("Field '" + field.getName() + "' must not be null or zero.");
                }
            }
        }
    }
}
