package com.ecommerce_microservices.product_service.dto.converter;

import com.ecommerce_microservices.product_service.dto.CategoryDto;
import com.ecommerce_microservices.product_service.dto.ProductDto;
import com.ecommerce_microservices.product_service.model.Category;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryDtoConverter {

    public static CategoryDto convert(Category from) {
        return new CategoryDto(
            from.getId(),
            from.getName(),
            from.getDescription(),
            from.getProducts() != null
                    ? from.getProducts().stream()
                          .map(ProductDtoConverter::convert)
                          .collect(Collectors.toList())
                    : new ArrayList<>(),
            from.getCreatedAt(),
            from.getUpdatedAt()
        );
    }
}
