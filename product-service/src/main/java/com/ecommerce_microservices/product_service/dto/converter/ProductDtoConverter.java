package com.ecommerce_microservices.product_service.dto.converter;

import com.ecommerce_microservices.product_service.dto.CategoryDto;
import com.ecommerce_microservices.product_service.dto.ProductDto;
import com.ecommerce_microservices.product_service.model.Product;

public class ProductDtoConverter {

    public static ProductDto convert(Product from) {
        return new ProductDto(
            from.getId(),
            from.getName(),
            from.getDescription(),
            from.getPrice(),
            from.getStock(),
            CategoryDtoConverter.convert(from.getCategory()),
            from.getCreatedAt(),
            from.getUpdatedAt()
        );
    }
}
