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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PersonAuthProvider personAuthProvider;

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
