package com.ecommerce_microservices.user_service.config;

import com.ecommerce_microservices.user_service.dto.CreateUserRequest;
import com.ecommerce_microservices.user_service.repository.UserRepository;
import com.ecommerce_microservices.user_service.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    public DataSeeder(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        if (userRepository.count() == 0) {
            CreateUserRequest adminRequest = new CreateUserRequest(
                    "admin@ecommerce.com",
                    "admin123",
                    "System Admin"
            );
            userService.createAdmin(adminRequest);
            System.out.println("Admin user created successfully.");
        }
    }
}
