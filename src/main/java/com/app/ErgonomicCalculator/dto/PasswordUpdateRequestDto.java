package com.app.ErgonomicCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequestDto {
    private String oldPassword;
    private String newPassword;
}
