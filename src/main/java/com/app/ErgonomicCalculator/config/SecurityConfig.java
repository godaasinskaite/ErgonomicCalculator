package com.app.ErgonomicCalculator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PersonAuthProvider personAuthProvider;

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object used to configure security settings.
     * @return a SecurityFilterChain instance representing the configured security filter chain.
     * @throws Exception if an error occurs during security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors()
                .and()
                .addFilterBefore(new JwtAuthFilter(personAuthProvider), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) ->
                        requests
                                .antMatchers(HttpMethod.POST, "/api/person/login").permitAll()
                                .antMatchers(HttpMethod.PUT, "/api/person/register").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/ergonomic/openPdf/**").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/ergonomic/new").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/api/person/delete/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/ergonomic/download/**").permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();
    }
}
