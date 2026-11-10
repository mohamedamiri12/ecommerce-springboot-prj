package com.amiri.ecommerce_api;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
