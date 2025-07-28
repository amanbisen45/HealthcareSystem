package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;

public interface LoginRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword (String Email, String Password);
}
