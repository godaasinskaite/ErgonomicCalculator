package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.PasswordUpdateRequestDto;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.IncorrectPasswordException;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.exception.ServiceException;
import com.app.ErgonomicCalculator.mapper.PersonMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.repository.PersonRepository;
import com.app.ErgonomicCalculator.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordUpdateRequestValidator;

    public Person findPersonByEmail(final String personEmail) {
        return personRepository.findByEmail(personEmail)
                .orElseGet(() -> {
                    final var newPerson = Person.builder()
                            .email(personEmail)
                            .build();
                    return personRepository.save(newPerson);
                });
    }

    public PersonDto login(final CredentialsDto credentialsDto) throws PersonNotFoundException, ServiceException {
        Person person = personRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new PersonNotFoundException("Unknown user"));

        if (isPasswordInvalid(credentialsDto.getPassword(), person.getPassword())) {
            throw new ServiceException("Invalid Password");
        }
        return personMapper.toPersonDto(person);
    }

    private boolean isPasswordInvalid(String password, String personPassword) {
        return !passwordEncoder.matches(CharBuffer.wrap(password), personPassword);
    }

    public PersonDto registerPerson(final RegisterDto registerDto) throws ServiceException {
        Optional<Person> oPerson = personRepository.findByEmail(registerDto.getEmail());

        if (oPerson.isPresent()) {
            Person existingPerson = oPerson.get();

            if (existingPerson.getPassword() != null) {
                throw new ServiceException("User with provided email already exists.");
            }
            existingPerson.setFirstName(registerDto.getFirstName());
            existingPerson.setLastName(registerDto.getLastName());
            existingPerson.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));
            System.out.println(existingPerson);
            personRepository.save(existingPerson);

            return personMapper.toPersonDto(existingPerson);
        } else {
            Person person = personMapper.toPerson(registerDto);
            person.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));

        }
        Person person = personMapper.toPerson(registerDto);
        person.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));
        personRepository.save(person);
        return personMapper.toPersonDto(person);
    }

    public void delete(final String email) throws PersonNotFoundException, IOException {
        log.info("Looking for a person with email " + email);
        final Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new PersonNotFoundException("Unknown user"));

        Files.deleteIfExists(Path.of(person.getWorkspaceMetrics().getImagePath()));
        personRepository.delete(person);
    }

    public void updatePassword(final PasswordUpdateRequestDto requestDto, String email) throws PersonNotFoundException, IncorrectPasswordException, InvalidDataException {
        log.info("Looking for a person with email " + email);
        final Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new PersonNotFoundException("Person can not be found."));

        if (isPasswordInvalid(requestDto.getOldPassword(), person.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password.");
        }

        var newPassword = requestDto.getNewPassword();
        passwordUpdateRequestValidator.validate(newPassword);
        person.setPassword(passwordEncoder.encode(newPassword));
        log.info("Password updated successfully");
        personRepository.save(person);
    }
}
