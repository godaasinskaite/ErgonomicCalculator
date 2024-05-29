package com.app.ErgonomicCalculator.validator;

import com.app.ErgonomicCalculator.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PasswordValidator {

    public void validate(String passwordToValidate) throws InvalidDataException {
        if (passwordToValidate.length() < 8) {
            throw new InvalidDataException("Password is too short");
        }
    }
}
