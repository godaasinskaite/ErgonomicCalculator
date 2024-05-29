package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AnthropometricsService {

    private final AnthropometricsRequestValidator anthropometricsRequestValidator;
    private final AnthropometricsRepository anthropometricsRepository;
    private final AnthropometricsMapper anthropometricsMapper;
    private final PersonService personService;

    public PersonAnthropometrics validateAndSaveAnthropometrics(final AnthropometricsRequestDto requestDto) throws InvalidDataException, IllegalAccessException {
        Person person = personService.findPersonByEmail(requestDto.getPersonEmail());
        log.info("Person with where email = " + requestDto.getPersonEmail() + " founded or created.");

        anthropometricsRequestValidator.validate(requestDto);
        if (person.getPersonAnthropometrics() != null) {
            PersonAnthropometrics anthropometrics = updateAnthropometrics(requestDto, person.getPersonAnthropometrics(), person);
            anthropometricsRepository.save(anthropometrics);
            return anthropometrics;
        }
        PersonAnthropometrics personAnthropometrics = anthropometricsMapper.toPersonAnthropometrics(requestDto);
        personAnthropometrics.setPerson(person);

        anthropometricsRepository.save(personAnthropometrics);
        log.info("Anthropometric data saved.");
        return personAnthropometrics;
    }

    public PersonAnthropometrics updateAnthropometrics(final AnthropometricsRequestDto requestDto, final PersonAnthropometrics anthropometrics, Person person) {
        BeanUtils.copyProperties(requestDto, anthropometrics);
        anthropometrics.setPerson(person);
        log.info("Person Anthropometrics updated.");
        return anthropometrics;
    }
}
