package com.app.ErgonomicCalculator.service;

import com.app.ErgonomicCalculator.dto.CredentialsDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.exception.IncorrectPasswordException;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.exception.ServiceException;
import com.app.ErgonomicCalculator.mapper.PersonMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {


    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    @Test
    void findPersonByEmail() {
    }

    @Test
    void login() throws IncorrectPasswordException {
        var email = "goda@mail.com";
        var password = "password123";
        var credentialsDto = CredentialsDto.builder()
                .email(email)
                .password("anotheremail")
                .build();

        var person = Person.builder()
                .email(email)
                .password(password)
                .build();

        when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));

        assertThrows(IncorrectPasswordException.class, () -> personService.login(credentialsDto));
    }

//    @Test
//    void registerPerson() throws ServiceException {
//
//        var registerDto = RegisterDto.builder()
//                .email("goda@mail.com")
//                .firstName("name")
//                .lastName("lastname")
//                .password("password123".toCharArray())
//                .build();
//
//        var person = Person.builder()
//                .id(1L)
//                .email("goda@email.com")
//                .firstName("name")
//                .lastName("lastname")
//                .password("password123")
//                .build();
//
//        when(personRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(person));
//        assertThrows(ServiceException.class, () -> personService.registerPerson(registerDto));
//    }
}