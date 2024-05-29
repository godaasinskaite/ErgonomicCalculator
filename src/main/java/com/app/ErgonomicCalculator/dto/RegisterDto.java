package com.app.ErgonomicCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private char[] password;
}
