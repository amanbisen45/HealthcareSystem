package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Doctor;
import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.DoctorRepository;
import com.anudip.HealthcareSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository ;

    //Handles Registration part
    public boolean registerUser(User user, String speciality, Integer experience) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false; // Email already exists
        }
        // Save user first
        userRepository.save(user);

        // If role is DOCTOR, create and save a Doctor entity
        if (user.getRole() == Role.DOCTOR) {
            Doctor doctor = new Doctor(user, speciality, experience);
            doctorRepository.save(doctor);
        }

        return true;
    }


    //Find user by email
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    //Get all doctors from the database
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }


    // Get user by ID (doctor or patient)
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        // Return null if user not found
        return user.orElse(null);
    }
}
