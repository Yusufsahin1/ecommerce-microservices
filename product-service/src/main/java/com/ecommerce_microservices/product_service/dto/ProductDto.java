package com.ecommerce_microservices.product_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    CategoryDto category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
