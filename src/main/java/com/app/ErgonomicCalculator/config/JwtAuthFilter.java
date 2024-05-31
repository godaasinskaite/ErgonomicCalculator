package com.app.ErgonomicCalculator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final PersonAuthProvider personAuthProvider;

    /**
     * Performs authentication and authorization based on the provided JWT token in the request header.
     * Extracts and validates a JWT token from the Authorization header of the incoming HTTP request.
     * Sets the authenticated user's details in the security context based on the token.
     * Authentication method varies by HTTP method: standard validation for GET requests, stronger validation for others.
     * Invalid or expired tokens clear the security context.
     *
     * @param request     the HTTP servlet request
     * @param response    the HTTP servlet response
     * @param filterChain the filter chain for executing subsequent filters
     * @throws ServletException if the filter encounters any servlet-related issues
     * @throws IOException      if an I/O error occurs during filter execution
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    if ("GET".equals(request.getMethod())) {
                        SecurityContextHolder.getContext().setAuthentication(personAuthProvider.validateToken(authElements[1]));
                    } else {
                        SecurityContextHolder.getContext().setAuthentication(personAuthProvider.validateTokenStrongly(authElements[1]));
                    }
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
