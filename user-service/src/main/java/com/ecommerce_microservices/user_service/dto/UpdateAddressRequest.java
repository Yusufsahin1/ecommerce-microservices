package com.ecommerce_microservices.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAddressRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "District is required")
        String district,

        @NotBlank(message = "Address line is required")
        String addressLine,

        String zipCode
) {}
