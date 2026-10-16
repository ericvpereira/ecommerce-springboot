package com.eric.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

	Optional<AppUser> findByEmail(String email);

	boolean existsByEmail(String email);

}
