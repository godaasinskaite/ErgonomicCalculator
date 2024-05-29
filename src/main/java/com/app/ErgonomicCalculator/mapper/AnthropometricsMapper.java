package com.app.ErgonomicCalculator.mapper;

import com.app.ErgonomicCalculator.dto.AnthropometricsRequestDto;
import com.app.ErgonomicCalculator.model.PersonAnthropometrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnthropometricsMapper {

    PersonAnthropometrics toPersonAnthropometrics(AnthropometricsRequestDto anthropometricsRequestDto);
}
