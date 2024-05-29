package com.app.ErgonomicCalculator.mapper;

import com.app.ErgonomicCalculator.dto.PersonDto;
import com.app.ErgonomicCalculator.dto.RegisterDto;
import com.app.ErgonomicCalculator.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto toPersonDto(Person person);

    @Mapping(target = "password", ignore = true)
    Person toPerson(RegisterDto registerDto);
}
