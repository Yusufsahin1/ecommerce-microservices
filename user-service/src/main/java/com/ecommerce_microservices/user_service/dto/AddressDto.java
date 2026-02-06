package com.ecommerce_microservices.user_service.dto;

public record AddressDto(
        Long id,
        String title,
        String city,
        String district,
        String addressLine,
        String zipCode
) {}
