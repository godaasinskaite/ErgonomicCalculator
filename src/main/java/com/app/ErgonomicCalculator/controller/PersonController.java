package com.app.ErgonomicCalculator.controller;

import com.app.ErgonomicCalculator.config.PersonAuthProvider;
import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.PasswordUpdateRequestDto;
import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.IncorrectPasswordException;
import com.app.ErgonomicCalculator.exception.InvalidDataException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.exception.ServiceException;
import com.app.ErgonomicCalculator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/person")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    private final PersonService personService;
    private final PersonAuthProvider personAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final CredentialsDto credentialsDto) throws ServiceException, PersonNotFoundException {
        var person = personService.login(credentialsDto);
        person.setToken(personAuthProvider.createToken(person));
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody final RegisterDto registerDto) throws ServiceException {
        var person = personService.registerPerson(registerDto);
        person.setToken(personAuthProvider.createToken(person));
        System.out.println(person.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody final PasswordUpdateRequestDto passwordUpdateRequestDto, Authentication authentication) throws PersonNotFoundException, IncorrectPasswordException, InvalidDataException {
        var personDto = (PersonDto) authentication.getPrincipal();
        personService.updatePassword(passwordUpdateRequestDto, personDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deletePerson(@PathVariable final String email) throws PersonNotFoundException, IOException {
        personService.delete(email);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Person deleted successfully");
        return ResponseEntity.ok().body(response);
    }
}
