package com.ecommerce_microservices.user_service.service;

import com.ecommerce_microservices.user_service.dto.AddressDto;
import com.ecommerce_microservices.user_service.dto.CreateAddressRequest;
import com.ecommerce_microservices.user_service.dto.UpdateAddressRequest;
import com.ecommerce_microservices.user_service.dto.converter.AddressDtoConverter;
import com.ecommerce_microservices.user_service.exception.AddressNotFoundException;
import com.ecommerce_microservices.user_service.model.Address;
import com.ecommerce_microservices.user_service.model.User;
import com.ecommerce_microservices.user_service.repository.AddressRepository;
import com.ecommerce_microservices.user_service.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public AddressDto addAddress(String userEmail, CreateAddressRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        Address newAddress = new Address();
        newAddress.setTitle(request.title());
        newAddress.setCity(request.city());
        newAddress.setDistrict(request.district());
        newAddress.setAddressLine(request.addressLine());
        newAddress.setZipCode(request.zipCode());
        newAddress.setUser(user);

        Address savedAddress = addressRepository.save(newAddress);

        return AddressDtoConverter.convert(savedAddress);
    }

    public List<AddressDto> getAllAddressesByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        return user.getAddresses().stream()
                .map(AddressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public void deleteAddress(String userEmail, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to delete this address");
        }

        addressRepository.delete(address);
    }

    public AddressDto updateAddress(String userEmail, Long addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to update this address");
        }

        address.setTitle(request.title());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setAddressLine(request.addressLine());
        address.setZipCode(request.zipCode());

        Address updatedAddress = addressRepository.save(address);

        return AddressDtoConverter.convert(updatedAddress);
    }
}
