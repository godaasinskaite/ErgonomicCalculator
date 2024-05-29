package com.app.ErgonomicCalculator.dto;

import com.app.ErgonomicCalculator.model.Person;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnthropometricsRequestDto {

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
    private String personEmail;
}
