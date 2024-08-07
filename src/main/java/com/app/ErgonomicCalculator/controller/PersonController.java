package com.app.ErgonomicCalculator.controller;

import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.PasswordUpdateRequestDto;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.IncorrectPasswordException;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * Controller for handling requests related to person: logging, registering, updating, deleting.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/person")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    private final PersonService personService;


    /**
     * Handles user login by validating credentials and generating an authentication token.
     * Receives login credentials, validates them, and if successful, generates a token for the user.
     * The authenticated user's details along with the token are returned in the response.
     *
     * @param credentialsDto the Data Transfer Object containing the user's login credentials.
     * @return ResponseEntity containing the authenticated user's details and token.
     * @throws PersonNotFoundException    if no user is found with the provided credentials.
     * @throws IncorrectPasswordException if password in the credentials is incorrect.
     */
    @PostMapping("/login")
    public ResponseEntity<PersonDto> login(@RequestBody final CredentialsDto credentialsDto) throws PersonNotFoundException, IncorrectPasswordException {
        var person = personService.login(credentialsDto);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    /**
     * Handles user register by checking if person already exists and generating an authentication token if service method successful.
     * The authenticated user's details along with the token are returned in the response.
     *
     * @param registerDto the Data Transfer Object containing user's register credentials.
     * @return ResponseEntity containing the authenticated user's details and token.
     */
    @PutMapping("/register")
    public ResponseEntity<PersonDto> register(@RequestBody final RegisterDto registerDto) {
        var person = personService.registerPerson(registerDto);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    /**
     * Handles password update request by checking if the new password is valid and the old password matches password from database.
     *
     * @param passwordUpdateRequestDto the Data Transfer Object containing new password and old password to validate operation.
     * @param authentication           the authentication information from the current person, used to recognize the user.
     * @return ResponseEntity containing the authenticated user's details and token.
     * @throws PersonNotFoundException    if person with authenticated email can not be found.
     * @throws IncorrectPasswordException if password to validate operation does not match person's password.
     * @throws InvalidDataException       if password validation fails.
     */
    @PutMapping("/updatePassword")
    public ResponseEntity<PersonDto> updatePassword(@RequestBody final PasswordUpdateRequestDto passwordUpdateRequestDto, final Authentication authentication) throws PersonNotFoundException, IncorrectPasswordException, InvalidDataException {
        var personDto = personService.updatePassword(passwordUpdateRequestDto, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }

    /**
     * Method to delete unregistered person information from DB and local resources.
     *
     * @param email is the unique identifier of person to be deleted.
     * @return ResponseEntity containing status and message indicating the result of the operation.
     * @throws PersonNotFoundException if the person with given id can not be found.
     * @throws IOException             if an input/output error occurs during processing.
     */
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deletePerson(@PathVariable final String email) throws PersonNotFoundException, IOException {
        personService.delete(email);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Person deleted successfully\"}");
    }
}
