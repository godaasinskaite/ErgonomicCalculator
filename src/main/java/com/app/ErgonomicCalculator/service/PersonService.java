package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.config.PersonAuthProvider;
import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.PasswordUpdateRequestDto;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.IncorrectPasswordException;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.mapper.PersonMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.repository.PersonRepository;
import com.app.ErgonomicCalculator.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Service for person related operations: finding, adding, deleting, updating, registering.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordUpdateRequestValidator;
    private final PersonAuthProvider personAuthProvider;


    /**
     * Method finds a person by their email address. If no person is found with the given email address,
     * a new person is created with the provided email and saved to the database.
     *
     * @param personEmail the email address of the person to be found or created.
     * @return the Person object corresponding to the given email address, either found or newly created.
     */
    public Person findPersonByEmail(final String personEmail) {
        return personRepository.findByEmail(personEmail)
                .orElseGet(() -> {
                    final var newPerson = Person.builder()
                            .email(personEmail)
                            .build();
                    return personRepository.save(newPerson);
                });
    }

    /**
     * Method to check if received credentials is valid for authentication.
     *
     * @param credentialsDto the Data Transfer Object containing person email and password.
     * @return PersonDto holding name, lastName, generated token, unique identifiers id and email.
     * @throws PersonNotFoundException if person with given email can not be found.
     */
    public PersonDto login(final CredentialsDto credentialsDto) throws PersonNotFoundException, IncorrectPasswordException {
        Person person = personRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new PersonNotFoundException("Unknown user"));

        checkIfPasswordValid(credentialsDto.getPassword(), person.getPassword());

        return mapPersonDtoAndSetToken(person);
    }


    /**
     * Method to register person by given registerDto data.
     * If person by unique email is found in the database and password is null,
     * existing entity is updated.
     * When person is not found - new entity is created and saved to database.
     *
     * @param registerDto the Data Transfer Object containing information for registration.
     * @return PersonDto holding name, lastName, generated token, unique identifiers id and email.
     */
    public PersonDto registerPerson(final RegisterDto registerDto) {
        log.info("Looking for a person with email " + registerDto.getEmail());
        Optional<Person> oPerson = personRepository.findByEmail(registerDto.getEmail());

        if (oPerson.isPresent() && oPerson.get().getPassword() == null) {
            return updateAndRegisterExistingPerson(registerDto, oPerson.get());
        }
        Person person;
        person = personMapper.toPerson(registerDto);
        person.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));
        log.info("Person created.");
        personRepository.save(person);

        return mapPersonDtoAndSetToken(person);
    }


    /**
     * Private method to update existing Person after registration.
     *
     * @param registerDto    contains necessary information of person to register.
     * @param existingPerson is already existing object in DB and needs to be updated.
     */
    private PersonDto updateAndRegisterExistingPerson(RegisterDto registerDto, Person existingPerson) {
        existingPerson.setFirstName(registerDto.getFirstName());
        existingPerson.setLastName(registerDto.getLastName());
        existingPerson.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));
        personRepository.save(existingPerson);

        log.info("Existing person updated.");

        return mapPersonDtoAndSetToken(existingPerson);
    }

    /**
     * Maps a {@link Person} to a {@link PersonDto} and sets an authentication token.
     *
     * @param person the {@link Person} entity to be mapped to a {@link PersonDto}.
     * @return the {@link PersonDto} with the authentication token set.
     */
    private PersonDto mapPersonDtoAndSetToken(Person person) {
        final var personDto = personMapper.toPersonDto(person);
        personDto.setToken(personAuthProvider.createToken(personDto));
        return personDto;
    }

    /**
     * If unregistered person click on specific buttons, their data is deleted from database.
     *
     * @param email unique identifier to find a person to delete.
     * @throws PersonNotFoundException if the person by given unique identifier can not be found.
     */
    public void delete(final String email) throws PersonNotFoundException, IOException {
        log.info("Looking for a person with email " + email);
        final Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new PersonNotFoundException("Unknown user"));

        if (person.getPassword() != null) {
            return;
        }

        Files.deleteIfExists(Path.of(person.getWorkspaceMetrics().getImagePath()));
        personRepository.delete(person);
        log.info("Person deleted successfully");
    }


    /**
     * Method to update password and save updated value to database.
     *
     * @param requestDto     the Data Transfer Object containing new password and current password to validate if update is allowed.
     * @param authentication the authentication object containing user credentials and authorization details
     * @throws PersonNotFoundException    if person by given email is not found.
     * @throws IncorrectPasswordException if password received from requestDto does not match with person's password from the db.
     * @throws InvalidDataException       if password does not pass validation.
     */
    public PersonDto updatePassword(final PasswordUpdateRequestDto requestDto, Authentication authentication) throws PersonNotFoundException, IncorrectPasswordException, InvalidDataException {
        PersonDto personDto = (PersonDto) authentication.getPrincipal();
        String email = personDto.getEmail();

        log.info("Looking for a person with email " + email);
        final Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new PersonNotFoundException("Person can not be found."));

        checkIfPasswordValid(requestDto.getOldPassword(), person.getPassword());

        final String newPassword = requestDto.getNewPassword();
        passwordUpdateRequestValidator.validate(newPassword);
        person.setPassword(passwordEncoder.encode(newPassword));
        log.info("Password updated successfully");

        personRepository.save(person);
        return personDto;
    }

    /**
     * Checks if password from received credentials matches the person's password from database.
     *
     * @param password       is the password from credentialsDto.
     * @param personPassword is the password retrieved from database.
     * @throws IncorrectPasswordException if passwords do not match.
     */
    private void checkIfPasswordValid(String password, String personPassword) throws IncorrectPasswordException {
        if (!passwordEncoder.matches(CharBuffer.wrap(password), personPassword)) {
            throw new IncorrectPasswordException("Incorrect password.");
        }
    }
}
