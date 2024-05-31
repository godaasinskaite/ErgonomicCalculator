package com.app.ErgonomicCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricsRequestDtoAfterAuth {
    private Double height;
    private Double sittingHeight;
    private Double shoulderHeight;
    private Double lowerLegLength;
    private Double hipBreadth;
    private Double elbowHeight;
    private Double eyeHeightStanding;
    private Double elbowHeightStanding;
    private Double thighClearance;
    private Double eyeHeight;
    private Double shoulderBreadth;
    private Double kneeHeight;
}
