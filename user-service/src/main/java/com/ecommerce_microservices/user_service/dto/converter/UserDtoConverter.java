package com.ecommerce_microservices.user_service.dto.converter;

import com.ecommerce_microservices.user_service.dto.UserDto;
import com.ecommerce_microservices.user_service.model.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDtoConverter {

    public static UserDto convert(User from) {
        return new UserDto(
                from.getId(),
                from.getEmail(),
                from.getFullName(),
                from.getRole(),
                from.getAddresses() != null 
                        ? from.getAddresses().stream()
                              .map(AddressDtoConverter::convert)
                              .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}
