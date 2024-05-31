package com.app.ErgonomicCalculator.config;

import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.exception.PersonNotFoundException;
import com.app.ErgonomicCalculator.mapper.PersonMapper;
import com.app.ErgonomicCalculator.model.Person;
import com.app.ErgonomicCalculator.repository.PersonRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class PersonAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Creates a JWT token for the provided person Data Transfer Object.
     *
     * @param dto the person Data Transfer Object containing the user information
     * @return a JWT token string representing the authenticated user/
     */
    public String createToken(PersonDto dto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);

        return JWT.create()
                .withIssuer(dto.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName", dto.getFirstName())
                .withClaim("lastName", dto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * Validates the provided JWT token and retrieves user authentication details.
     *
     * @param token the JWT token string to validate.
     * @return Authentication object representing the authenticated user.
     */
    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        PersonDto person = PersonDto.builder()
                .email(decoded.getIssuer())
                .firstName(decoded.getClaim("firstName").asString())
                .lastName(decoded.getClaim("lastName").asString())
                .build();

        return new UsernamePasswordAuthenticationToken(person, null, Collections.emptyList());
    }

    /**
     * Validates the provided JWT token strongly and retrieves user authentication details.
     *
     * @param token the JWT token string to validate
     * @return Authentication object representing the authenticated user
     * @throws PersonNotFoundException if the user associated with the token can not be found.
     */
    public Authentication validateTokenStrongly(String token) throws PersonNotFoundException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        Person person = personRepository.findByEmail(decoded.getIssuer())
                .orElseThrow(() -> new PersonNotFoundException("Unknown user"));

        return new UsernamePasswordAuthenticationToken(personMapper.toPersonDto(person), null, Collections.emptyList());
    }
}
