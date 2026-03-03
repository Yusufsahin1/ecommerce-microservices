package com.ecommerce_microservices.user_service.controller;

import com.ecommerce_microservices.user_service.dto.*;
import com.ecommerce_microservices.user_service.service.AddressService;
import com.ecommerce_microservices.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createCustomer(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<AddressDto> addAddress(Authentication authentication, @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(authentication.getName(), request));
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddresses(Authentication authentication) {
        return ResponseEntity.ok(addressService.getAllAddressesByUserEmail(authentication.getName()));
    }

    @PutMapping("/me/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(
            Authentication authentication, @PathVariable Long addressId, @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(authentication.getName(), addressId, request));
    }

    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(Authentication authentication, @PathVariable Long addressId) {
        addressService.deleteAddress(authentication.getName(), addressId);
        return ResponseEntity.noContent().build();
    }
}
