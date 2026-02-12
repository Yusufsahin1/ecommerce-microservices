package com.ecommerce_microservices.user_service.service;

import com.ecommerce_microservices.user_service.dto.CreateUserRequest;
import com.ecommerce_microservices.user_service.dto.LoginRequest;
import com.ecommerce_microservices.user_service.dto.UserDto;
import com.ecommerce_microservices.user_service.dto.converter.UserDtoConverter;
import com.ecommerce_microservices.user_service.model.User;
import com.ecommerce_microservices.user_service.model.UserRole;
import com.ecommerce_microservices.user_service.repository.UserRepository;
import com.ecommerce_microservices.user_service.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    private UserDto saveUser(CreateUserRequest request, UserRole role) {
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);

        return UserDtoConverter.convert(savedUser);
    }

    public UserDto createCustomer(CreateUserRequest request) {
        return saveUser(request, UserRole.CUSTOMER);
    }

    public UserDto createAdmin(CreateUserRequest request) {
        return saveUser(request, UserRole.ADMIN);
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return UserDtoConverter.convert(user);
    }
}
