package com.ecommerce_microservices.user_service.repository;

import com.ecommerce_microservices.user_service.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
