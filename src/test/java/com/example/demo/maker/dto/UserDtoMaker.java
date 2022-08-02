package com.example.demo.maker.dto;

import com.example.demo.dto.UserDto;
import com.example.demo.maker.StubValues;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

public class UserDtoMaker {

    public static final Property<UserDto, Long> ID = new Property<>();

    public static final Property<UserDto, String> NAME = new Property<>();

    public static final Property<UserDto, String> PASSWORD = new Property<>();

    public static final Property<UserDto, String> ROLE = new Property<>();

    public static final Instantiator<UserDto> USER_DTO_INSTANTIATOR = propertyLookup -> {
        UserDto userDto = new UserDto();
        userDto.setId(propertyLookup.valueOf(ID, StubValues.STUB_LONG));
        userDto.setName(propertyLookup.valueOf(NAME, StubValues.STUB_STRING));
        userDto.setPassword(propertyLookup.valueOf(PASSWORD, StubValues.STUB_STRING));
        userDto.setRole(propertyLookup.valueOf(ROLE, StubValues.STUB_STRING));
        return userDto;
    };
}
