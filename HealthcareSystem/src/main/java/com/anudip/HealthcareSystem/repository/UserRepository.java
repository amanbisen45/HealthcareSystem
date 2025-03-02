package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}
