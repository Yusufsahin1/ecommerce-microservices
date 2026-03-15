package com.ecommerce_microservices.product_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CategoryDto(
    Long id,
    String name,
    String description,
    List<ProductDto> products,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
