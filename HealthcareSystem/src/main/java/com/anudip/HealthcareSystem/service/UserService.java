package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private com.anudip.HealthcareSystem.repository.UserRepository userRepository;

    //Handles Registration part
    public boolean registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return false; // User already exists
        }

        userRepository.save(user);
        return true; // Successfully registered
    }
    //Get all doctors from the database
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    // Get user by ID (doctor or patient)
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);  // Return null if user not found
    }
}
