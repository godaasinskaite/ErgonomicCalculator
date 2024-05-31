package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDtoAfterAuth;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.mapper.AnthropometricsMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import com.app.ErgonomicCalculator.repository.AnthropometricsRepository;
import com.app.ErgonomicCalculator.validator.AnthropometricsRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


/**
 * Service for anthropometric data related operations: validating, saving, updating.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnthropometricsService {

    private final AnthropometricsRequestValidator anthropometricsRequestValidator;
    private final AnthropometricsRepository anthropometricsRepository;
    private final AnthropometricsMapper anthropometricsMapper;
    private final PersonService personService;


    /**
     * Validates and saves the anthropometric data provided in the request DTO. If the person already
     * has existing anthropometric data, it updates the existing data.
     *
     * @param requestDto the Data Transfer Object containing the anthropometrics data to be validated and saved.
     * @return the saved or updated PersonAnthropometrics entity.
     * @throws InvalidDataException   if the provided anthropometrics data is invalid.
     * @throws IllegalAccessException if there is an access violation during validation or saving.
     */
    public PersonAnthropometrics validateAndSaveAnthropometrics(final AnthropometricsRequestDto requestDto) throws InvalidDataException, IllegalAccessException {
        Person person = personService.findPersonByEmail(requestDto.getPersonEmail());
        log.info("Person with where email = " + requestDto.getPersonEmail() + " founded or created.");

        anthropometricsRequestValidator.validate(requestDto);
        if (person.getPersonAnthropometrics() != null) {
            PersonAnthropometrics anthropometrics = updateAnthropometrics(requestDto, person.getPersonAnthropometrics(), person);
            anthropometricsRepository.save(anthropometrics);
            return anthropometrics;
        }
        final PersonAnthropometrics personAnthropometrics = anthropometricsMapper.toPersonAnthropometrics(requestDto);
        personAnthropometrics.setPerson(person);

        anthropometricsRepository.save(personAnthropometrics);
        log.info("Anthropometric data saved.");
        return personAnthropometrics;
    }


    /**
     * Method to update existing anthropometric data.
     *
     * @param requestDto      the Data Transfer Object containing the anthropometrics data.
     * @param anthropometrics existing anthropometric data to be updated.
     * @param person          Person entity representing whose anthropometric data is being updated.
     * @return updated anthropometrics entity.
     */
    public PersonAnthropometrics updateAnthropometrics(final AnthropometricsRequestDto requestDto, final PersonAnthropometrics anthropometrics, Person person) {
        BeanUtils.copyProperties(requestDto, anthropometrics);
        anthropometrics.setPerson(person);
        log.info("Person Anthropometrics updated.");
        return anthropometrics;
    }

    /**
     * This method is typically called when person saves or updates anthropometric data after authenticates themselves.
     *
     * @param dtoToMap the Data Transfer Object containing the anthropometric data.
     * @param email    unique identifier of Person entity.
     * @return AnthropometricsRequestDto object suitable for further anthropometric data validation.
     */
    public AnthropometricsRequestDto mapRequests(final AnthropometricsRequestDtoAfterAuth dtoToMap, String email) {
        final AnthropometricsRequestDto dto = anthropometricsMapper.toAnthropometricsRequestDto(dtoToMap);
        dto.setPersonEmail(email);
        return dto;
    }
}
