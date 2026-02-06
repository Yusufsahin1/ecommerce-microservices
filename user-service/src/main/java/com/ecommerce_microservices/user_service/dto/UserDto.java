package com.ecommerce_microservices.user_service.dto;

import com.ecommerce_microservices.user_service.model.UserRole;
import java.util.List;

public record UserDto(
        Long id,
        String email,
        String fullName,
        UserRole role,
        List<AddressDto> addresses
) {}
