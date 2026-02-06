package com.ecommerce_microservices.user_service.dto.converter;

import com.ecommerce_microservices.user_service.dto.AddressDto;
import com.ecommerce_microservices.user_service.model.Address;

public class AddressDtoConverter {

    public static AddressDto convert(Address from) {
        return new AddressDto(
                from.getId(),
                from.getTitle(),
                from.getCity(),
                from.getDistrict(),
                from.getAddressLine(),
                from.getZipCode()
        );
    }
}
